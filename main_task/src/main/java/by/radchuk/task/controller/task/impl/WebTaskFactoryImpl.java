package by.radchuk.task.controller.task.impl;

import by.radchuk.task.controller.context.ControllerContext;
import by.radchuk.task.controller.*;
import by.radchuk.task.controller.annotation.*;
import by.radchuk.task.controller.task.WebTask;
import by.radchuk.task.controller.task.WebTaskFactory;
import by.radchuk.task.util.MethodReflections;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WebTaskFactoryImpl implements WebTaskFactory {
    private ControllerContext context;
    @Override
    public Collection<WebTask> create(Class cls) throws ControllerException {
        Object object;
        String classPath = "";
        byte securityLevel = 0;
        if (cls.isAnnotationPresent(Path.class)) {
            classPath = ((Path) cls.getAnnotation(Path.class)).value();
        }
        if (cls.isAnnotationPresent(Secure.class)) {
            securityLevel = ((Secure) cls.getAnnotation(Secure.class)).value();
        }
        try {
            object = cls.newInstance();
        } catch (InstantiationException | IllegalAccessException exception) {
            throw new ControllerException("Class instantiation/access exception.", exception);
        }

        List<WebTask> tasks = new ArrayList<>();

        for (Method method : MethodReflections.builder().load(cls)
                                             .filter(Path.class).setAccessible().get()) {
            WebTaskBuilder builder = new WebTaskBuilder();
            builder.handler(object, method);
            builder.controllerContext(context);

            String handlerPath = method.getAnnotation(Path.class).value();
            String resultPath;

            if (classPath.equals("") && handlerPath.equals("")) {
                throw new ControllerException("Invalid annotations: "
                        + "both method and class don't have valid 'Path'"
                        + " annotation.");
            } else if (classPath.equals("")) {
                resultPath = handlerPath;
            } else if (handlerPath.equals("")) {
                resultPath = classPath;
            } else {
                resultPath = classPath + handlerPath;
            }
            builder.path(resultPath);

            if (method.isAnnotationPresent(HttpMethod.class)) {
                builder.method(method.getAnnotation(HttpMethod.class).value());
            } else {
                throw new ControllerException("Invalid declaration: "
                        + "Method not annotated with 'HttpMethod' class.");
            }

            if (method.isAnnotationPresent(Consume.class)) {
                builder.requestContentType(method.getAnnotation(Consume.class).value().getType());
            }

            if (method.isAnnotationPresent(Produce.class)) {
                builder.responseContentType(method.getAnnotation(Produce.class).value().getType());
            }

            if (method.isAnnotationPresent(Secure.class)) {
                builder.securityLevel(method.getAnnotation(Secure.class).value());
            } else {
                builder.securityLevel(securityLevel);
            }

            builder.arguments(method.getParameters());

            tasks.add(builder.build());

        }
        return tasks;
    }

    @Override
    public void setControllerContext(ControllerContext context) {
        this.context = context;
    }

    // Inner classes -----------------------------------------------------------


    @Data
    @AllArgsConstructor
    public static class Argument {
        Annotation argAnnotation;
        Class argType;
    }

    // WebTaskBuilder class ----------------------------------------------------

    private static class WebTaskBuilder {
        private WebTaskImpl task = new WebTaskImpl();
        private MethodHandles.Lookup lookup = MethodHandles.lookup();

        void handler(Object object, Method method) throws ControllerException {
            if (!method.getReturnType().equals(Response.class)) {
                throw new ControllerException("Invalid declaration:  Invalid return type.");
            }
            try {
                task.setHandler(lookup.unreflect(method).bindTo(object));
                //task.setHandler();
            } catch (IllegalAccessException exception) {
                //impossible
                throw new ControllerException("Can't access the method.", exception);
            }
        }

        void path(String path) {
            task.setPath(path);
        }

        void method(String method) {
            task.setMethod(method);
        }

        void requestContentType(String contentType) {
            task.setRequestContentType(contentType);
        }

        void responseContentType(String contentType) {
            task.setResponseContentType(contentType);
        }

        void arguments(Parameter[] parameters) throws ControllerException {
            Argument[] arguments = new Argument[parameters.length];
            for (int i = 0; i < parameters.length; ++i) {
                if (parameters[i].isAnnotationPresent(RequestParam.class)) {
                    arguments[i] = new Argument(parameters[i].getAnnotation(RequestParam.class), parameters[i].getType());
                } else if (parameters[i].isAnnotationPresent(PartParam.class)) {
                    arguments[i] = new Argument(parameters[i].getAnnotation(PartParam.class), parameters[i].getType());
                } else if (parameters[i].isAnnotationPresent(Context.class)) {
                    arguments[i] = new Argument(parameters[i].getAnnotation(Context.class), parameters[i].getType());
                }  else {
                    throw new ControllerException("Invalid parameters, all parameters must be annotated.");
                }
            }
            task.setArguments(arguments);
        }

        void securityLevel(byte level) {
            task.setSecurityLevel(level);
        }

        void controllerContext(ControllerContext context) {
            task.setControllerContext(context);
        }

        WebTask build() {
            return task;
        }
    }
}
