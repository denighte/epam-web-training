package by.radchuk.task.controller;

import by.radchuk.task.util.ClassReflections;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

class Scanner {
    Collection<Class> scan(String clsPackage, Class annotationClass) throws ControllerException {
        try {
            return ClassReflections.builder().load(clsPackage)
                    .filter(annotationClass).get();

        } catch (IOException exception) {
            throw new ControllerException("Failed to scan the package!", exception);
        }
    }
}
