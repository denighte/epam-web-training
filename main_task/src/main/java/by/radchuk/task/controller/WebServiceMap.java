package by.radchuk.task.controller;

import by.radchuk.task.controller.annotation.HttpMethod;
import by.radchuk.task.controller.annotation.WebHandler;
import by.radchuk.task.util.Reflections;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service handler mapping class.
 * Contains pairs urlPattern --> ServiceTaskImpl
 *
 */
@Slf4j
public class WebServiceMap {
    //private final int HTTP_METHODS_NUMBER = HttpMethod.values().length;
    private static final File[] NO_FILES = {};
    private Map<String, Class[]> handlerMap;

    WebServiceMap() {
        handlerMap = new HashMap<>();


    }

    public void scan(String clsPackage) throws ScanException {
        try {
            List<Class> classes = Reflections.builder().loadClasses(clsPackage)
                                                       .filter(WebHandler.class).get();
            List<WebServiceTask> tasks = classes.stream().map(cls -> WebServiceTask.load(cls))
                                                .collect(Collectors.toList());
        } catch (IOException exception) {
            throw new ScanException("Failed to scan the package!", exception);
        }
    }

    public Class<HttpHandler> getHandler(String url, String method) {
        //@SuppressWarnings("unchecked")
        //Class<HttpHandler> handler = handlerMap.get(url)[method.ordinal()];
        return null;
    }







//    private void addHandler(WebHandler annotation, Class cls) {
//        Class[] urlHandlers = handlerMap.get(annotation.url());
//        int methodOrdinal = annotation.method().ordinal();
//        if (urlHandlers == null) {
//            urlHandlers = new Class[HTTP_METHODS_NUMBER];
//            urlHandlers[methodOrdinal] = cls;
//            handlerMap.put(annotation.url(), urlHandlers);
//        } else if (urlHandlers[methodOrdinal] == null) {
//            urlHandlers[methodOrdinal] = cls;
//        } else {
//            throw new IllegalArgumentException("Duplicate handler with url="
//                                               + annotation.url()
//                                               + " and HttpMethod="
//                                               + annotation.method().name());
//        }
//    }

    public static void main(String[] args) throws ScanException {
        WebServiceMap map = new WebServiceMap();
        map.scan("by.radchuk.task.service");
        //System.out.println(map.getHandler("/test", HttpMethod.GET).getName());
    }
}
