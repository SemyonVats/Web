package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.dto.UserDto;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class TalksPage extends Page {
    public void action(HttpServletRequest request, Map<String, Object> view) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            throw new RedirectException("/index");
        }


        List<UserDto> users = talkService.findAllUsersExcept(user.getId());
        view.put("users", users);


        List<Talk> talks = talkService.findAllTalksForUser(user.getId());
        view.put("talks", talks);
    }

    private void send(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            throw new RedirectException("/index");
        }

        long targetUserId;
        try {
            targetUserId = Long.parseLong(request.getParameter("targetUserId"));
        } catch (NumberFormatException e) {
            throw new ValidationException("Invalid recipient.");
        }

        String text = request.getParameter("text");
        talkService.sendMessage(user, targetUserId, text);

        setMessage("Message sent successfully!");
        throw new RedirectException("/talks");
    }
}
