package by.radchuk.task.controller.exception;

import by.radchuk.task.controller.Response;

public class NotAuthorizedException extends WebClientException {
    public NotAuthorizedException() {
        super(Response.builder().error(401, "Unauthorized").build());
    }
}
