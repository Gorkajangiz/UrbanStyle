package EJBs;

import EJBs.ClientEJB.Login;
import static EJBs.ClientEJB.Register.FIELDS_EMPTY;
import static EJBs.ClientEJB.Register.REGISTER_EMAIL_IN_USE;
import static EJBs.ClientEJB.Register.REGISTER_SUCCESS;
import dao.DaoInterfazClient;
import dao.DaoInterfazProduct;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import javax.sql.DataSource;
import modelo.Client;

@Stateless
public class ClientEJB implements ClientEJBLocal {

    //Inyecto EJBs
    @EJB
    private DaoInterfazProduct productDao;
    @EJB
    private DaoInterfazClient clientDao;

    //Enums de resultados para login y register
    public static enum Login {
        LOGIN_SUCCESS, LOGIN_EMAIL_FAILURE, LOGIN_PASSWD_FAILURE
    }

    public static enum Register {
        REGISTER_SUCCESS, REGISTER_EMAIL_IN_USE, FIELDS_EMPTY
    }

    //Método de login
    @Override
    public Login login(String email, String password) {

        Optional<Client> cliente = Optional.empty();
        try {
            cliente = clientDao.findByMail(email);
            if (cliente.isPresent()) {
                //Si hay un cliente con es email, restaura la contraseña y si es la misma
                //devuelve exito, si no fallo
                String passwordDecrypted = this.passwordCorrector(cliente.get().getPassword());
                if (passwordDecrypted.equals(password)) {
                    return Login.LOGIN_SUCCESS;
                } else {
                    return Login.LOGIN_PASSWD_FAILURE;
                }
            } else {
                //Si no encuentra el email email incorrecto
                return Login.LOGIN_EMAIL_FAILURE;
            }
        } catch (Exception ex) {
            System.err.println("ERROR en ClientEJB.login(): " + ex.getMessage());
            ex.printStackTrace();
            return Login.LOGIN_EMAIL_FAILURE;
        }
    }

    //Método de registrarse
    @Override
    public Register register(String name, String email, String password) {
        Register retorno = null;
        try {
            //Si el cliente no esta con el email (no esta registrado), hace comprobaciones
            if (!clientDao.findByMail(email).isPresent()) {
                if (email.isBlank() || name.isBlank() || password.isBlank()) {
                    //Campos vacíos
                    retorno = FIELDS_EMPTY;
                } else {
                    //Encripta la contraseña y crea un cliente nuevo y lo inserta
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
                //Si el cliente si que estaba en la base, email en uso
                retorno = REGISTER_EMAIL_IN_USE;
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return retorno;
    }

    //Rescata todos los clientes
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

    //Buscar por nombre
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

    //Buscador por email
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
        //METODOS VACÍOS, HERENCIA DEL DAO GENÉRICO
    }

    @Override
    public int deleteId(Long id) {
        //METODOS VACÍOS, HERENCIA DEL DAO GENÉRICO
        return 0;
    }

    @Override
    public int update(Client c) {
        //METODOS VACÍOS, HERENCIA DEL DAO GENÉRICO
        return 0;
    }

    //Estos son mis métodos para esconder las contraseñas antes de aprender a encriptarlas
    //con metodos propios de java. Básicamente lo que hace es añadir un caracter & antes
    //de la linea de texto de la contraseña que representa el número de posiciones que
    //tiene que mover cada letra en una dirección para encriptarla. Si cambias ese carácter
    //cambia el numero de movimientos y se encripta con otra clave. Es mega fácil de descifrar
    //pero no soy el CNI
    @Override
    public String passwordScrambler(String clientPassword) {
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

    //Este lo revierte
    @Override
    public String passwordCorrector(String scrambledPassword) {
        String original = "";
        int offset = scrambledPassword.charAt(0) - 33;
        for (int i = 1; i < scrambledPassword.length(); i++) {
            char c = scrambledPassword.charAt(i);
            c -= offset;
            original += c;
        }
        return original;
    }
}
