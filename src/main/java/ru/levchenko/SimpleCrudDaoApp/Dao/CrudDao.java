package ru.levchenko.SimpleCrudDaoApp.Dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<T> {
    Optional<T> find(long id);

    List<T> findAll();

    void delete(long id);

    void save(T model);

    void update(T model);


}
