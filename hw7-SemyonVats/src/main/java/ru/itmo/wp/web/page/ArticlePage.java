package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.web.annotation.Json;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ArticlePage {
    private final ArticleService articleService = new ArticleService();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        String csrfToken = (String) request.getSession().getAttribute("_csrf");
        if (csrfToken == null) {
            csrfToken = java.util.UUID.randomUUID().toString();
            request.getSession().setAttribute("_csrf", csrfToken);
        }
        view.put("csrfToken", csrfToken);
    }

    @Json
    private void createJson(HttpServletRequest request, Map<String, Object> view) {
        try {
            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                throw new ValidationException("You must be logged in to create articles");
            }

            String sessionToken = (String) request.getSession().getAttribute("_csrf");
            String requestToken = request.getParameter("_csrf");
            if (sessionToken == null || !sessionToken.equals(requestToken)) {
                throw new ValidationException("Invalid CSRF token");
            }

            String title = request.getParameter("title");
            String text = request.getParameter("text");

            articleService.create(user.getId(), title, text);

            view.put("success", true);
            view.put("message", "Article created successfully");
            view.put("redirect", "/index");
        } catch (ValidationException e) {
            view.put("success", false);
            view.put("error", e.getMessage());
        } catch (Exception e) {
            view.put("success", false);
            view.put("error", "Failed to create article: " + e.getMessage());
        }
    }
}