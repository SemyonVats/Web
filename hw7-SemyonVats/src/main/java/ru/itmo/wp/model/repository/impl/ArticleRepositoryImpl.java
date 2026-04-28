package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.database.DatabaseUtils;
import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.exception.RepositoryException;
import ru.itmo.wp.model.repository.ArticleRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ArticleRepositoryImpl implements ArticleRepository {
    private final DataSource DATA_SOURCE = DatabaseUtils.getDataSource();

    @Override
    public void save(Article article) {
        if (article.getCreationTime() == null) {
            article.setCreationTime(LocalDateTime.now());
        }

        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO `Article` (`user_id`, `title`, `text`, `creation_time`, `hidden`) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setLong(1, article.getUserId());
                statement.setString(2, article.getTitle());
                statement.setString(3, article.getText());
                statement.setTimestamp(4, Timestamp.valueOf(article.getCreationTime()));
                statement.setBoolean(5, article.isHidden());

                int affectedRows = statement.executeUpdate();

                if (affectedRows != 1) {
                    throw new RepositoryException("Expected 1 row affected, got " + affectedRows);
                }

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        article.setId(generatedKeys.getLong(1));
                    } else {
                        throw new RepositoryException("No ID generated after save");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RepositoryException("Can't save Article: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Article> findAll() {
        return findAll("ORDER BY `creation_time` DESC");
    }

    @Override
    public List<Article> findByUserId(long userId) {
        return findAll("WHERE `user_id` = ? ORDER BY `creation_time` DESC", userId);
    }

    @Override
    public List<Article> findAllVisible() {
        return findAll("WHERE `hidden` = FALSE ORDER BY `creation_time` DESC");
    }

    @Override
    public void setHidden(long articleId, boolean hidden) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE `Article` SET `hidden` = ? WHERE `id` = ?"
            )) {
                statement.setBoolean(1, hidden);
                statement.setLong(2, articleId);

                int affectedRows = statement.executeUpdate();

                if (affectedRows != 1) {
                    throw new RepositoryException("Expected 1 row affected, got " + affectedRows);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RepositoryException("Can't update hidden status: " + e.getMessage(), e);
        }
    }

    private List<Article> findAll(String whereClause, Object... params) {
        List<Article> articles = new ArrayList<>();
        try (Connection connection = DATA_SOURCE.getConnection()) {
            String sql = "SELECT * FROM `Article` " + whereClause;

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i]);
                }

                try (ResultSet resultSet = statement.executeQuery()) {
                    ResultSetMetaData metaData = resultSet.getMetaData();

                    while (resultSet.next()) {
                        articles.add(toArticle(metaData, resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find Articles", e);
        }
        return articles;
    }

    private Article toArticle(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
        Article article = new Article();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            String columnName = metaData.getColumnName(i);
            switch (columnName) {
                case "id":
                    article.setId(resultSet.getLong(i));
                    break;
                case "user_id":
                    article.setUserId(resultSet.getLong(i));
                    break;
                case "title":
                    article.setTitle(resultSet.getString(i));
                    break;
                case "text":
                    article.setText(resultSet.getString(i));
                    break;
                case "creation_time":
                    Timestamp timestamp = resultSet.getTimestamp(i);
                    if (timestamp != null) {
                        article.setCreationTime(timestamp.toLocalDateTime());
                    }
                    break;
                case "hidden":
                    article.setHidden(resultSet.getBoolean(i));
                    break;
            }
        }
        return article;
    }
}