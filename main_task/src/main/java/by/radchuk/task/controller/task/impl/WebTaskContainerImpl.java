package by.radchuk.task.controller.task.impl;

import by.radchuk.task.controller.ControllerException;
import by.radchuk.task.controller.task.WebTaskContainer;
import by.radchuk.task.controller.task.WebTask;
import by.radchuk.task.util.StringView;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.util.*;

/**
 * Service handler mapping class.
 * Contains pairs urlPattern --> ServiceTaskImpl
 *
 */
@Slf4j
public class WebTaskContainerImpl implements WebTaskContainer {
    private static final int METHODS_NUMBER = HttpMethodType.values().length;
    private Map<StringView, WebTask[]> handlerMap;

    public WebTaskContainerImpl() {
        handlerMap = new HashMap<>();
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

    @Override
    public void addTask(WebTask task) throws ControllerException {
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

}
