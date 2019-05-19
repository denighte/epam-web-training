package by.radchuk.task.controller;

import javax.servlet.http.HttpServletRequest;

public interface RequestFilter extends AbstractFilter {
    void filter(HttpServletRequest request);
}
