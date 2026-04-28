package ru.itmo.wp.web.page;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface OldPage {
    default void action(HttpServletRequest request, Map<String, Object> view) {
        view.put("name", "Van Darkholme");
    }
}
