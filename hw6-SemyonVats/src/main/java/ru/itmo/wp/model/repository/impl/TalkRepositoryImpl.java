package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.database.DatabaseUtils;
import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.exception.RepositoryException;
import ru.itmo.wp.model.repository.TalkRepository;
import ru.itmo.wp.model.repository.dto.UserDto;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TalkRepositoryImpl implements TalkRepository {
    private final DataSource DATA_SOURCE = DatabaseUtils.getDataSource();

    @Override
    public void save(Talk talk) {
        try (Connection connection = DATA_SOURCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO Talk (sourceUserId, targetUserId, text, creationTime) VALUES (?, ?, ?, NOW())",
                     Statement.RETURN_GENERATED_KEYS
             )) {
            statement.setLong(1, talk.getSourceUserId());
            statement.setLong(2, talk.getTargetUserId());
            statement.setString(3, talk.getText());
            if (statement.executeUpdate() != 1) {
                throw new RepositoryException("Can't save Talk.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    talk.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't save Talk.", e);
        }
    }

    @Override
    public List<Talk> findAllByUserId(long userId) {
        List<Talk> talks = new ArrayList<>();
        try (Connection connection = DATA_SOURCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT t.*, " +
                             "       su.login AS sourceLogin, " +
                             "       tu.login AS targetLogin " +
                             "FROM Talk t " +
                             "JOIN User su ON t.sourceUserId = su.id " +
                             "JOIN User tu ON t.targetUserId = tu.id " +
                             "WHERE t.sourceUserId = ? OR t.targetUserId = ? " +
                             "ORDER BY t.creationTime DESC"
             )) {
            statement.setLong(1, userId);
            statement.setLong(2, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Talk talk = new Talk();
                    talk.setId(resultSet.getLong("id"));
                    talk.setSourceUserId(resultSet.getLong("sourceUserId"));
                    talk.setTargetUserId(resultSet.getLong("targetUserId"));
                    talk.setText(resultSet.getString("text"));
                    talk.setCreationTime(resultSet.getTimestamp("creationTime"));
                    talk.setSourceLogin(resultSet.getString("sourceLogin"));
                    talk.setTargetLogin(resultSet.getString("targetLogin"));
                    talks.add(talk);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find talks.", e);
        }
        return talks;
    }

    @Override
    public List<UserDto> findAllUsersExcept(long userId) {
        List<UserDto> users = new ArrayList<>();
        try (Connection connection = DATA_SOURCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT id, login FROM User WHERE id != ? ORDER BY login"
             )) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(new UserDto(
                            resultSet.getLong("id"),
                            resultSet.getString("login")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find users.", e);
        }
        return users;
    }
}