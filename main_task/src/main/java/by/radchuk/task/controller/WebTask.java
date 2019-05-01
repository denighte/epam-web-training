package by.radchuk.task.controller;

import by.radchuk.task.util.StringView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface WebTask {
    StringView getPath();
    String getMethod();
    String getRequestContentType();
    Response execute(HttpServletRequest request,
                     HttpServletResponse response)
                        throws IOException, ServletException;
}
