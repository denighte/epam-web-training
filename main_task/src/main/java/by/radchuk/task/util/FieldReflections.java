package by.radchuk.task.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FieldReflections {
    private List<Field> fields;

    private FieldReflections() {
        fields = new ArrayList<>();
    }

    public static FieldReflections builder() {
        return new FieldReflections();
    }

    public FieldReflections load(Class cls) {
        Collections.addAll(fields, cls.getDeclaredFields());
        return this;
    }

    public FieldReflections setAccessible() {
        fields.forEach(field -> field.setAccessible(true));
        return this;
    }

    public <T extends Annotation> FieldReflections findAnnotated(Class<T> annotationClass) {
        fields = fields.stream().filter(field -> field.isAnnotationPresent(annotationClass)).collect(Collectors.toList());
        return this;
    }

    public List<Field> get() {
        return fields;
    }
}
