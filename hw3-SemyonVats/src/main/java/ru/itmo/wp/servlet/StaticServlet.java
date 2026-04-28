package ru.itmo.wp.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/static/*")

public class StaticServlet extends HttpServlet {

    private File findFile(String uri, HttpServletRequest request) {

        if (!uri.startsWith("/")) {
            uri = "/" + uri;
        }

        String root = System.getProperty("user.dir");
        String prepath = request.getContextPath();
        File srcFile1 = new File(root + prepath + uri);
        if (srcFile1.isFile()) {
            return srcFile1;
        }

        File srcFile2 = new File(getServletContext().getRealPath("/static" + uri));
        if (srcFile2.isFile()) {
            return srcFile2;
        }
        return null;
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String uri = request.getRequestURI();

        List<File> allFiles = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for(int i = 1; i < uri.length(); i++){
            if(uri.charAt(i) == '+' && uri.charAt(i - 1) == '+'){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return ;
            }
        }
        for (int i = 0; i < uri.length(); i++) {
            if (uri.charAt(i) == '+') { // if '++' 400 bad request
                allFiles.add(findFile(builder.toString(), request));
                builder = new StringBuilder();
            } else {
                builder.append(uri.charAt(i));
                if (i + 1 == uri.length()) {
                    allFiles.add(findFile(builder.toString(), request) );
                }
            }
        }

        for (File allFile : allFiles) {
            if (allFile == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        }

        if(allFiles.isEmpty()){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return ;
        }

        response.setContentType(getServletContext().getMimeType(allFiles.get(0).getName())); // not checked size

        try (OutputStream outputStream = response.getOutputStream()) {
            for (File file : allFiles) {
                Files.copy(file.toPath(), outputStream);
            }
        }
    }
}
