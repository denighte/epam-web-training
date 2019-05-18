package by.radchuk.task.controller.impl;

import by.radchuk.task.controller.Response;
import by.radchuk.task.controller.WebApplicationException;
import by.radchuk.task.controller.WebTask;
import by.radchuk.task.controller.annotation.Context;
import by.radchuk.task.controller.annotation.PartParam;
import by.radchuk.task.controller.annotation.RequestParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.WrongMethodTypeException;

import static by.radchuk.task.controller.FrontControllerServlet.SECURITY_FILTER_ATTRIBUTE_NAME;

@Slf4j
@Setter
public class WebTaskImpl implements WebTask {
    private byte securityLevel;
    private String path;
    private String method;
    private String requestContentType;
    private String responseContentType;
    private MethodHandle handler;
    private WebTaskFactoryImpl.Argument[] arguments;

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public byte getSecurityLevel() {
        return securityLevel;
    }

    @Override
    public String getRequestContentType() {
        return requestContentType;
    }

    @Override
    public Response execute(final HttpServletRequest request,
                            final HttpServletResponse response) {
        try {
            ArgumentsProcessor processor = new ArgumentsProcessor(request, response);
            Object[] handlerArgs = processor.getArguments(arguments);
            Response result = (Response) handler.invokeWithArguments(handlerArgs);
            if (responseContentType != null) {
                result.setType(responseContentType);
            }
            return result;
        } catch (WebApplicationException exception) {
            throw exception;
        } catch (ArgumentsProcessor.ArgumentProcessException exception) {
            log.warn("Can't convert request parameters to required types: {}.", exception.getMessage());
            return createBadRequestResponse();
        } catch (WrongMethodTypeException | ClassCastException exception) {
            log.error("Error invoking web service method.", exception);
            return createDefaultErrorResponse();
        } catch (Throwable throwable) {
            log.error("Error during web service execution.", throwable);
            return createDefaultErrorResponse();
        }
    }

    private Response createBadRequestResponse() {
        return Response.builder()
                .error(400, "Invalid request parameters.")
                .build();
    }

    private Response createDefaultErrorResponse() {
        return Response.builder()
                .error(500, "Error during web service execution.")
                .build();
    }

    @AllArgsConstructor
    private static class ArgumentsProcessor {
        private HttpServletRequest request;
        private HttpServletResponse response;

        public Object[] getArguments(WebTaskFactoryImpl.Argument[] argumentsAnnotations)
                throws IOException, ServletException, ArgumentProcessException {
            Object[] arguments = new Object[argumentsAnnotations.length];
            for(int i = 0; i < argumentsAnnotations.length; ++i) {
                WebTaskFactoryImpl.Argument arg = argumentsAnnotations[i];
                Annotation argAnnotation = arg.argAnnotation;
                Class annotationClass = argAnnotation.annotationType();

                if (annotationClass.equals(RequestParam.class)) {
                    RequestParam param = (RequestParam)argAnnotation;
                    arguments[i] = processRequestParam(arg.argType, param.value());
                } else if (annotationClass.equals(Context.class)) {
                    arguments[i] = processContext(arg.argType);
                } else if (annotationClass.equals(PartParam.class)) {
                    PartParam param = (PartParam)argAnnotation;
                    arguments[i] = request.getPart(param.value());
                }
            }
            return arguments;
        }

        private Object processRequestParam(Class argClass, String paramName) throws ArgumentProcessException {
            try {
                String parameter = request.getParameter(paramName);
                SupportedRequestType type = SupportedRequestType.fromValue(argClass);
                switch (type) {
                    case STRING:
                        return parameter;
                    case INT:
                        assertNull(parameter);
                        return new Integer(parameter);
                    case BYTE:
                        assertNull(parameter);
                        return new Byte(parameter);
                    case CHAR:
                        assertNull(parameter);
                        if (parameter.length() > 1) {
                            throw new IllegalArgumentException();
                        }
                        return parameter.charAt(0);
                    case SHORT:
                        assertNull(parameter);
                        return new Short(parameter);
                    case LONG:
                        assertNull(parameter);
                        return new Long(parameter);
                    case FLOAT:
                        assertNull(parameter);
                        return new Float(parameter);
                    case DOUBLE:
                        assertNull(parameter);
                        return new Double(parameter);
                    default:
                        throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException exception) {
                throw new ArgumentProcessException("Unsupported argument type for '@RequestParam' = "
                                                   + argClass.getName(), exception);
            } catch (Exception exception) {
                throw new ArgumentProcessException("Can't convert request parameter to specified type = "
                        + argClass.getName(), exception);
            }
        }

        private Object processContext(Class argClass) throws ArgumentProcessException {
            try {
                SupportedContextType type = SupportedContextType.fromValue(argClass);
                switch (type) {
                    case REQUEST:
                        return request;
                    case RESPONSE:
                        return response;
                    case SERVLET_CONTEXT:
                        return request.getServletContext();
                    case HTTP_SESSION:
                        return request.getSession();
                    default:
                        throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException exception) {
                throw new ArgumentProcessException("Unsupported argument type for '@RequestParam' = "
                        + argClass.getName(), exception);
            } catch (Exception exception) {
                throw new ArgumentProcessException("Can't convert request parameter to specified type = "
                        + argClass.getName(), exception);
            }
        }

        @AllArgsConstructor
        private enum SupportedContextType {
            REQUEST(HttpServletRequest.class),
            RESPONSE(HttpServletResponse.class),
            SERVLET_CONTEXT(ServletContext.class),
            HTTP_SESSION(HttpSession.class);

            @Getter
            private Class type;

            public static SupportedContextType fromValue(Class cls) {
                for(var supportedType : SupportedContextType.values()) {
                    if(supportedType.type.equals(cls)) {
                        return supportedType;
                    }
                }
                throw new IllegalArgumentException();
            }
        }

        @AllArgsConstructor
        private enum SupportedRequestType {
            BYTE(byte.class),
            SHORT(short.class),
            CHAR(char.class),
            INT(int.class),
            LONG(long.class),
            FLOAT(float.class),
            DOUBLE(double.class),
            STRING(String.class);

            @Getter
            private Class type;

            public static SupportedRequestType fromValue(Class cls) {
                for(var supportedType : SupportedRequestType.values()) {
                    if(supportedType.type.equals(cls)) {
                        return supportedType;
                    }
                }
                throw new IllegalArgumentException();
            }
        }

        private void assertNull(String param) throws ArgumentProcessException {
            if (param == null) {
                throw new ArgumentProcessException("Can't convert request parameter to specified type: request param is null!");
            }
        }

        class ArgumentProcessException extends Exception {
            /**
             * Constructs a new exception with the specified detail message.  The
             * cause is not initialized, and may subsequently be initialized by
             * a call to {@link #initCause}.
             *
             * @param   message   the detail message. The detail message is saved for
             *          later retrieval by the {@link #getMessage()} method.
             */
            ArgumentProcessException(final String message) {
                super(message);
            }

            /**
             * Constructs a new exception with the specified detail message and
             * cause.  <p>Note that the detail message associated with
             * {@code cause} is <i>not</i> automatically incorporated in
             * this exception's detail message.
             *
             * @param  message the detail message (which is saved for later retrieval
             *         by the {@link #getMessage()} method).
             * @param  cause the cause (which is saved for later retrieval by the
             *         {@link #getCause()} method).  (A <tt>null</tt> value is
             *         permitted, and indicates that the cause is nonexistent or
             *         unknown.)
             */
            ArgumentProcessException(String message, Throwable cause) {
                super(message, cause);
            }
        }

    }
}
