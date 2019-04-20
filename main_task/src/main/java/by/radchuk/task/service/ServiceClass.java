package by.radchuk.task.service;

import by.radchuk.task.controller.HttpHandler;
import by.radchuk.task.controller.annotation.HttpMethod;
import by.radchuk.task.controller.annotation.Path;
import by.radchuk.task.controller.annotation.WebHandler;
import by.radchuk.task.dao.DaoException;
import by.radchuk.task.dao.UserDao;
import by.radchuk.task.model.User;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebHandler
public class ServiceClass {
    //@Get
    //execute(@RequestParam param, ...)

    @Path("/test")
    @HttpMethod("GET")
    public String process() {
        return "A_B";
    }
}

