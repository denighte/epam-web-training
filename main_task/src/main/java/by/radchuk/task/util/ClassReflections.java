package by.radchuk.task.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ClassReflections {
    private static final File[] NO_FILES = {};
    private List<Class> classes;

    private ClassReflections() {
        classes = new ArrayList<>();
    }

    public static ClassReflections builder() {
        return new ClassReflections();
    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     * NOTE: It seems that class loaders return URLs in UTF-8 encoding.
     * @param packageName The base package
     * @return The classes
     * @throws IOException
     */
    @SneakyThrows(UnsupportedEncodingException.class)
    public ClassReflections load(String packageName) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(URLDecoder.decode(resource.getFile(), "UTF-8")));
        }
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return this;
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     */
    private List<Class> findClasses(File directory, String packageName) {
        List<Class> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files == null) {
            files = NO_FILES;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                String className = "";
                try {
                    className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    classes.add(Class.forName(className));
                } catch (ClassNotFoundException exception) {
                    log.warn("can't get class for name={}, it may not be loaded by class loader.", className);
                }
            }
        }
        return classes;
    }

    public <T extends Annotation> ClassReflections filter(Class<T> annotationClass) {
        classes = classes.stream().filter(cls -> cls.isAnnotationPresent(annotationClass)).collect(Collectors.toList());
        return this;
    }

    public List<Class> get() {
        return classes;
    }
}
