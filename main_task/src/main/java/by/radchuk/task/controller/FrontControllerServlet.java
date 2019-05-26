package by.radchuk.task.controller;
import by.radchuk.task.controller.exception.MethodNotSupportedException;
import by.radchuk.task.controller.exception.NotSupportedException;
import by.radchuk.task.controller.exception.WebClientException;
import by.radchuk.task.controller.filter.RequestFilter;
import by.radchuk.task.controller.filter.ResponseFilter;
import by.radchuk.task.controller.filter.WebFilterContainer;
import by.radchuk.task.controller.task.WebTask;
import by.radchuk.task.controller.task.WebTaskContainer;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Slf4j
@AllArgsConstructor
@MultipartConfig
public class FrontControllerServlet extends HttpServlet {
    private WebTaskContainer taskContainer;
    private WebFilterContainer<RequestFilter> requestFilterContainer;
    private WebFilterContainer<ResponseFilter> responseFilterContainer;


    public static final String SECURITY_FILTER_ATTRIBUTE_NAME = "security_filter";

    protected void processRequest(HttpServletRequest request,
                                  HttpServletResponse response)
                                throws ServletException, IOException {
        String method = (String) request.getAttribute("method");
        String uri = request.getRequestURI();
        ResponseHandler responseHandler = new ResponseHandler(request, response);

        try {
            WebTask task = taskContainer.getTask(uri, method);
            if (task == null) {
                throw new MethodNotSupportedException();
            }
            if (!validateContentType(task.getRequestContentType(), request.getContentType())) {
                throw new NotSupportedException();
            }
            Collection<RequestFilter> requestFilters = requestFilterContainer.getFilters(uri);
            for (var filter : requestFilters) {
                filter.filter(request);
            }

            Response result = task.execute(request, response);
            responseHandler.handle(result);

            Collection<ResponseFilter> responseFilters = responseFilterContainer.getFilters(uri);
            for (var filter : responseFilters) {
                filter.filter(request, response);
            }
        } catch (WebClientException exception) {
            responseHandler.handle(exception.getResponse());
        }
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("method", "HEAD");
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("method", "POST");
        processRequest(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("method", "PUT");
        processRequest(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("method", "DELETE");
        processRequest(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("method", "OPTIONS");
        processRequest(req, resp);
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("method", "TRACE");
        processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("method", "GET");
        processRequest(req, resp);
    }



    private boolean validateContentType(String expected, String actual) {
        if (expected != null && actual != null) {
            //avoiding 'boundary' parameter in multipart/form-data content type.
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

        void handle(Response response) throws IOException, ServletException {
            if (response == null) {
                return;
            }
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
