package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.database.DatabaseUtils;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.RepositoryException;
import ru.itmo.wp.model.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class UserRepositoryImpl implements UserRepository {
    private final DataSource DATA_SOURCE = DatabaseUtils.getDataSource();

    @Override
    public User find(long id) {
        return findUser("SELECT * FROM `User` WHERE `id`=?", id);
    }

    @Override
    public User findByLogin(String login) {
        return findUser("SELECT * FROM `User` WHERE `login`=?", login);
    }

    @Override
    public User findByLoginAndPasswordSha(String login, String passwordSha) {
        return findUser("SELECT * FROM `User` WHERE `login`=? AND `passwordSha`=?", login, passwordSha);
    }

    private User findUser(String query, Object... params) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i]);
                }
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return toUser(resultSet);
                    }
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find User.", e);
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("SELECT * FROM `User` ORDER BY `id` DESC")) {
                    while (resultSet.next()) {
                        users.add(toUser(resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find Users.", e);
        }
        return users;
    }

    @Override
    public void save(User user, String passwordSha) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO `User` (`login`, `passwordSha`, `creationTime`) VALUES (?, ?, NOW())",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, user.getLogin());
                statement.setString(2, passwordSha);

                if (statement.executeUpdate() != 1) {
                    throw new RepositoryException("Can't save User.");
                }

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getLong(1));
                        user.setCreationTime(new Date());
                    } else {
                        throw new RepositoryException("Can't get id for User.");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't save User.", e);
        }
    }

    private User toUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setLogin(resultSet.getString("login"));

        try {
            user.setAdmin(resultSet.getBoolean("admin"));
        } catch (SQLException e) {
            user.setAdmin(false);
        }

        Timestamp timestamp = resultSet.getTimestamp("creationTime");
        if (timestamp != null) {
            user.setCreationTime(new Date(timestamp.getTime()));
        }
        return user;
    }

    @Override
    public List<User> findBatchByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }

        String placeholders = ids.stream().map(id -> "?").collect(Collectors.joining(","));
        String sql = "SELECT * FROM `User` WHERE `id` IN (" + placeholders + ")";

        List<User> users = new ArrayList<>();
        try (Connection connection = DATA_SOURCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (int i = 0; i < ids.size(); i++) {
                statement.setLong(i + 1, ids.get(i));
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(toUser(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find users by ids", e);
        }
        return users;
    }

    @Override
    public void setAdmin(long userId, boolean admin) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE `User` SET `admin` = ? WHERE `id` = ?"
            )) {
                statement.setBoolean(1, admin);
                statement.setLong(2, userId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't update admin status", e);
        }
    }
}