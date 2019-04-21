package by.radchuk.task.service;

import by.radchuk.task.controller.RequestContext;
import by.radchuk.task.controller.Response;
import by.radchuk.task.controller.annotation.*;

@WebHandler
public class ServiceClass {
    @Context
    RequestContext context;

    @Path("/test")
    @HttpMethod("GET")
    @Consume("multipart/form-data")
    @Produce("application/json")
    public Response process(@RequestParam("param") String param) {
        return Response.builder().data("sample").build();
    }
}

