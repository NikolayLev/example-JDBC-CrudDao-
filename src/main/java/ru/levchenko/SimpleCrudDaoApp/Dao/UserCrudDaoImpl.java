package ru.levchenko.SimpleCrudDaoApp.Dao;

import ru.levchenko.SimpleCrudDaoApp.models.User;
import ru.levchenko.SimpleCrudDaoApp.models.UserRoles;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public final class UserCrudDaoImpl implements UserCrudDao {

    private String SQL_SELECT_ALL;
    private String SQL_SELECT_BY_ID;
    private String SQL_INSERT_USER;
    private String SQL_INSERT_ROLE;
    private String SQL_DELETE_USER;
    private String SQL_UPDATE_USER;
    private String SQL_SELECT_BY_ROLE;

    private void initializeSQLCommands(Properties properties) {
        SQL_SELECT_ALL = properties.getProperty("SQL_SELECT_ALL");
        SQL_SELECT_BY_ID = properties.getProperty("SQL_SELECT_BY_ID");
        SQL_INSERT_USER = properties.getProperty("SQL_INSERT_USER");
        SQL_INSERT_ROLE = properties.getProperty("SQL_INSERT_ROLE");
        SQL_DELETE_USER = properties.getProperty("SQL_DELETE_USER");
        SQL_UPDATE_USER = properties.getProperty("SQL_UPDATE_USER");
        SQL_SELECT_BY_ROLE = properties.getProperty("SQL_SELECT_BY_ROLE");
    }

    Connection connection;

    public UserCrudDaoImpl(Connection connection) throws IOException {
        this.connection = connection;
        Properties properties = new Properties();
        properties.load(new FileInputStream(
                "C:\\JAVA\\SimpleCrudDaoApp\\src\\main\\resources\\SQL_commands.properties"));
        initializeSQLCommands(properties);
    }

    public UserCrudDaoImpl(Connection connection, Properties properties) {
        this.connection = connection;
        initializeSQLCommands(properties);
    }


    @Override
    public List<UserRoles> roles(Long id) {
        return find(id).orElse(new User()).getRoles();
    }

    @Override
    public List<User> findUsersByRole(String role) {
        PreparedStatement preparedStatement = null;
        Map<Long, User> userMap = new HashMap();
        try {
            preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ROLE);
            preparedStatement.setString(1, role);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                User user;
                Long id = resultSet.getLong("id");
                if (userMap.get(id) == null) {
                    user = new User();
                    user.setId(id);
                    user.setName(resultSet.getString("name"));
                    user.setPassword(resultSet.getString("password"));
                    userMap.put(id, user);
                } else {
                    user = userMap.get(id);
                }
                user.getRoles().add(UserRoles.getRole(resultSet.getString("role")));

            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {

            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new IllegalArgumentException("Не закрыт preparedStatement");
            }
        }

        return new ArrayList<>(userMap.values());
    }

    @Override
    public Optional<User> find(Long id) {
        PreparedStatement preparedStatement = null;
        User user = new User();
        try {
            preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (user.getId() == null) {
                    user.setId(id);
                    user.setName(resultSet.getString("name"));
                    user.setPassword(resultSet.getString("password"));
                }

                user.getRoles().add(UserRoles.getRole(resultSet.getString("role")));
            }

            if (user.getId() != null) {
                return Optional.of(user);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {

            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new IllegalArgumentException("Не закрыт preparedStatement");
            }
        }

        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        PreparedStatement preparedStatement = null;
        Map<Long, User> userMap = new HashMap();

        try {
            preparedStatement = connection.prepareStatement(SQL_SELECT_ALL);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user;
                Long id = resultSet.getLong("id");
                if (userMap.get(id) == null) {
                    user = new User();
                    user.setId(id);
                    user.setName(resultSet.getString("name"));
                    user.setPassword(resultSet.getString("password"));
                    userMap.put(id, user);
                } else {
                    user = userMap.get(id);
                }
                user.getRoles().add(UserRoles.getRole(resultSet.getString("role")));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {

            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new IllegalArgumentException("Не закрыт preparedStatement");
            }
        }

        return new ArrayList<>(userMap.values());
    }

    @Override
    public void delete(Long id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_DELETE_USER);
            preparedStatement.setLong(1, id);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new IllegalArgumentException("Не закрыт preparedStatement");
            }
        }
    }

    @Override
    public void save(User user) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_INSERT_USER);

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                updateRoles(id, user.getRoles());
            }


        } catch (SQLException e) {
            e.printStackTrace();

        } finally {

            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new IllegalArgumentException("Не закрыт preparedStatement");
            }
        }


    }

    private void updateRoles(Long id, List<UserRoles> rolesList) throws SQLException {
        PreparedStatement preparedStatement = null;
        if (rolesList.isEmpty()) {
            rolesList.add(UserRoles.USER);
        }
        for (UserRoles userRole : rolesList) {

            preparedStatement = connection.prepareStatement(SQL_INSERT_ROLE);
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, userRole.name());

            preparedStatement.execute();

        }

    }


    @Override
    public void update(User user) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_UPDATE_USER);

            preparedStatement.setLong(3, user.getId());
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.execute();

            updateRoles(user.getId(), user.getRoles());

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {

            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new IllegalArgumentException("Не закрыт preparedStatement");
            }
        }
    }
}
