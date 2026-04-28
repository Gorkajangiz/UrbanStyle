/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import modelo.Client;

/**
 *
 * @author edria
 */
public interface DaoInterfazClient extends Dao<Client> {

    Collection<Client> findByName(String name) throws SQLException;

    Optional<Client> findByMail(String mail) throws SQLException;

    public void updatePurchase(Long idClient) throws SQLException;

    public int getPurchases(Long idClient) throws SQLException;

    public void updateDiscount(Long idClient, Integer discount) throws SQLException;
}
