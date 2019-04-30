package by.radchuk.task.controller;

import by.radchuk.task.controller.annotation.WebHandler;
import by.radchuk.task.util.ClassReflections;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.io.IOException;
import java.util.*;

/**
 * Service handler mapping class.
 * Contains pairs urlPattern --> ServiceTaskImpl
 *
 */
@Slf4j
public class WebTaskMap {
    private WebTaskFactory factory = new WebTaskFactory();
    private static final int METHODS_NUMBER = HttpMethodType.values().length;
    private Map<String, WebTask[]> handlerMap;

    WebTaskMap() {
        handlerMap = new HashMap<>();
    }

    public void scan(String clsPackage) throws ControllerException {
        try {
            List<Class> classes = ClassReflections.builder().load(clsPackage)
                                                  .filter(WebHandler.class).get();
            for (var cls : classes) {
                for (var task : factory.create(cls)) {
                    addHandler(task);
                }
            }
        } catch (IOException | ControllerException exception) {
            throw new ControllerException("Failed to scan the package!", exception);
        }
    }

    public WebTask getTask(String url, String method) {
        WebTask task;
        try {
            task = handlerMap.get(url)[HttpMethodType.valueOf(method).ordinal()];
        } catch (IllegalArgumentException exception) {
            log.debug("Unsupported method type: {}", method);
            throw new IllegalArgumentException("Unsupported method type.");
        }
        return task;
    }

    private void addHandler(WebTask task) {
        WebTask[] handlers = handlerMap.get(task.getURI());
        HttpMethodType httpMethod;
        try {
            httpMethod = HttpMethodType.valueOf(task.getMethod());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Unsupported method type.");
        }
        int methodOrdinal = httpMethod.ordinal();
        if (handlers == null) {
            handlers = new WebTask[METHODS_NUMBER];
            handlers[methodOrdinal] = task;
            handlerMap.put(task.getURI(), handlers);
        } else if (handlers[methodOrdinal] == null) {
            handlers[methodOrdinal] = task;
        } else {
            throw new IllegalArgumentException("Duplicate handler with url="
                                               + task.getURI()
                                               + " and HttpMethod="
                                               + httpMethod.name());
        }
    }

    private enum HttpMethodType {
        GET,
        HEAD,
        POST,
        PUT,
        DELETE,
    }


    public static void main(String[] args) throws ControllerException {
        WebTaskMap map = new WebTaskMap();
        map.scan("by.radchuk.task.service");
        System.out.println(map.getTask("/test", "GET").getURI());
    }
}
