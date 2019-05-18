package by.radchuk.task.controller;

public class WebApplicationException extends RuntimeException {

    private Response response;
    private int status;

    public WebApplicationException() {
        super();
    }
    public WebApplicationException(Response response) {
        super();
        this.response = response;
    }
    public WebApplicationException(int status) {
        super();
        this.status = status;
    }
    public WebApplicationException(Throwable cause) {
        super(cause);
    }
    public WebApplicationException(Throwable cause,
                                   Response response) {
        super(cause);
        this.response = response;
    }
    public WebApplicationException(Throwable cause, int status) {
        super(cause);
        this.status = status;
    }

    public Response getResponse() {
        return response;
    }
}
