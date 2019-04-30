package by.radchuk.task.util;

import lombok.var;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MethodReflections {
    private List<Method> methods;

    private MethodReflections() {
        methods = new ArrayList<>();
    }

    public static MethodReflections builder() {
        return new MethodReflections();
    }

    public MethodReflections load(Class cls) {
        Collections.addAll(methods, cls.getDeclaredMethods());
        return this;
    }

    public MethodReflections setAccessible() {
        methods.forEach(method -> method.setAccessible(true));
        return this;
    }

    public <T extends Annotation> MethodReflections filter(Class<T> annotationClass) {
        methods = methods.stream().filter(method -> method.isAnnotationPresent(annotationClass)).collect(Collectors.toList());
        return this;
    }

    public List<Method> get() {
        return methods;
    }

}
