package ru.levchenko.SimpleCrudDaoApp.models;

import java.util.HashMap;
import java.util.Map;

public enum UserRoles {
    USER, ADMIN, NEWS_MAKER, MODERATOR, BATMAN;

    private static final Map<String, UserRoles> lookup = new HashMap<>();

    static {
        for (UserRoles roles : UserRoles.values()) {
            lookup.put(roles.name(), roles);
        }
    }

    public static UserRoles getRole(String role) {
        return lookup.get(role);
    }


}
