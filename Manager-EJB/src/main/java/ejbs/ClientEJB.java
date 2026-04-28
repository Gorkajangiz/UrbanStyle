/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejbs;

import dao.DaoInterfazClient;
import dao.DaoInterfazProduct;
import static ejbs.ClientEJB.Register.FIELDS_EMPTY;
import static ejbs.ClientEJB.Register.REGISTER_EMAIL_IN_USE;
import static ejbs.ClientEJB.Register.REGISTER_SUCCESS;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import modelo.Client;
import modelo.Product;

/**
 *
 * @author edria
 */
@Stateless
public class ClientEJB implements ClientEJBLocal {

    @EJB
    private DaoInterfazClient clientDao;

    @EJB
    private DaoInterfazProduct productDao;

    public static enum Login {
        LOGIN_SUCCESS, LOGIN_EMAIL_FAILURE, LOGIN_PASSWD_FAILURE
    }

    public static enum Register {
        REGISTER_SUCCESS, REGISTER_EMAIL_IN_USE, FIELDS_EMPTY
    }

    @Override
    public Register register(String name, String email, String password) {
        Register retorno = null;
        try {
            if (!clientDao.findByMail(email).isPresent()) {
                if (email.isBlank() || name.isBlank() || password.isBlank()) {
                    retorno = FIELDS_EMPTY;
                } else {
                    String temp = this.passwordScrambler(password);
                    Client c = new Client();
                    c.setName(name);
                    c.setEmail(email);
                    c.setPassword(temp);
                    c.setDiscount(0);
                    c.setPurchases(0);
                    clientDao.insert(c);
                    retorno = REGISTER_SUCCESS;
                }
            } else {
                retorno = REGISTER_EMAIL_IN_USE;
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return retorno;
    }

    private String passwordScrambler(String clientPassword) {
        String password = "";
        int offset = clientPassword.charAt(1) % 10 + 5;
        password += (char) (offset + 33);
        for (int i = 0; i < clientPassword.length(); i++) {
            char c = clientPassword.charAt(i);
            c += offset;
            password += c;
        }
        return password;
    }

    private String passwordCorrector(String scrambledPassword) {
        String original = "";
        int offset = scrambledPassword.charAt(0) - 33;
        for (int i = 1; i < scrambledPassword.length(); i++) {
            char c = scrambledPassword.charAt(i);
            c -= offset;
            original += c;
        }
        return original;
    }

    @Override
    public Collection<Client> getClients() {
        Collection<Client> clients = new ArrayList<>();
        try {
            clients = clientDao.findAll();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return clients;
    }

    @Override
    public Collection<Client> getByName(String name) {
        Collection<Client> clients = new ArrayList<>();
        try {
            clients = clientDao.findByName(name);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return clients;
    }

    @Override
    public Optional<Client> getByMail(String mail) {
        Optional<Client> cl = Optional.empty();
        try {
            cl = clientDao.findByMail(mail);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return cl;
    }

    @Override
    public void insert(Client c) {
        try {
            clientDao.insert(c);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public int deleteId(Long id) {
        int i = 0;
        try {
            i = clientDao.delete(id);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return i;
    }

    @Override
    public int update(Client c) {
        int i = 0;
        try {
            i = clientDao.update(c);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return i;
    }

    @Override
    public Collection<Product> getProducts() {
        Collection<Product> products = new ArrayList<>();
        try {
            products = productDao.findAll();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return products;
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        Optional<Product> product = Optional.empty();
        try {
            product = productDao.findById(id);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return product;
    }

    @Override
    public Collection<Product> getProductsByName(String name) {
        Collection<Product> products = new ArrayList<>();
        try {
            products = productDao.findByName(name);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return products;
    }

    @Override
    public void insertProduct(Product product) {
        try {
            productDao.insert(product);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public int updateProduct(Product product) {
        int i = 0;
        try {
            i = productDao.update(product);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return i;
    }

    @Override
    public int deleteProductId(Long id) {
        int i = 0;
        try {
            i = productDao.delete(id);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return i;
    }

    @Override
    public Login login(String email, String password) {
        try {
            Optional<Client> cliente = clientDao.findByMail(email);
            if (cliente.isPresent()) {
                String passwordDecrypted = this.passwordCorrector(cliente.get().getPassword());
                if (passwordDecrypted.equals(password)) {
                    return Login.LOGIN_SUCCESS;
                } else {
                    return Login.LOGIN_PASSWD_FAILURE;
                }
            } else {
                return Login.LOGIN_EMAIL_FAILURE;
            }
        } catch (Exception ex) {
            System.err.println("ERROR en ClientEJB.login(): " + ex.getMessage());
            return Login.LOGIN_EMAIL_FAILURE;
        }
    }
}
