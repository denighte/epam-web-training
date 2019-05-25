package by.radchuk.task.filter;

import by.radchuk.task.controller.filter.RequestFilter;
import by.radchuk.task.controller.annotation.WebRequestFilter;

import javax.servlet.http.HttpServletRequest;

@WebRequestFilter(url = {"/check"}, priority = 10)
public class RequestFilterExample2 implements RequestFilter {

    @Override
    public void filter(HttpServletRequest request) {
        System.out.println(2);
    }
}
