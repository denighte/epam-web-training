package by.radchuk.task.service;

import by.radchuk.task.controller.Response;
import by.radchuk.task.controller.annotation.*;
import by.radchuk.task.controller.exception.BadRequestException;
import by.radchuk.task.dao.DaoException;
import by.radchuk.task.dao.PhotoPostDao;
import by.radchuk.task.dao.UserDao;
import by.radchuk.task.model.FilterConfig;
import by.radchuk.task.model.PhotoPost;
import by.radchuk.task.model.User;

import java.sql.Date;
import java.util.List;

@WebHandler
@Secure
//request param example: %7B"skip":5,"top":5%7D
public class SearchService {
    @Path("/search")
    @HttpMethod("GET")
    public Response search(@RequestParam("config")FilterConfig config) {
        if (!validateFilterConfig(config)) {
            throw new BadRequestException();
        }
        try(PhotoPostDao postDao = new PhotoPostDao(); UserDao userDao = new UserDao()) {
            Integer userId = null;
            if (config.getUser() != null) {
                User user = userDao.find(config.getUser());
                if (user == null) {
                    throw new BadRequestException();
                }
                userId = user.getId();
            }
            List<PhotoPost> posts = postDao.find(config.getSkip(), config.getTop(), userId, config.getDate());
            return Response.builder().entity(posts).build();
        } catch (DaoException exception) {
            return Response.builder().error(503, "Unknown error, please try again later.").build();
        }
    }

    private boolean validateFilterConfig(FilterConfig config) {
        if (config == null) {
            return false;
        }
        if (config.getDate() != null) {
            try {
                Date.valueOf(config.getDate());
            } catch (IllegalArgumentException exception) {
                return false;
            }
        }
        if (config.getSkip() < 0 || config.getTop() <= 0) {
            return false;
        }
        return true;
    }
}
