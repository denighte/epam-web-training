package by.radchuk.task.controller;

import java.util.Collection;

public interface WebTaskScanner {
    Collection<WebTask> scan(String clsPackage) throws ControllerException;
}
