package by.radchuk.task.controller;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class FrontControllerServlet extends HttpServlet {
    private WebContainer container;

    public FrontControllerServlet(WebContainer webContainer) {
        container = webContainer;
    }

    protected void processRequest(HttpServletRequest request,
                                  HttpServletResponse response)
                                throws ServletException, IOException {
        String method = (String) request.getAttribute("method");
        WebTask task = container.getTask(request.getRequestURI(), method);
        ResponseHandler responseHandler = new ResponseHandler(request, response);
        if (task == null || !validateContentType(task.getRequestContentType(),
                                 request.getContentType())) {
            Response msg = Response.builder()
                                   .error(415, "Not supported Media type.")
                                   .build();
            responseHandler.handle(msg);
            return;
        }
        Response result = task.execute(request, response);
        responseHandler.handle(result);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("method", "GET");
        processRequest(req, resp);
    }

    private boolean validateContentType(String expected, String actual) {
        if (expected != null && actual != null) {
            //avoiding boundary parameter in multipart/form-data content type.
            int propertyEnd = actual.indexOf(';');
            if (propertyEnd != -1) {
                actual = actual.substring(0, propertyEnd);
            }
            return actual.equals(expected);
        }
        return true;
    }

    @RequiredArgsConstructor
    private static class ResponseHandler {
        @NonNull private HttpServletRequest servletRequest;
        @NonNull private HttpServletResponse servletResponse;

        public void handle(Response response) throws IOException, ServletException {
            servletResponse.setStatus(response.getStatus());
            for (var header : response.getHeaders()) {
                servletResponse.addHeader(header.getKey(), header.getValue());
            }
            for (var cookie : response.getCookies()) {
                servletResponse.addCookie(cookie);
            }
            if (response.getType() != null) {
                servletResponse.setContentType(response.getType());
            }
            servletResponse.setCharacterEncoding(response.getEncoding());
            if (response.getData() != null) {
                servletResponse.setContentLength(response.getData().length());
                servletResponse.getWriter().write(response.getData());
            }

            if (response.getDispatchPath() != null) {
                servletRequest.getServletContext()
                              .getRequestDispatcher(response.getDispatchPath())
                              .forward(servletRequest, servletResponse);
                return;
            }
            if (response.getDispatcherName() != null) {
                servletRequest.getServletContext()
                              .getNamedDispatcher(response.getDispatcherName())
                              .forward(servletRequest, servletResponse);
                return;
            }
        }
    }
}
