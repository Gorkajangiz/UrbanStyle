package ejbs;

import java.util.Collection;
import java.util.Optional;
import modelo.Client;
import modelo.Product;

public interface ClientEJBLocal {

    ClientEJB.Register register(String name, String email, String password);

    Collection<Client> getClients();

    Collection<Client> getByName(String name);

    Optional<Client> getByMail(String mail);

    void insert(Client c);

    int deleteId(Long id);

    int update(Client c);

    ClientEJB.Login login(String email, String password);

    Collection<Product> getProducts();

    Optional<Product> getProductById(Long id);

    Collection<Product> getProductsByName(String name);

    void insertProduct(Product product);

    int updateProduct(Product product);

    int deleteProductId(Long id);
}
