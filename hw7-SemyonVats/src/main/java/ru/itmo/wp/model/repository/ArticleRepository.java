package ru.itmo.wp.model.repository;

import ru.itmo.wp.model.domain.Article;

import java.util.List;

public interface ArticleRepository {
    void save(Article article);

    List<Article> findAll();

    List<Article> findByUserId(long userId);

    List<Article> findAllVisible();

    void setHidden(long articleId, boolean hidden);
}