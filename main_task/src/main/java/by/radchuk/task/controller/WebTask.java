package by.radchuk.task.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface WebTask {
    String getPath();
    String getMethod();
    String getRequestContentType();
    Response execute(HttpServletRequest request,
                     HttpServletResponse response);
}
