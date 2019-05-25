package by.radchuk.task.filter;

import by.radchuk.task.controller.context.ControllerContext;
import by.radchuk.task.controller.filter.RequestFilter;
import by.radchuk.task.controller.annotation.WebRequestFilter;
import lombok.var;

import javax.servlet.http.HttpServletRequest;

@WebRequestFilter(url = {"/check"})
public class RequestFilterExample implements RequestFilter {
    @Override
    public void filter(HttpServletRequest request) {
        System.out.println(1);
    }

}
