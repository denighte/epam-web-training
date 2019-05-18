package by.radchuk.task.controller;

import javax.servlet.http.HttpServletRequest;

public interface RequestFilter {
    void filter(HttpServletRequest request);
}
