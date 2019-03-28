package by.radchuk.task4.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/locale"})
public class LocaleServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String newLocale = request.getParameter("locale");
        if (newLocale != null) {
            request.getSession().setAttribute("sessionLocale", newLocale);
        } else {
            newLocale = "en";
            request.getSession().setAttribute("sessionLocale", "en");
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(newLocale);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String locale = (String) request.getSession().getAttribute("sessionLocale");
        if (locale == null) {
            locale = "en";
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(locale);
    }
}
