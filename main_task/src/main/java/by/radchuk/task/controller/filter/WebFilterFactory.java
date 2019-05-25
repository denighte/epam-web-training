package by.radchuk.task.controller.filter;

import by.radchuk.task.controller.ControllerException;
import by.radchuk.task.controller.ControllerFactory;

public interface WebFilterFactory<T> extends ControllerFactory {
    public T create(Class<T> filterClass) throws ControllerException;
}
