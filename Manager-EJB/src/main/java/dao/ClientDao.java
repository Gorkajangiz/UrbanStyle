package dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import modelo.Client;

@Stateless
public class ClientDao implements DaoInterfazClient {

    @PersistenceContext(unitName = "TiendaPU")
    private EntityManager em;

    @Override
    public Collection<Client> findByName(String name) {
        TypedQuery<Client> q = em.createQuery(
                "SELECT c FROM Client c WHERE c.name = :name", Client.class);
        q.setParameter("name", name);
        return q.getResultList();
    }

    @Override
    public Optional<Client> findByMail(String mail) {
        TypedQuery<Client> q = em.createQuery(
                "SELECT c FROM Client c WHERE c.email = :mail", Client.class);
        q.setParameter("mail", mail);

        List<Client> res = q.getResultList();
        return res.isEmpty() ? Optional.empty() : Optional.of(res.get(0));
    }

    @Override
    public Optional<Client> findById(Long id) {
        return Optional.ofNullable(em.find(Client.class, id));
    }

    @Override
    public Collection<Client> findAll() {
        return em.createQuery("SELECT c FROM Client c", Client.class)
                .getResultList();
    }

    @Override
    public void insert(Client entity) {
        em.persist(entity);
    }

    @Override
    public int update(Client entity) {
        em.merge(entity);
        return 1;
    }

    @Override
    public int delete(Long id) {
        Client c = em.find(Client.class, id);
        if (c != null) {
            em.remove(c);
            return 1;
        }
        return 0;
    }
}
