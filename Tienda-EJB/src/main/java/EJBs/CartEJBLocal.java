/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package EJBs;

import jakarta.ejb.Local;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collection;
import modelo.LineaProducto;
import modelo.Product;

/**
 *
 * @author edria
 */
@Local
public interface CartEJBLocal {

    public void addLine(LineaProducto lp);

    public void deleteLine(Product p, Integer cantidad);

    public void emptyCart() throws SQLException;

    public Collection<LineaProducto> getProducts();

    public int addCart(LineaProducto lp);

    public int getStockAvailability(Product product);

    public BigDecimal totalCalc(boolean logged, Integer discount);

    public int buy(Long idClient);

    public Product precioDescontado(boolean logged, Integer discount, Product p);

    public Product precioOriginal(Integer discount, Product p);

}
