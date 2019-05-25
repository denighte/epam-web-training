package by.radchuk.task.controller.task;

import by.radchuk.task.controller.ControllerException;
import by.radchuk.task.controller.ControllerFactory;

import java.util.Collection;

public interface WebTaskFactory extends ControllerFactory {
    Collection<WebTask> create(Class cls) throws ControllerException;
}
