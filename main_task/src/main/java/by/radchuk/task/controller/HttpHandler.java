package by.radchuk.task.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HttpHandler {
    String execute(HttpServletRequest request,
                   HttpServletResponse response);
}
