package by.radchuk.task.controller.filter;

import javax.servlet.http.HttpServletRequest;

public interface RequestFilter extends AbstractFilter {
    void filter(HttpServletRequest request);
}
