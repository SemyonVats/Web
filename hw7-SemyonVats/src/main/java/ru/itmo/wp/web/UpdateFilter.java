package ru.itmo.wp.web;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.service.UserService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateFilter implements Filter {
    private final UserService userService = new UserService();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        User user = (User) httpRequest.getSession().getAttribute("user");

        if (user != null) {
            User updatedUser = userService.find(user.getId());
            if (updatedUser != null) {
                httpRequest.getSession().setAttribute("user", updatedUser);
            }
        }
        chain.doFilter(request, response);
    }
}