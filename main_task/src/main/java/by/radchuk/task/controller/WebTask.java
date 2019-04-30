package by.radchuk.task.controller;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.WrongMethodTypeException;

@Slf4j
public class WebTask {
    @Setter
    private String path;
    @Setter
    private String method;
    @Setter
    private String requestContentType;
    @Setter
    private String responseContentType;
    @Setter
    private MethodHandle handler;
    @Setter
    private String[] parametersNames;
    @Setter
    private MethodHandle context;

    protected String getMethod() {
        return method;
    }

    protected String getURI() {
        return path;
    }

    protected String getRequestContentType() {
        return requestContentType;
    }

    protected Response execute(HttpServletRequest request,
                          HttpServletResponse response) {
        String[] parameters = new String[parametersNames.length];
        for(int i = 0; i < parametersNames.length; ++i) {
            parameters[i] = request.getParameter(parametersNames[i]);
        }
        Response result;
        if (context != null) {
            try {
                context.invokeExact(new RequestContext(request, response));
            } catch (Throwable throwable) {
                log.error("Error during context injection.", throwable);
                return createDefaultErrorResponse();
            }
        }

        try {
            result = (Response) handler.invokeExact(parameters);
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

    private Response createDefaultErrorResponse() {
        return Response.builder()
                .status(500)
                .data("Error during web service execution.")
                .build();
    }

}
