package ru.levchenko.SimpleCrudDaoApp.Dao;

import ru.levchenko.SimpleCrudDaoApp.models.User;
import ru.levchenko.SimpleCrudDaoApp.models.UserRoles;

import java.util.List;

public interface UserCrudDao extends CrudDao<User> {
    List<UserRoles> roles(Long id);

    List<User> findUsersByRole(String role);
}
