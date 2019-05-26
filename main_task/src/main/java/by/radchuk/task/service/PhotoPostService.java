package by.radchuk.task.service;

import by.radchuk.task.controller.ContentType;
import by.radchuk.task.controller.Response;
import by.radchuk.task.controller.annotation.*;
import by.radchuk.task.controller.exception.BadRequestException;
import by.radchuk.task.dao.DaoException;
import by.radchuk.task.dao.PhotoPostDao;
import by.radchuk.task.dao.UserDao;
import by.radchuk.task.model.PhotoPost;
import by.radchuk.task.model.User;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;

@Slf4j
@Secure
@WebHandler
@Path("/post")
public class PhotoPostService {
    @HttpMethod("POST")
    @Consume(ContentType.MULTIPART_FORM_DATA)
    public Response add(@Context User authorizedUser,
                        @Context ServletContext servletContext,
                        @PartParam("file")Part file,
                        @RequestParam("post")PhotoPost post) {
        if (!validatePost(post)) {
            throw new BadRequestException();
        }

        int postId;
        java.nio.file.Path relativePath = Paths.get("resources", "img", file.getSubmittedFileName());
        java.nio.file.Path realPath = Paths.get(servletContext.getRealPath("/")).resolve(relativePath);
        try(PhotoPostDao postDao = new PhotoPostDao(); UserDao userDao = new UserDao()) {
            //residual?
//            if (userDao.find(post.getUserId()) == null) {
//                return Response.builder().error(400, "User id is invalid.").build();
//            }
            Files.copy(file.getInputStream(), realPath);
            post.setUserId(authorizedUser.getId());
            post.setSrc(relativePath.toString());
            postId = postDao.save(post);
        } catch (DaoException | IOException exception) {
            try {
                Files.deleteIfExists(realPath);
            } catch (IOException ioException) {
                log.warn("Can't delete photo post image file, filename={}", file.getSubmittedFileName());
            }
            return Response.builder().error(503, "Error during photo post saving.").build();
        }
        return Response.builder().data("{\"id\":" + postId + "}").build();
    }

    @HttpMethod("GET")
    @Consume(ContentType.MULTIPART_FORM_DATA)
    public Response get(@RequestParam("id") int id) {
        try (PhotoPostDao postDao = new PhotoPostDao()) {
            return Response.builder().entity(postDao.find(id)).build();
        } catch (DaoException exception) {
            return Response.builder()
                           .error(503, "Service is unavailable, please try again later.")
                           .build();
        }
    }

    @HttpMethod("DELETE")
    public Response delete(@Context User user,
                           @Context ServletContext servletContext,
                           @RequestParam("id") int id) {
        try (PhotoPostDao postDao = new PhotoPostDao()) {
            PhotoPost post = postDao.find(id);
            if (post == null) {
                return Response.builder().data("There is no photo post with specified id.").build();
            }
            if (user.getId() != post.getUserId()) {
                return Response.builder().data("unauthorized access").build();
            }
            postDao.delete(id);
            Files.delete(Paths.get(servletContext.getRealPath("/")).resolve(Paths.get(post.getSrc())));
        } catch (DaoException exception) {
            return Response.builder().data("Failed to delete photo post.").build();
        } catch (IOException exception) {
            //add file to special list?
            //or just ignore?
        }
        return Response.builder().data("Successfully deleted.").build();
    }

    private boolean validatePost(PhotoPost post) {
        if (post == null || post.getCreationDate() == null) {
            return false;
        }
        try {
            Date.valueOf(post.getCreationDate());
        } catch (IllegalArgumentException exception) {
            return false;
        }
        return true;
    }
}
