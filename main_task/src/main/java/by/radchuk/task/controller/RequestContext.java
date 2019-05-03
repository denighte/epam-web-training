package by.radchuk.task.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequestContext {
    HttpServletRequest getRequest();
    HttpServletResponse getResponse();
    Object getSessionAttribute(String key);
    void setSessionAttribute(String key, Object value);
}
