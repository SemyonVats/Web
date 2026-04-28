package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.web.annotation.Json;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class MyArticlesPage {
    private final ArticleService articleService = new ArticleService();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            throw new RedirectException("/enter");
        }

        view.put("articles", articleService.findByUserId(user.getId()));

        String csrfToken = (String) request.getSession().getAttribute("_csrf");
        if (csrfToken == null) {
            csrfToken = java.util.UUID.randomUUID().toString();
            request.getSession().setAttribute("_csrf", csrfToken);
        }
        view.put("csrfToken", csrfToken);
    }

    @Json
    private void toggleHidden(HttpServletRequest request, Map<String, Object> view) {
        try {
            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                throw new ValidationException("You must be authorized");
            }

            String sessionToken = (String) request.getSession().getAttribute("_csrf");
            String requestToken = request.getParameter("_csrf");
            if (sessionToken == null || !sessionToken.equals(requestToken)) {
                throw new ValidationException("Invalid CSRF token");
            }

            long articleId = Long.parseLong(request.getParameter("articleId"));
            boolean hidden = Boolean.parseBoolean(request.getParameter("hidden"));

            articleService.setHidden(articleId, user.getId(), hidden);

            view.put("success", true);
            view.put("hidden", hidden);
        } catch (ValidationException e) {
            view.put("success", false);
            view.put("error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            view.put("success", false);
            view.put("error", "Server error: " + e.getMessage());
        }
    }
}