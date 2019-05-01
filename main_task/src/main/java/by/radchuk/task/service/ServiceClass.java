package by.radchuk.task.service;

import by.radchuk.task.controller.RequestContext;
import by.radchuk.task.controller.Response;
import by.radchuk.task.controller.annotation.*;

@WebHandler
@Path("/test")
public class ServiceClass {
    @Context
    RequestContext context;

    @Path("/test/*")
    @HttpMethod("GET")
    public Response process(@RequestParam("param") String param) {
        return Response.builder().data("WebHandler response: " + param).build();
    }
}

