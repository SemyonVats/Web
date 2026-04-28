package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.service.EventService;
import ru.itmo.wp.model.service.TalkService;
import ru.itmo.wp.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

public abstract class Page {
    protected HttpServletRequest request;

    protected final UserService userService = new UserService();
    protected final EventService eventService = new EventService();
    protected final TalkService talkService = new TalkService();

    public void before(HttpServletRequest request, Map<String, Object> view) {
        this.request = request;

        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            view.put("user", user);
        }

        view.put("userCount", userService.findUserCount());

        HttpSession session = request.getSession();
        if (session.getAttribute("message") != null) {
            view.put("message", session.getAttribute("message"));
            session.removeAttribute("message");
        }
    }

    public void after(HttpServletRequest request, Map<String, Object> view) {}

    protected void setMessage(String message) {
        request.getSession().setAttribute("message", message);
    }

    protected HttpSession getSession() {
        return request.getSession();
    }

    public void action(HttpServletRequest request, Map<String, Object> view) {}
}