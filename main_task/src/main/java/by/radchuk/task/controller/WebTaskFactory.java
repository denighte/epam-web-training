package by.radchuk.task.controller;

import java.util.Collection;

public interface WebTaskFactory {
    Collection<WebTask> create(Class cls) throws ControllerException;
}
