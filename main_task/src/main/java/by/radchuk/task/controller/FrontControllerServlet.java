package by.radchuk.task.controller;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebServlet(urlPatterns = {"/test"}, loadOnStartup = 1)
public class FrontControllerServlet extends HttpServlet {
    private WebServiceMap map;

    @Override
    public void init() throws ServletException {
        map = new WebServiceMap();
        try {
            map.scan("by.radchuk.task.service");
        } catch (ScanException exception) {
            throw new ServletException(exception);
        }
    }

    protected void processRequest(HttpServletRequest request,
                                  HttpServletResponse response)
                                throws ServletException, IOException {

        String method = (String)request.getAttribute("method");
        WebServiceTask task = map.getTask(request.getRequestURI(), method);
        String taskContentType = task.getRequestContentType();
        if (taskContentType != null) {
            String requestContentType = request.getContentType();
            //avoiding boundary parameter in multipart/form-data content type.
            int propertyEnd = requestContentType.indexOf(';');
            if (propertyEnd != -1) {
                requestContentType = requestContentType.substring(0, propertyEnd);
            }
            if (!requestContentType.equals(taskContentType)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Unsupported request content type.");
                return;
            }
        }
        String result = task.execute(request, response);
        response.getWriter().write(result);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("method", "GET");
        processRequest(req, resp);
    }
}
