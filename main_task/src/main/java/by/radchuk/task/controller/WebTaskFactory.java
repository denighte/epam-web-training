package by.radchuk.task.controller;

import by.radchuk.task.controller.annotation.*;
import by.radchuk.task.util.MethodReflections;
import by.radchuk.task.util.StringView;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.WrongMethodTypeException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

//make package private?
public class WebTaskFactory {
    public List<WebTask> create(Class cls) throws ControllerException{
        Object object;
        String classPath = "";
        if (cls.isAnnotationPresent(Path.class)) {
            classPath = ((Path) cls.getAnnotation(Path.class)).value();
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

            builder.arguments(method.getParameters());

            tasks.add(builder.build());

        }
        return tasks;
    }

    // Inner classes -----------------------------------------------------------

    // WebTaskImpl class -------------------------------------------------------

    @Slf4j
    public static class WebTaskImpl implements WebTask {
        private StringView path;
        private String method;
        private String requestContentType;
        private String responseContentType;
        private MethodHandle handler;
        private Annotation[] argumentsAnnotations;

        @Override
        public String getMethod() {
            return method;
        }

        @Override
        public StringView getPath() {
            return path;
        }

        @Override
        public String getRequestContentType() {
            return requestContentType;
        }

        @Override
        public Response execute(final HttpServletRequest request,
                                final HttpServletResponse response)
                                    throws IOException, ServletException {
            Object[] arguments = processArguments(request, response);
            Response result;
            try {
                result = (Response) handler.invokeExact(arguments);
            } catch (WrongMethodTypeException | ClassCastException exception) {
                log.error("Error invoking web service method.", exception);
                return createDefaultErrorResponse();
            } catch (Throwable throwable) {
                log.error("Error during web service execution.", throwable);
                return createDefaultErrorResponse();
            }
            if (responseContentType != null) {
                result.setType(responseContentType);
            }
            return result;
        }

        private Object[] processArguments(final HttpServletRequest request,
                                          final HttpServletResponse response)
                                            throws IOException, ServletException {
            Object[] arguments = new Object[argumentsAnnotations.length];
            for(int i = 0; i < argumentsAnnotations.length; ++i) {
                Annotation annotation = argumentsAnnotations[i];
                annotation.annotationType();
                Class annotationClass = annotation.getClass();

                if (annotationClass.equals(RequestParam.class)) {
                    RequestParam param = (RequestParam)annotation;
                    arguments[i] = request.getParameter(param.value());
                } else if (annotationClass.equals(Context.class)) {
                    arguments[i] = new RequestContext(request, response);
                } else if (annotationClass.equals(PartParam.class)) {
                    PartParam param = (PartParam)annotation;
                    arguments[i] = request.getPart(param.value());
                }
            }
            return arguments;
        }

        private Response createDefaultErrorResponse() {
            return Response.builder()
                    .status(500)
                    .data("Error during web service execution.")
                    .build();
        }

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
                task.handler = lookup.unreflect(method).bindTo(object);
                //task.setHandler();
            } catch (IllegalAccessException exception) {
                //impossible
                throw new ControllerException("Can't access the method.", exception);
            }
        }

        void path(String path) throws ControllerException {
            task.path = new StringView(path);
        }

        void method(String method) {
            task.method = method;
        }

        void requestContentType(String contentType) {
            task.requestContentType = contentType;
        }

        void responseContentType(String contentType) {
            task.responseContentType = contentType;
        }

        void arguments(Parameter[] parameters) throws ControllerException {
            Annotation[] webParameters = new Annotation[parameters.length];
            for (int i = 0; i < parameters.length; ++i) {
                if (parameters[i].isAnnotationPresent(RequestParam.class)) {
                    webParameters[i] = parameters[i].getAnnotation(RequestParam.class);
                } else if (parameters[i].isAnnotationPresent(PartParam.class)) {
                    webParameters[i] = parameters[i].getAnnotation(PartParam.class);
                } else if (parameters[i].isAnnotationPresent(Context.class)) {
                    webParameters[i] = parameters[i].getAnnotation(Context.class);
                }  else {
                    throw new ControllerException("Invalid parameters, use 'RequestParam' annotation.");
                }
            }
            task.argumentsAnnotations = webParameters;
        }

        WebTask build() {
            return task;
        }
    }

    @Data
    static class Argument {
        private Annotation annotation;
        private Class type;
    }
}
