package by.radchuk.task.controller.exception;

import by.radchuk.task.controller.Response;

public class MethodNotSupportedException extends WebClientException {
    public MethodNotSupportedException() {
        super(Response.builder().error(405, "Method not supported.").build());
    }
}
