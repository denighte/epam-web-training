package by.radchuk.task.controller;

import by.radchuk.task.service.ServiceClass;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebServlet(urlPatterns = {"/test"}, asyncSupported = true, loadOnStartup = 1)
public class FrontControllerServlet extends HttpServlet {
    private WebServiceMap map;
    private HandlerFactory handlerFactory = new HandlerFactory();
    private boolean isScanned = false;

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

//        String method = (String)request.getAttribute("method");
//        System.out.println(request.getRequestURL());
//        Class<HttpHandler> cls = map.getHandler("/test", method);
//        HttpHandler handler = handlerFactory.createInstance(cls);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("method", "GET");
        processRequest(req, resp);
    }
}
