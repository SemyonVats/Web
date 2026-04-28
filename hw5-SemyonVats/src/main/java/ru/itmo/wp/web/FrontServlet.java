package ru.itmo.wp.web;

import freemarker.template.*;
import ru.itmo.wp.web.exception.NotFoundException;
import ru.itmo.wp.web.exception.RedirectException;
import ru.itmo.wp.web.page.IndexPage;
import ru.itmo.wp.web.page.NotFoundPage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FrontServlet extends HttpServlet {
    private static final String BASE_PACKAGE = FrontServlet.class.getPackage().getName() + ".page";
    private static final String DEFAULT_ACTION = "action";
    private static final String LANG = "lang";
    private static final String EN = "en";


    private Configuration sourceConfiguration;
    private Configuration targetConfiguration;


    private Configuration newFreemarkerConfiguration(String templateDirName, boolean debug) throws ServletException {
        File templateDir = new File(templateDirName);
        if (!templateDir.isDirectory()) {
            return null;
        }

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        try {
            configuration.setDirectoryForTemplateLoading(templateDir);
        } catch (IOException e) {
            throw new ServletException("Can't create freemarker configuration [templateDir=" + templateDir + "]");
        }
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        configuration.setTemplateExceptionHandler(debug ? TemplateExceptionHandler.HTML_DEBUG_HANDLER :
                TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);

        return configuration;
    }

    @Override
    public void init() throws ServletException {
        sourceConfiguration = newFreemarkerConfiguration(
                getServletContext().getRealPath("/") + "../../src/main/webapp/WEB-INF/templates", true);
        targetConfiguration = newFreemarkerConfiguration(
                getServletContext().getRealPath("WEB-INF/templates"), false);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String langParam = request.getParameter(LANG); // todo const
        if (langParam != null && langParam.matches("[a-z][a-z]")) {
            request.getSession().setAttribute(LANG, langParam);
        }


        Route route = Route.newRoute(request);
        try {
            process(route, request, response);
        } catch (NotFoundException e) {
            try {
                process(Route.newNotFoundRoute(), request, response);
            } catch (NotFoundException notFoundException) {
                throw new ServletException(notFoundException);
            }
        }
    }


    private Method checkMethod(Method[] methods, Route route) {
        for (Method m : methods) {
            if (m.getName().equals(route.getAction())) {
                Class<?>[] paramTypes = m.getParameterTypes();
                boolean valid = true;
                for (Class<?> p : paramTypes) {
                    if (p != HttpServletRequest.class && p != Map.class) { //retrieve
                        valid = false;
                        break;
                    }
                }
                if (valid) {
                    return m;
                }
            }
        }
        return null;
    }

    private Method findMethod(Class<?> clazz, Route route) {
        Method method = null;
        while (method == null && clazz != null) {
            method = checkMethod(clazz.getDeclaredMethods(), route);
            if (method == null) {
                for (Class<?> iface : clazz.getInterfaces()) {
                    method = checkMethod(iface.getMethods(), route);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return method;
    }

    private Template makeTemplateByLang(String lang, String baseTemplateName) throws ServletException{
        return newTemplate((lang.equals(EN)) ? baseTemplateName + ".ftlh" : baseTemplateName + "_" + lang + ".ftlh");
    }

    private void process(Route route, HttpServletRequest request, HttpServletResponse response) throws NotFoundException, ServletException, IOException {
        Class<?> pageClass;
        try {
            pageClass = Class.forName(route.getClassName());
        } catch (ClassNotFoundException e) {
            throw new NotFoundException();
        }


        Method method = findMethod(pageClass, route);
        if (method == null) {
            throw new NotFoundException();
        }


        Object page;
        try {
            page = pageClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new ServletException("Can't create page [pageClass=" + pageClass + "]");
        }

        Map<String, Object> view = new HashMap<>();
        method.setAccessible(true);


        Map<Class<?>, Object> context = Map.of(
                HttpServletRequest.class, request,
                Map.class, view
        );

        Class<?>[] paramTypes = method.getParameterTypes();
        Object[] args = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            args[i] = context.get(paramTypes[i]);
        }


        try {
            method.invoke(page, args);
        } catch (IllegalAccessException e) {
            throw new ServletException("Can't invoke action method [pageClass=" + pageClass + ", method=" + method + "]");
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RedirectException) {
                RedirectException redirectException = (RedirectException) cause;
                response.sendRedirect(redirectException.getTarget());
                return;
            } else {
                throw new ServletException("Can't invoke action method [pageClass=" + pageClass + ", method=" + method + "]", cause);
            }
        }


        String lang = (String) request.getSession().getAttribute(LANG);  // todo retrieve consts
        if (lang == null) {
            lang = EN; // TODO ENUM OR CONST
        }
        view.put(LANG, lang);
        Template template = makeTemplateByLang(lang, pageClass.getSimpleName());


        response.setContentType("text/html");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try {
            template.process(view, response.getWriter());
        } catch (TemplateException e) {
            if (sourceConfiguration == null) {
                throw new ServletException("Can't render template [pageClass=" + pageClass + ", action=" + method + "]", e);
            }
        }
    }


    private Template loadTemplate(Configuration configuration, String templateName) throws ServletException{
        if (configuration == null) {
            return null;
        }
        try {
            return configuration.getTemplate(templateName);
        } catch (TemplateNotFoundException ignored) {
            return null;
        } catch (IOException e) {
            throw new ServletException("Can't load template [templateName=" + templateName + "]", e);
        }
    }

    private Template newTemplate(String templateName) throws ServletException {
        Template template = loadTemplate(sourceConfiguration, templateName);
        if (template == null && targetConfiguration != null) {
            template = loadTemplate(targetConfiguration, templateName);
        }

        if (template == null) {
            throw new ServletException("Can't find template [templateName=" + templateName + "]");
        }

        return template;
    }

    private static class Route {
        private final String className;
        private final String action;

        private Route(String className, String action) {
            this.className = className;
            this.action = action;
        }

        private String getClassName() {
            return className;
        }

        private String getAction() {
            return action;
        }

        private static Route newNotFoundRoute() {
            return new Route(NotFoundPage.class.getName(), DEFAULT_ACTION);
        }

        private static Route newIndexRoute() {
            return new Route(IndexPage.class.getName(), DEFAULT_ACTION);
        }

        private static Route newRoute(HttpServletRequest request) {
            String uri = request.getRequestURI();

            List<String> classNameParts = Arrays.stream(uri.split("/")).filter(part -> !part.isEmpty()).collect(Collectors.toList());

            if (classNameParts.isEmpty()) {
                return newIndexRoute();
            }

            StringBuilder simpleClassName = new StringBuilder(classNameParts.get(classNameParts.size() - 1));
            int lastDotIndex = simpleClassName.lastIndexOf(".");
            simpleClassName.setCharAt(lastDotIndex + 1, Character.toUpperCase(simpleClassName.charAt(lastDotIndex + 1)));
            classNameParts.set(classNameParts.size() - 1, simpleClassName.toString());

            String className = BASE_PACKAGE + "." + String.join(".", classNameParts) + "Page";

            String action = request.getParameter("action");
            if (action == null || action.isEmpty()) {
                action = DEFAULT_ACTION;
            }

            return new Route(className, action);
        }
    }
}
