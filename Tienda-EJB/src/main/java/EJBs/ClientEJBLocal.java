/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package EJBs;

import EJBs.ClientEJB.Login;
import EJBs.ClientEJB.Register;
import jakarta.ejb.Local;
import java.util.Collection;
import java.util.Optional;
import modelo.Client;

/**
 *
 * @author edria
 */
@Local
public interface ClientEJBLocal {

    Login login(String email, String password);

    Register register(String name, String email, String password);

    public Collection<Client> getClients();

    public Collection<Client> getByName(String name);

    public Optional<Client> getByMail(String mail);

    public void insert(Client c);

    public int deleteId(Long id);

    public int update(Client c);

    public String passwordScrambler(String password);

    public String passwordCorrector(String password);

//    public void buy(Client c, Product p, BigDecimal price, Integer quantity);
}
