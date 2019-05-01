package by.radchuk.task.controller;

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
public class WebTaskContainer {
    private WebTaskFactory factory = new WebTaskFactory();
    private static final int METHODS_NUMBER = HttpMethodType.values().length;
    private Map<StringView, WebTask[]> handlerMap;

    WebTaskContainer() {
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

    public WebTask getTask(String requestUri, String method) {
        try {
            StringView uri = new StringView(requestUri);
            WebTask task = getExactTask(uri, method);
            if (task != null) {
                return task;
            }

            int uriDepth = uri.count('/');
            for(int i = 0; i < uriDepth; ++i) {
                uri.setEnd(uri.lastIndexOf('/'));
                uri.add("/*");
                task = getExactTask(uri, method);
                if (task != null) {
                    return task;
                }
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
        WebTask[] handlers = handlerMap.get(task.getPath());
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
            handlerMap.put(task.getPath(), handlers);
        } else if (handlers[methodOrdinal] == null) {
            handlers[methodOrdinal] = task;
        } else {
            throw new ControllerException("Duplicate handler with url="
                                               + task.getPath().toString()
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
        WebTaskContainer map = new WebTaskContainer();
        map.scan("by.radchuk.task.service");
        System.out.println(map.getTask("/test/test/test/id", "GET").getPath());
    }
}
