package by.radchuk.task.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

@AllArgsConstructor
public class RequestContext {
    @Getter
    public HttpServletRequest request;
    @Getter
    public HttpServletResponse response;

    public Object getSessionAttribute(String key) {
        return request.getSession().getAttribute(key);
    }

    public void setSessionAttribute(String key, Object value) {
        request.getSession().setAttribute(key, value);
    }
}
