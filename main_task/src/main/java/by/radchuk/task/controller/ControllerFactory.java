package by.radchuk.task.controller;

import by.radchuk.task.controller.context.ControllerContext;

public interface ControllerFactory {
    //default void setControllerContext(ControllerContext context) {}
    void setControllerContext(ControllerContext context);
}
