package by.radchuk.task.controller.filter;

import by.radchuk.task.controller.context.ControllerContext;

public interface AbstractFilter {
    default void init(ControllerContext context) {};
}
