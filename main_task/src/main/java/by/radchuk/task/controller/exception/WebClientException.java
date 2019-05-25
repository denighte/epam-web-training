package by.radchuk.task.controller.exception;

import by.radchuk.task.controller.Response;

public class WebClientException extends RuntimeException {

    protected Response response;

    public WebClientException() {
        response = Response.builder().status(503).build();
    }
    public WebClientException(Response response) {
        super();
        this.response = response;
    }
    public WebClientException(Throwable cause) {
        super(cause);
    }
    public Response getResponse() {
        return response;
    }
}
