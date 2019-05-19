package by.radchuk.task.controller;

import java.util.Collection;

public interface WebTaskContainer {
    void addTask(WebTask task) throws ControllerException;
    Collection<WebTask> tasks();
    WebTask getTask(String requestUri, String method);
}
