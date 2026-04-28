/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package EJBs;

import jakarta.ejb.Local;
import java.util.Collection;
import java.util.Optional;
import modelo.Product;

/**
 *
 * @author edria
 */
@Local
public interface TiendaEJBLocal {

    public Collection<Product> getProducts();

    public Collection<Product> getByName(String name);

    public Optional<Product> getById(Long id);

    public void insert(Product product);

    public int deleteId(Long id);

    public int update(Product product);

    public Collection<Product> getProductsByCategory(String category);

    public Collection<Product> getProductsByDescription(String category);
}
