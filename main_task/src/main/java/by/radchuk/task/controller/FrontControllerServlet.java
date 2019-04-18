package by.radchuk.task.controller;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@WebServlet(urlPatterns = {"/login"}, asyncSupported = true)
public class FrontControllerServlet extends HttpServlet {
    private ExecutorService pool;

    @Override
    public void init() throws ServletException {
        pool = Executors.newCachedThreadPool();
    }

    protected void processRequest(HttpServletRequest request,
                                  HttpServletResponse response)
                                throws ServletException, IOException {
        //starting async
//        AsyncContext context = request.startAsync();
//
//        /**
//         * Singleton with ServiceTasks classes mapping;
//         * ServiceTask is a class which manages request/response objects.
//         * Note: There is {@link by.radchuk.task.service.RestServiceTaskWrapper} abstract class
//         * it is used to make Java Objects from application/json requests.
//         */
//        ActionMap map = ActionMap.getInstance();
//        //logging class, error handler
//        AsyncListener controllerListener = new ControllerListener();
//        //adding logging and error handler (or probably make 2 classes: logger and error handler?)
//        context.addListener(controllerListener);
//        //WebCommand is an interface, which holds special ServiceTask class.
//        //It used by FrontController and have methods to set up error/async listeners.
//        //WebCommand simply manages the request lifetime and ServiceTask class.
//        WebCommand command = map.getCommand(request.getRequestURI());
//        command.addListener(controllerListener);
//        //Here will be the logic of async request managing
//        switch(command.getType()) {
//            case FAST:
//                context.start(command);
//                break;
//            case HEAVY:
//                //<code>service</code> is a thread pool for heavy tasks
//                pool.execute(command);
//                break;
//            //Ajax event (or long polling requests)
//            case AJAX_EVENT:
//                ...
//                break;
//        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}
