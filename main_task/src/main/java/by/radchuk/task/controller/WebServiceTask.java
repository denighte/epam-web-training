package by.radchuk.task.controller;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
@Builder
public class WebServiceTask {
    private String path;
    private String method;
    private String requestContentType;
    private String responseContentType;
    private Object object;
    private Method handler;
    private String[] parametersNames;
    private Field context;

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
                          HttpServletResponse response) {
        Object[] parameters = new Object[parametersNames.length];
        for(int i = 0; i < parametersNames.length; ++i) {
            parameters[i] = request.getParameter(parametersNames[i]);
        }
        try {
            if (context != null) {
                context.set(object, new RequestContext(request, response));
            }
            Response result = (Response)handler.invoke(object, parameters);
            if (responseContentType != null) {
                response.setContentType(requestContentType);
            }
            if (result.getStatus() == 0) {
                response.setStatus(200);
            } else {
                response.setStatus(result.getStatus());
            }
            return result.getData();
        } catch (IllegalAccessException | InvocationTargetException exception) {
            log.error("Fatal: Error invoking handler method.");
            //TODO: Add exception class.
            throw new RuntimeException("Fatal: Error invoking handler method.");
        }

    }
}
