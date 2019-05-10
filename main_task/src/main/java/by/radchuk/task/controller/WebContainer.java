package by.radchuk.task.controller;

import java.util.Collection;

public interface WebContainer {
    void addTask(WebTask task) throws ControllerException;
    Collection<WebTask> tasks();
    WebTask getTask(String requestUri, String method);
}
