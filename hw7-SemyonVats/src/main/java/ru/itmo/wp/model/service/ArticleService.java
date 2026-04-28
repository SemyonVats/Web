package ru.itmo.wp.model.service;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.ArticleRepository;
import ru.itmo.wp.model.repository.impl.ArticleRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;

public class ArticleService {
    private final ArticleRepository articleRepository = new ArticleRepositoryImpl();

    public void validateArticle(String title, String text) throws ValidationException {
        if (title == null || title.isEmpty()) {
            throw new ValidationException("Title is required");
        }
        if (title.length() < 3) {
            throw new ValidationException("Title is too short");
        }
        if (title.length() > 255) {
            throw new ValidationException("Title is too long");
        }

        if (text == null || text.isEmpty()) {
            throw new ValidationException("Text is required");
        }
        if (text.length() < 10) {
            throw new ValidationException("Text is too short");
        }
    }

    public void create(long userId, String title, String text) throws ValidationException {
        validateArticle(title, text);

        Article article = new Article();
        article.setUserId(userId);
        article.setTitle(title);
        article.setText(text);
        article.setCreationTime(LocalDateTime.now());
        article.setHidden(false);

        articleRepository.save(article);
    }

    public List<Article> findAllVisible() {
        return articleRepository.findAllVisible();
    }

    public List<Article> findByUserId(long userId) {
        return articleRepository.findByUserId(userId);
    }

    public void setHidden(long articleId, long userId, boolean hidden) throws ValidationException {

        boolean hasPermission = false;
        for (Article article : articleRepository.findByUserId(userId)) {
            if (article.getId() == articleId) {
                hasPermission = true;
                break;
            }
        }

        if (!hasPermission) {
            throw new ValidationException("You don't have permission to modify this article");
        }

        articleRepository.setHidden(articleId, hidden);
    }
}