package by.radchuk.task.controller;

import java.util.Collection;

public interface WebContainer {
    void scan(String clsPackage) throws ControllerException;
    Collection<WebTask> tasks();
    WebTask getTask(String requestUri, String method);
}
