package ru.levchenko.SimpleCrudDaoApp.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private Long id;
    private String name;
    private String password;
    private List<UserRoles> roles = new ArrayList<>();

    public User(Long id, String name, String password, List<UserRoles> roles) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.roles = roles;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<UserRoles> getRoles() {
        return roles;
    }

    public void addRole(UserRoles userRole) {
        roles.add(userRole);
    }

    public void setRoles(List<UserRoles> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}
