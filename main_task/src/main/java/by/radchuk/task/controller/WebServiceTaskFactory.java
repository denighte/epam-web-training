package by.radchuk.task.controller;

import by.radchuk.task.controller.annotation.*;
import by.radchuk.task.service.ServiceClass;
import lombok.var;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//make package private?
public class WebServiceTaskFactory {
    public List<WebServiceTask> create(Class cls) throws ControllerException{
        Method[] methods = cls.getDeclaredMethods();
        List<WebServiceTask> tasks = new ArrayList<>();

        Object object = null;
        try {
            object = cls.newInstance();
        } catch (InstantiationException | IllegalAccessException exception) {
            throw new ControllerException("Class instantiation/access exception.", exception);
        }

        String path = null;
        Field contextHolder = null;

        for (var field : cls.getDeclaredFields()) {
            if (field.isAnnotationPresent(Context.class)) {
                field.setAccessible(true);
                if (!field.getType().equals(RequestContext.class)) {
                    throw new ControllerException("Invalid field annotated with @Context annotation.");
                }
                contextHolder = field;
                break;
            }
        }

        MethodHandle methodHandle;
        MethodHandles.Lookup lookup = MethodHandles.lookup();

        for(var method : methods) {
            if (method.isAnnotationPresent(Path.class)) {
                var taskBuilder = WebServiceTask.builder();
                path = method.getAnnotation(Path.class).value();
                taskBuilder.object(object);
                taskBuilder.path(path);
                taskBuilder.context(contextHolder);

                if (method.isAnnotationPresent(HttpMethod.class)) {
                    method.setAccessible(true);
                    try {
                        methodHandle = lookup.unreflect(method);
                    } catch (IllegalAccessException exception) {
                        throw new ControllerException("Can't access the method.");
                    }
                    taskBuilder.method(method.getAnnotation(HttpMethod.class).value());
                    taskBuilder.handler(methodHandle);

                } else {
                    throw new ControllerException("Unknown method.");
                }
                if (!method.getReturnType().equals(Response.class)) {
                    throw new ControllerException("Invalid return type.");
                }

                if (method.isAnnotationPresent(Consume.class)) {
                    taskBuilder.requestContentType(method.getAnnotation(Consume.class).value());
                }

                if (method.isAnnotationPresent(Produce.class)) {
                    taskBuilder.responseContentType(method.getAnnotation(Produce.class).value());
                }

                Parameter[] parameters = method.getParameters();
                String[] parametersNames = new String[parameters.length];
                for (int i = 0; i < parameters.length; ++i) {
                    if (parameters[i].isAnnotationPresent(RequestParam.class)) {
                        parametersNames[i] = parameters[i].getAnnotation(RequestParam.class).value();
                    } else {
                        throw new ControllerException("Invalid parameters");
                    }
                }
                taskBuilder.parametersNames(parametersNames);

                tasks.add(taskBuilder.build());
            }
        }
        return tasks;
    }


    public static void main(String[] args) throws ControllerException{
        WebServiceTaskFactory factory = new WebServiceTaskFactory();
        WebServiceTask task = factory.create(ServiceClass.class).get(0);
        System.out.println(task.getMethod());
        System.out.println("finally");
    }
}
