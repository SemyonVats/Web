package ru.itmo.web.hw4.web;

import freemarker.template.*;
import ru.itmo.web.hw4.model.User;
import ru.itmo.web.hw4.util.DataUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreemarkerServlet extends HttpServlet {
    private static final String UTF_8 = StandardCharsets.UTF_8.name();
    private static final String DEBUG_TEMPLATES_PATH = "../../src/main/webapp/WEB-INF/templates";
    private static final String TEMPLATES_PATH = "/WEB-INF/templates";

    private final Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_31);

    @Override
    public void init() throws ServletException {
        File dir = new File(getServletContext().getRealPath("."), DEBUG_TEMPLATES_PATH);
        if (!dir.exists() || !dir.isDirectory()) {
            dir = new File(getServletContext().getRealPath(TEMPLATES_PATH));
        }

        try {
            freemarkerConfiguration.setDirectoryForTemplateLoading(dir);
        } catch (IOException e) {
            throw new ServletException("Unable to set template directory [dir=" + dir + "].", e);
        }

        freemarkerConfiguration.setDefaultEncoding(UTF_8);
        freemarkerConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        freemarkerConfiguration.setLogTemplateExceptions(false);
        freemarkerConfiguration.setWrapUncheckedExceptions(true);
        freemarkerConfiguration.setFallbackOnNullLoopVariable(false);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding(UTF_8);
        response.setCharacterEncoding(UTF_8);

        String requestURI = request.getRequestURI();
        if ("/".equals(requestURI)) {
            response.sendRedirect("/index");
            return;
        }

        Template template;
        try {
            template = freemarkerConfiguration.getTemplate(URLDecoder.decode(request.getRequestURI(), UTF_8) + ".ftlh");
        } catch (TemplateNotFoundException error) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            Map<String, Object> data = new HashMap<>();
            data.put("currentMenuItem", null);
            DataUtil.addData(request, data);
            try {
                template = freemarkerConfiguration.getTemplate("404.ftlh");
            } catch (TemplateNotFoundException ignored) {
                response.setContentType("text/html");
                response.getWriter().write("<h1>404 Not Found</h1><p>No such page.</p>");
                return;
            }
        }

        Map<String, Object> data = getData(request);

        response.setContentType("text/html");
        try {
            template.process(data, response.getWriter());
        } catch (TemplateException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private Map<String, Object> getData(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();

        for (Map.Entry<String, String[]> e : request.getParameterMap().entrySet()) {
            if (e.getValue() != null && e.getValue().length == 1) {
                data.put(e.getKey(), e.getValue()[0]);
            }
        }

        String currentMenuItem = "Jackpot!!!";
        String requestURI = request.getRequestURI();
        requestURI = requestURI.replaceAll("/+", "/");

        if (requestURI.endsWith("/help")) {
            currentMenuItem = "help";
        } else if (requestURI.endsWith("/index") || requestURI.endsWith("/")) {
            currentMenuItem = "index";
        } else if (requestURI.endsWith("/contests")) {
            currentMenuItem = "contests";
        } else if (requestURI.endsWith("/users")) {
            currentMenuItem = "users";
        }
        data.put("currentMenuItem", currentMenuItem);


        DataUtil.addData(request, data);

        @SuppressWarnings("unchecked")
        List<User> users = (List<User>) data.get("users");

        if (users != null && !users.isEmpty()) {
            String handle = request.getParameter("handle");
            String userIdStr = request.getParameter("user_id");

            int currentIndex = findUserIndex(users, handle, userIdStr);

            User currentUser = (currentIndex != -1) ? users.get(currentIndex) : null;
            User prevUser = (currentIndex > 0) ? users.get(currentIndex - 1) : null;
            User nextUser = (currentIndex < users.size() - 1) ? users.get(currentIndex + 1) : null;

            data.put("currentUser", currentUser);
            data.put("prevUser", prevUser);
            data.put("nextUser", nextUser);
        } else {
            data.put("currentUser", null);
            data.put("prevUser", null);
            data.put("nextUser", null);
        }

        return data;
    }

    private int findUserIndex(List<User> users, String handle, String userIdStr) {
        if (handle != null) {
            return findUserIndexByHandle(users, handle);
        } else if (userIdStr != null) {
            return findUserIndexById(users, userIdStr);
        }
        return -1;
    }

    private int findUserIndexByHandle(List<User> users, String handle) {
        for (int i = 0; i < users.size(); i++) {
            if (handle.equals(users.get(i).getHandle())) {
                return i;
            }
        }
        return -1;
    }

    private int findUserIndexById(List<User> users, String userIdStr) {
        try {
            long userId = Long.parseLong(userIdStr);
            for (int i = 0; i < users.size(); i++) {
                if (userId == users.get(i).getId()) {
                    return i;
                }
            }
        } catch (NumberFormatException ignored) {

        }
        return -1;
    }
}