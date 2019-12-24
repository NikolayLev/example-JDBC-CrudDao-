package ru.levchenko.SimpleCrudDaoApp.Dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<T> {
    Optional<T> find(Long id);

    List<T> findAll();

    void delete(Long id);

    void save(T model);

    void update(T model);


}
