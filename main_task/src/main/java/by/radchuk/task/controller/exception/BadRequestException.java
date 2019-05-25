package by.radchuk.task.controller.exception;

import by.radchuk.task.controller.Response;

public class BadRequestException extends WebClientException {
    public BadRequestException() {
        super(Response.builder()
                .error(400, "Invalid request parameters.")
                .build());
    }

    public BadRequestException(String message) {
        super(Response.builder()
                .error(400, message)
                .build());
    }
}
