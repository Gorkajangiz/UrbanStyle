/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SingletonEjbClass.java to edit this template
 */
package EJBs;

import dao.DaoInterfazClient;
import dao.DaoInterfazProduct;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import modelo.Product;

/**
 *
 * @author edria
 */
@Singleton
public class TiendaEJB implements TiendaEJBLocal {

    //Inyecto EJBs
    @EJB
    private DaoInterfazProduct productDao;
    @EJB
    private DaoInterfazClient clientDao;

    //recoger productos normal
    @Override
    public Collection<Product> getProducts() {
        Collection<Product> retorno = new ArrayList<>();
        try {
            retorno = productDao.findAll();
        } catch (Exception ex) {
            Logger.getLogger(TiendaEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    //Recoger productosp por categoría especifica
    @Override
    public Collection<Product> getProductsByCategory(String category) {
        Collection<Product> lista = new ArrayList<>();
        try {
            lista = productDao.findAll();
        } catch (Exception ex) {
            Logger.getLogger(TiendaEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Aqui los filtro
        Collection<Product> filtrada = new ArrayList<>();
        for (Product p : lista) {
            if (p.getCategory().equals(category)) {
                filtrada.add(p);
            }
        }
        return filtrada;
    }

    //Recoger productos por descripción especifica
    public Collection<Product> getProductsByDescription(String description) {
        Collection<Product> lista = new ArrayList<>();
        try {
            lista = productDao.findAll();
        } catch (Exception ex) {
            Logger.getLogger(TiendaEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Aqui los filtro
        Collection<Product> filtrada = new ArrayList<>();
        for (Product p : lista) {
            if (p.getDescription().equals(description)) {
                filtrada.add(p);
            }
        }
        return filtrada;
    }

    //Recoger por nombre
    @Override
    public Collection<Product> getByName(String name) {
        Collection<Product> retorno = new ArrayList<>();
        try {
            retorno = productDao.findByName(name);
        } catch (Exception ex) {
            Logger.getLogger(TiendaEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    //recoger por id
    @Override
    public Optional<Product> getById(Long id) {
        Optional<Product> retorno = Optional.empty();
        try {
            retorno = productDao.findById(id);
        } catch (Exception ex) {
            Logger.getLogger(TiendaEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    //insertar
    @Override
    public void insert(Product product) {
        try {
            productDao.insert(product);
        } catch (Exception ex) {
            Logger.getLogger(TiendaEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //borrar con id
    @Override
    public int deleteId(Long id) {
        int retorno = 0;
        try {
            retorno = productDao.delete(id);
        } catch (Exception ex) {
            Logger.getLogger(TiendaEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    //actualizar
    @Override
    public int update(Product product) {
        int retorno = 0;
        try {
            retorno = productDao.update(product);
        } catch (Exception ex) {
            Logger.getLogger(TiendaEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

}
