package by.radchuk.task.controller;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebServlet(urlPatterns = {"/login"}, asyncSupported = true)
public class FrontControllerServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
    }

    protected void processRequest(HttpServletRequest request,
                                  HttpServletResponse response)
                                throws ServletException, IOException {
        AsyncContext context = request.startAsync();
        context.addListener(...);
        WebCommand command = map.getCommand(request.getRequestURI());
        command.addListener(...);
        command.addListener(writeListner)
        switch(command.getType()) {
            case LITE:
                context.start(command);
                break;
            case HEAVY:
                service.execute(command);
                break;
        }
        // set up async listener
        //context.addListener();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}
