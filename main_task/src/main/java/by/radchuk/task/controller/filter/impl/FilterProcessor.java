package by.radchuk.task.controller.filter.impl;

import by.radchuk.task.controller.ControllerException;
import by.radchuk.task.controller.filter.FilterInfo;
import by.radchuk.task.controller.annotation.WebRequestFilter;
import by.radchuk.task.controller.annotation.WebResponseFilter;
import lombok.var;

public class FilterProcessor {
    public FilterInfo getInfo(Class filter) throws ControllerException {
        for(var annotation : filter.getDeclaredAnnotations()) {
            if (annotation.annotationType().equals(WebRequestFilter.class)) {
                return processRequestFilter((WebRequestFilter)annotation);
            }
            if (annotation.annotationType().equals(WebResponseFilter.class)) {
                return processResponseFilter((WebResponseFilter)annotation);
            }
        }
        throw new ControllerException("No suitable annotation was found.");
    }

    private FilterInfo processRequestFilter(WebRequestFilter annotation) {
        return new FilterInfo(annotation.url(), annotation.priority());
    }

    private FilterInfo processResponseFilter(WebResponseFilter annotation) {
        return new FilterInfo(annotation.url(), annotation.priority());
    }
}
