package by.radchuk.task.controller.task;

import by.radchuk.task.controller.ControllerException;

import java.util.Collection;

public interface WebTaskContainer {
    void addTask(WebTask task) throws ControllerException;
    Collection<WebTask> tasks();
    WebTask getTask(String requestUri, String method);
}
