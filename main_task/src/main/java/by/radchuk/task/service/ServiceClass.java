package by.radchuk.task.service;

import by.radchuk.task.controller.impl.RequestContextImpl;
import by.radchuk.task.controller.Response;
import by.radchuk.task.controller.annotation.*;

@WebHandler
public class ServiceClass {

    @Path("/test")
    @HttpMethod("GET")
    public Response process1(@RequestParam("test") String param) {
        return Response.builder().data("WebHandler 1 response").dispatch("/check").build();
    }

    @Path("/check")
    @HttpMethod("GET")
    public Response process2() {
        return Response.builder().data("WebHandler 2 response").build();
    }
}

