package ru.levchenko.SimpleCrudDaoApp;

import ru.levchenko.SimpleCrudDaoApp.Dao.UserCrudDaoImpl;
import ru.levchenko.SimpleCrudDaoApp.models.User;
import ru.levchenko.SimpleCrudDaoApp.models.UserRoles;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class App {
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("C:\\JAVA\\SimpleCrudDaoApp\\src\\main\\resources\\JDBC.properties"));

        Class.forName(properties.getProperty("JDBC.driverName"));

        Connection connection = DriverManager.getConnection(properties.getProperty("JDBC.url"),
                properties.getProperty("JDBC.username"), properties.getProperty("JDBC.password"));
        UserCrudDaoImpl userCrudDao = new UserCrudDaoImpl(connection);

        System.out.println(userCrudDao.find(4).get());
        System.out.println(userCrudDao.findAll());
        User user = userCrudDao.find(11).get();
        user.setRoles(new ArrayList<UserRoles>());
        user.getRoles().add(UserRoles.BATMAN);
        User user2 = new User();
        user2.setName("asdasdasd");
        user2.setPassword("asd2425");
        user2.addRole(UserRoles.USER);
        user2.addRole(UserRoles.NEWS_MAKER);
        userCrudDao.save(user2);
        userCrudDao.update(user);
        userCrudDao.delete(5);
        System.out.println(userCrudDao.roles(11));
        System.out.println(userCrudDao.findUsersByRole("USER"));
    }
}
