/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.sql.SQLException;
import java.util.Collection;
import modelo.Product;

/**
 *
 * @author edria
 */
public interface DaoInterfazProduct extends Dao<Product> {

    Collection<Product> findByName(String name) throws SQLException;

    int getStock(Product p) throws SQLException;

    public void buy(Product p, Integer quantity) throws SQLException;
}
