package ru.itmo.wp.servlet;

import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;


public class AnswerServlet extends HttpServlet {



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String prepath = request.getContextPath();
        final String path = (request.getRequestURI()).substring(prepath.length());

        String user = String.valueOf(StandardCharsets.UTF_8.encode(request.getParameter("user")));

        List<Message> sorted = new ArrayList<>(systemMessages.stream()
                .sorted(Comparator.comparing(Message::createdAt))
                .toList());

        if (path.equals("/message/auth")) {
            HttpSession session = request.getSession();
            if (user != null) {
                session.setAttribute("user", user); // todo utf
            } else {
                user = (String) session.getAttribute("user");
                if (user == null) {
                    user = "";
                }
            }
            sendJson(user, response);

        } else if (path.equals("/message/findAll")) {
            sendJson(sorted, response);

        } else if (path.equals("/message/add")) {
            String text = request.getParameter("text");
            HttpSession session = request.getSession();
            if (user == null || user.isEmpty()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            String our_user = (String) session.getAttribute("user"); // should send only for suthorized
            session.setAttribute("text", text);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            systemMessages.add(new Message(our_user, text, timeStamp));

        } else {
            response.setContentType("application/json");
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

    }

    private record Message(String user, String text, String createdAt) {}

    private final List<Message> systemMessages = new ArrayList<>(); // final

    private void sendJson(Object user, HttpServletResponse response) throws IOException {
        String json = new Gson().toJson(user); // todo retrieve common code
        response.setContentType("application/json");
        response.getWriter().print(json);
        response.getWriter().flush();
    }
}
