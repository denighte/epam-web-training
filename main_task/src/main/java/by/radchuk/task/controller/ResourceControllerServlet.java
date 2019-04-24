package by.radchuk.task.controller;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
@Slf4j
@WebServlet(urlPatterns = {"/resourcesss/*"}, asyncSupported = true)
public class ResourceControllerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
                        throws ServletException, IOException {
        String resourcePath = request.getRequestURI();
        InputStream resource = getServletContext().getResourceAsStream(resourcePath);
        if (resource == null) {
            response.setStatus(404);
            return;
        }
        response.setContentType("image/jpg");
        ServletOutputStream output = response.getOutputStream();
        AsyncContext asyncContext = request.startAsync();
        //output.setWriteListener(new ResourceWriteListener(asyncContext, resource, output));
    }
}
