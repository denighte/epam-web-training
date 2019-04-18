package by.radchuk.task.service;

import by.radchuk.task.controller.ServiceTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RestServiceTaskWrapper implements ServiceTask {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}
