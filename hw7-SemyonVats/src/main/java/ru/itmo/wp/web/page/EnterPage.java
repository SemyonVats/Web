package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.UserService;
import ru.itmo.wp.web.annotation.Json;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class EnterPage {
    private final UserService userService = new UserService();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        String csrfToken = (String) request.getSession().getAttribute("_csrf");
        if (csrfToken == null) {
            csrfToken = java.util.UUID.randomUUID().toString();
            request.getSession().setAttribute("_csrf", csrfToken);
        }
        view.put("csrfToken", csrfToken);
    }

    private void enter(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        User user = userService.validateAndFindByLoginAndPassword(login, password);
        request.getSession().setAttribute("user", user);
        request.getSession().setAttribute("message", "Hello, " + user.getLogin());

        throw new RedirectException("/index");
    }

    @Json
    private void loginJson(HttpServletRequest request, Map<String, Object> view) {
        try {
            String sessionToken = (String) request.getSession().getAttribute("_csrf");
            String requestToken = request.getParameter("_csrf");
            if (sessionToken == null || !sessionToken.equals(requestToken)) {
                throw new ValidationException("Invalid CSRF token");
            }

            String login = request.getParameter("login");
            String password = request.getParameter("password");

            User user = userService.validateAndFindByLoginAndPassword(login, password);
            request.getSession().setAttribute("user", user);

            view.put("success", true);
            view.put("redirect", "/index");
        } catch (ValidationException e) {
            view.put("success", false);
            view.put("error", e.getMessage());
        }
    }
}