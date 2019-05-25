package by.radchuk.task.service;

import by.radchuk.task.controller.Response;
import by.radchuk.task.controller.annotation.*;
import by.radchuk.task.controller.exception.BadRequestException;
import by.radchuk.task.controller.security.framework.JWT;
import by.radchuk.task.dao.DaoException;
import by.radchuk.task.dao.UserDao;
import by.radchuk.task.model.User;
import by.radchuk.task.security.PasswordAuthentication;

import java.io.IOException;

@WebHandler
public class LoginService {
    private PasswordAuthentication authentication;

    public LoginService() {
        authentication = new PasswordAuthentication();
    }

    @Path("/register")
    @HttpMethod("POST")
    public Response register(@RequestParam("user") User user) {
        if (user == null || user.getPassword() == null || user.getName() == null) {
            throw new BadRequestException();
        }
        user.setPassword(authentication.hash(user.getPassword()));
        try (UserDao dao = new UserDao()) {
            if (dao.find(user.getName()) == null) {
                dao.save(user);
            } else {
                return Response.builder().data("User with this name already exists.").build();
            }
        } catch (DaoException exception) {
            return Response.builder().error(503, "Service unavailable.").build();
        }
        return Response.builder().data("Successfully registered.").build();
    }

    @Path("/login")
    @HttpMethod("POST")
    public Response login(@RequestParam("name") String name,
                          @RequestParam("password") String password) throws IOException {
        if(name == null || password == null) {
            throw  new BadRequestException();
        }
        User user;
        try (UserDao dao = new UserDao()) {
            user = dao.find(name);
        } catch (DaoException exception) {
            return Response.builder().error(503, "Service unavailable.").build();
        }

        if (user == null) {
            return Response.builder().data("There is no user with this name.").build();
        }

        try {
            if(!authentication.authenticate(password, user.getPassword())) {
                return Response.builder().error(401, "Invalid password").build();
            }
        } catch (IllegalArgumentException exception) {
            return Response.builder().error(401, "Invalid password").build();
        }


        String token = JWT.encoder().withHeader("alg", "HMAC512")
                                    .withHeader("typ", "JWT")
                                    .setPayload(user).sign();
        return Response.builder().data(token).build();
    }
}

