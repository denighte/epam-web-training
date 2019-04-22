package by.radchuk.task.controller;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.WrongMethodTypeException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
@Builder
public class WebServiceTask {
    private String path;
    private String method;
    private String requestContentType;
    private String responseContentType;
    private Object object;
    private MethodHandle handler;
    private String[] parametersNames;
    private Field context;
    private Response result;

    public String getMethod() {
        return method;
    }

    public String getURI() {
        return path;
    }

    public String getRequestContentType() {
        return requestContentType;
    }

    public String execute(HttpServletRequest request,
                          HttpServletResponse response) throws ControllerException {
        Object[] parameters = new Object[parametersNames.length + 1];
        parameters[0] = object;
        for(int i = 1; i < parametersNames.length + 1; ++i) {
            parameters[i] = request.getParameter(parametersNames[i - 1]);
        }
        try {
            if (context != null) {
                context.set(object, new RequestContext(request, response));
            }
            try {
                result = (Response) handler.invokeWithArguments(parameters);
            } catch (WrongMethodTypeException | ClassCastException exception) {
                log.error("Error invoking web service method.", exception);
                throw new ControllerException("Error during web service execution", exception);
            } catch (Throwable throwable) {
                response.setStatus(500);
                response.getWriter().write(throwable.getMessage());
                response.flushBuffer();
                throw new ControllerException("Error during web service execution", throwable);
            }
            if (responseContentType != null) {
                response.setContentType(requestContentType);
            }
            if (result.getStatus() == 0) {
                response.setStatus(200);
            } else {
                response.setStatus(result.getStatus());
            }
            return result.getData();
        } catch (IllegalAccessException | IOException exception) {
            log.error("Fatal: Error while setting context.", exception);
            throw new ControllerException("Fatal: Error while setting context.", exception);
        }

    }
}
