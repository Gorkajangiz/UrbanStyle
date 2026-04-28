package dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public interface Dao<T> {

    Collection<T> findAll() throws SQLException;

    Optional<T> findById(Long id) throws SQLException;

    void insert(T entitys) throws SQLException;

    int update(T entity) throws SQLException;

//    int delete(T entity, DataSource ds) throws SQLException;
    int delete(Long id) throws SQLException;
}
