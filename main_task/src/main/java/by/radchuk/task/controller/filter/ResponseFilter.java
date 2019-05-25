package by.radchuk.task.controller.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ResponseFilter extends AbstractFilter {
    void filter(HttpServletRequest request,
                HttpServletResponse response);

}
