package dao;

import jakarta.ejb.Local;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import modelo.Client;

@Local
public interface DaoInterfazClient extends Dao<Client> {

    Collection<Client> findByName(String name) throws SQLException;

    Optional<Client> findByMail(String mail) throws SQLException;

}
