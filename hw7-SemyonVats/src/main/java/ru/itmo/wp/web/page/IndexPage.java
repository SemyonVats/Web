package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.model.service.UserService;
import ru.itmo.wp.web.annotation.Json;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexPage {
    private void action(HttpServletRequest request, Map<String, Object> view) {}

    @Json
    private void articlesJson(HttpServletRequest request, Map<String, Object> view) {
        ArticleService articleService = new ArticleService();
        UserService userService = new UserService();

        List<Map<String, Object>> articlesData = new ArrayList<>();

        for (Article article : articleService.findAllVisible()) {
            User author = userService.find(article.getUserId());

            Map<String, Object> articleData = getStringObjectMap(article, author);

            articlesData.add(articleData);
        }

        view.put("articles", articlesData);
    }

    private static Map<String, Object> getStringObjectMap(Article article, User author) {
        Map<String, Object> articleData = new HashMap<>();
        articleData.put("id", article.getId());
        articleData.put("title", article.getTitle());
        articleData.put("text", article.getText());
        articleData.put("creationTime", article.getCreationTime().toString());

        Map<String, Object> authorData = new HashMap<>();
        authorData.put("id", author.getId());
        authorData.put("login", author.getLogin());
        articleData.put("author", authorData);
        return articleData;
    }
}