package by.radchuk.task.controller;

import by.radchuk.task.controller.annotation.*;
import by.radchuk.task.service.ServiceClass;
import lombok.var;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//make package private?
public class WebServiceTaskFactory {
    public List<WebServiceTask> create(Class cls) {
        Method[] methods = cls.getDeclaredMethods();
        List<WebServiceTask> tasks = new ArrayList<>();

        Object object = null;
        try {
            object = cls.newInstance();
        } catch (InstantiationException | IllegalAccessException exception) {
            throw new RuntimeException("Class instantiation/access exception.", exception);
        }

        String path = null;
        Field contextHolder = null;

        for (var field : cls.getDeclaredFields()) {
            if (field.isAnnotationPresent(Context.class)) {
                field.setAccessible(true);
                if (!field.getType().equals(RequestContext.class)) {
                    throw new RuntimeException("Invalid field annotated with @Context annotation.");
                }
                contextHolder = field;
                break;
            }
        }
        for(var method : methods) {
            if (method.isAnnotationPresent(Path.class)) {
                var taskBuilder = WebServiceTask.builder();
                path = method.getAnnotation(Path.class).value();
                taskBuilder.path(path);
                taskBuilder.object(object);
                taskBuilder.context(contextHolder);

                if (method.isAnnotationPresent(HttpMethod.class)) {
                    taskBuilder.method(method.getAnnotation(HttpMethod.class).value());
                } else {
                    throw new RuntimeException("Unknown method.");
                }
                if (!method.getReturnType().equals(Response.class)) {
                    throw new RuntimeException("Invalid return type.");
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
                        throw new RuntimeException("Invalid parameters");
                    }
                }
                taskBuilder.parametersNames(parametersNames);

                tasks.add(taskBuilder.build());
            }
        }
        if (path == null) {
            throw new RuntimeException("No annotated methods!");
        }
        return tasks;
    }


    public static void main(String[] args) {
        WebServiceTaskFactory factory = new WebServiceTaskFactory();
        WebServiceTask task = factory.create(ServiceClass.class).get(0);
        System.out.println(task.getMethod());
        System.out.println("finally");
    }
}
