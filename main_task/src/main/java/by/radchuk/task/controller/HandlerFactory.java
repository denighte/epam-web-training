package by.radchuk.task.controller;

public class HandlerFactory {
    public HttpHandler createInstance(Class<? extends HttpHandler> cls) {
        try {
            return cls.newInstance();
        } catch (InstantiationException | IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }
}
