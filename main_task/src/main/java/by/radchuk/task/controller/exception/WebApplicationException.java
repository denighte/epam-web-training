package by.radchuk.task.controller.exception;

import by.radchuk.task.controller.Response;

public class WebApplicationException extends WebClientException {
    public WebApplicationException(int status) {
        super();
        response = Response.builder().status(status).build();
    }

    public WebApplicationException(Throwable cause) {
        super(cause);
        response = Response.builder().error(503, cause.getMessage()).build();
    }

    public WebApplicationException(Throwable cause,
                                   Response response) {
        super(cause);
        this.response = response;
    }
    public WebApplicationException(Throwable cause, int status) {
        super(cause);
        response = Response.builder().error(status, cause.getMessage()).build();
    }

    public Response getResponse() {
        return response;
    }
}
