/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.Collection;
import java.util.Optional;
import modelo.Product;

/**
 *
 * @author edria
 */
@Stateless
public class ProductDao implements DaoInterfazProduct {

    @PersistenceContext(unitName = "TiendaPU")
    private EntityManager em;

    @Override
    public Collection<Product> findAll() {
        return em.createQuery("SELECT p FROM Product p", Product.class)
                .getResultList();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(em.find(Product.class, id));
    }

    @Override
    public Collection<Product> findByName(String name) {
        TypedQuery<Product> q = em.createQuery(
                "SELECT p FROM Product p WHERE p.name = :name", Product.class);
        q.setParameter("name", name);
        return q.getResultList();
    }

    @Override
    public int getStock(Product p) {
        Product prod = em.find(Product.class, p.getId());
        return prod != null ? prod.getStock() : 0;
    }

    @Override
    public void insert(Product product) {
        em.persist(product);
    }

    @Override
    public int update(Product product) {
        em.merge(product);
        return 1;
    }

    @Override
    public int delete(Long id) {
        Product p = em.find(Product.class, id);
        if (p != null) {
            em.remove(p);
            return 1;
        }
        return 0;
    }

    @Override
    public void buy(Product p, Integer quantity) {
        Product prod = em.find(Product.class, p.getId());
        if (prod != null) {
            prod.setStock(prod.getStock() - quantity);
        }
    }
}
