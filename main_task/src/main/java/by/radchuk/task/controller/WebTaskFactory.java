package by.radchuk.task.controller;

import by.radchuk.task.controller.annotation.*;
import by.radchuk.task.service.ServiceClass;
import by.radchuk.task.util.FieldReflections;
import by.radchuk.task.util.MethodReflections;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

//make package private?
public class WebTaskFactory {
    public List<WebTask> create(Class cls) throws ControllerException{
        Object object;
        String path;
        Field contextHolder = FieldReflections.builder()
                                              .load(cls)
                                              .findAnnotated(Context.class)
                                              .setAccessible().get().get(0);

        try {
            object = cls.newInstance();
        } catch (InstantiationException | IllegalAccessException exception) {
            throw new ControllerException("Class instantiation/access exception.", exception);
        }

        List<WebTask> tasks = new ArrayList<>();

        for (Method method : MethodReflections.builder().load(cls)
                                             .filter(Path.class).setAccessible().get()) {
            WebTaskBuilder builder = new WebTaskBuilder();
            builder.context(object, contextHolder);
            builder.handler(object, method);

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

            builder.parametersNames(method.getParameters());

            tasks.add(builder.build());

        }
        return tasks;
    }

    private static class WebTaskBuilder {
        private WebTask task = new WebTask();
        private MethodHandle handler;
        private MethodHandles.Lookup lookup = MethodHandles.lookup();

        void context(Object object, Field context) throws ControllerException{
            try {
                task.setContext(lookup.unreflectSetter(context).bindTo(object));
            } catch (IllegalAccessException exception) {
                //impossible
                throw new ControllerException("Can't access the field.", exception);
            }
        }

        void handler(Object object, Method method) throws ControllerException {
            if (!method.getReturnType().equals(Response.class)) {
                throw new ControllerException("Invalid declaration:  Invalid return type.");
            }

            try {
                handler = lookup.unreflect(method).bindTo(object);
                //task.setHandler();
            } catch (IllegalAccessException exception) {
                //impossible
                throw new ControllerException("Can't access the method.", exception);
            }
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

        void parametersNames(Parameter[] parameters) throws ControllerException {
            String[] parametersNames = new String[parameters.length];
            for (int i = 0; i < parameters.length; ++i) {
                if (parameters[i].isAnnotationPresent(RequestParam.class)) {
                    parametersNames[i] = parameters[i].getAnnotation(RequestParam.class).value();
                } else {
                    throw new ControllerException("Invalid parameters, use 'RequestParam' annotation.");
                }
            }
            task.setHandler(handler.asSpreader(String[].class, parameters.length));
            task.setParametersNames(parametersNames);
        }

        WebTask build() {
            return task;
        }
    }


    public static void main(String[] args) throws ControllerException{
        WebTaskFactory factory = new WebTaskFactory();
        WebTask task = factory.create(ServiceClass.class).get(0);
        System.out.println(task.getMethod());
        System.out.println("finally");
    }
}
