package by.radchuk.task.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ServiceTask {
    String execute(HttpServletRequest request,
                   HttpServletResponse response);
}
