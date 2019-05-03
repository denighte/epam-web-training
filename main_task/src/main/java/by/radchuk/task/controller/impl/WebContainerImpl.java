package by.radchuk.task.controller.impl;

import by.radchuk.task.controller.ControllerException;
import by.radchuk.task.controller.WebContainer;
import by.radchuk.task.controller.WebTask;
import by.radchuk.task.controller.WebTaskFactory;
import by.radchuk.task.controller.annotation.WebHandler;
import by.radchuk.task.util.ClassReflections;
import by.radchuk.task.util.StringView;
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
public class WebContainerImpl implements WebContainer {
    private WebTaskFactory factory = new WebTaskFactoryImpl();
    private static final int METHODS_NUMBER = HttpMethodType.values().length;
    private Map<StringView, WebTask[]> handlerMap;

    public WebContainerImpl() {
        handlerMap = new HashMap<>();
    }

    @Override
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

    @Override
    public Collection<WebTask> tasks() {
        List<WebTask> tasks = new ArrayList<>();
        for(var arr : handlerMap.values()) {
            for(var handler : arr) {
                if (handler != null) {
                    tasks.add(handler);
                }
            }
        }
        return tasks;
    }

    @Override
    public WebTask getTask(String requestUri, String method) {
        try {
            StringView uri = new StringView(requestUri);
            WebTask task = getExactTask(uri, method);
            if (task != null) {
                return task;
            }

            int uriDepth = uri.count('/');
            for(int i = 0; i < uriDepth; ++i) {
                uri.add("/*");
                task = getExactTask(uri, method);
                if (task != null) {
                    return task;
                }
                uri.setEnd(uri.lastIndexOf('/'));
                uri.setEnd(uri.lastIndexOf('/'));
            }
            return null;
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    private WebTask getExactTask(StringView uri, String method) {
        WebTask[] urlHandlers = handlerMap.get(uri);
        if (urlHandlers != null) {
            return urlHandlers[HttpMethodType.valueOf(method).ordinal()];
        }
        return null;
    }

    private void addHandler(WebTask task) throws ControllerException {
        StringView path = new StringView(task.getPath());
        WebTask[] handlers = handlerMap.get(path);
        HttpMethodType httpMethod;
        try {
            httpMethod = HttpMethodType.valueOf(task.getMethod());
        } catch (IllegalArgumentException exception) {
            throw new ControllerException("Unsupported method type.");
        }
        int methodOrdinal = httpMethod.ordinal();
        if (handlers == null) {
            handlers = new WebTask[METHODS_NUMBER];
            handlers[methodOrdinal] = task;
            handlerMap.put(path, handlers);
        } else if (handlers[methodOrdinal] == null) {
            handlers[methodOrdinal] = task;
        } else {
            throw new ControllerException("Duplicate handler with url="
                                               + task.getPath()
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
        WebContainerImpl map = new WebContainerImpl();
        map.scan("by.radchuk.task.service");
        System.out.println(map.getTask("/test/test/test/id", "GET").getPath());
    }
}
