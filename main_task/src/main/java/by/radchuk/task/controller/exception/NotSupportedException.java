package by.radchuk.task.controller.exception;

import by.radchuk.task.controller.Response;

public class NotSupportedException extends WebClientException {
    public NotSupportedException() {
        super(Response.builder().error(415, "Not supported Media type.").build());
    }
}
