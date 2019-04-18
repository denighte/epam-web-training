package by.radchuk.task.controller;

import by.radchuk.task.dao.DaoException;
import by.radchuk.task.dao.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/test"})
public class TestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (UserDao dao = new UserDao()) {
            resp.getWriter().write(dao.find(1).toString());
        } catch (DaoException exception) {
            resp.getWriter().write(exception.getMessage());
        }
    }
}
