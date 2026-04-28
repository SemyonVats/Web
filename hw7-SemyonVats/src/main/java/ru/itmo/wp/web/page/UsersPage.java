package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.UserService;
import ru.itmo.wp.web.annotation.Json;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class UsersPage {
    private final UserService userService = new UserService();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        User currentUser = (User) request.getSession().getAttribute("user");

        String csrfToken = (String) request.getSession().getAttribute("_csrf");
        if (csrfToken == null) {
            csrfToken = java.util.UUID.randomUUID().toString();
            request.getSession().setAttribute("_csrf", csrfToken);
        }
        view.put("csrfToken", csrfToken);
        view.put("users", userService.findAll());

        if (currentUser != null) {
            view.put("user", currentUser);
        }
    }

    @Json
    private void toggleAdmin(HttpServletRequest request, Map<String, Object> view) {
        try {
            User currentUser = (User) request.getSession().getAttribute("user");
            if (currentUser == null || !currentUser.isAdmin()) {
                throw new ValidationException("Only administrators can change admin status");
            }

            String sessionToken = (String) request.getSession().getAttribute("_csrf");
            String requestToken = request.getParameter("_csrf");
            if (sessionToken == null || !sessionToken.equals(requestToken)) {
                throw new ValidationException("Invalid CSRF token");
            }

            long userId = Long.parseLong(request.getParameter("userId"));
            boolean admin = Boolean.parseBoolean(request.getParameter("admin"));

            if (userId == currentUser.getId()) {
                throw new ValidationException("You cannot change your own admin status");
            }

            userService.setAdmin(userId, admin, currentUser.getId());
            view.put("success", true);
            view.put("admin", admin);
        } catch (Exception e) {
            view.put("success", false);
            view.put("error", e.getMessage());
        }
    }
}