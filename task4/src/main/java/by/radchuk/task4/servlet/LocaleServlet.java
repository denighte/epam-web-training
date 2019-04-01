package by.radchuk.task4.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Locale servlet.
 * Provides locale on index.jsp load.
 */
@WebServlet(urlPatterns = {"/locale"})
public class LocaleServlet extends HttpServlet {
    /**
     * Sets new locale for user.
     * @param request Http request object.
     * @param response Http response object.
     * @throws IOException in case IO exceptions.
     */
    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response)
                                            throws IOException {
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

    /**
     * retrieves locale from user session.
     * @param request Http request object
     * @param response Http response object.
     * @throws IOException in case IO exceptions.
     */
    @Override
    protected void doGet(final HttpServletRequest request,
                         final HttpServletResponse response)
                                            throws IOException {
        String locale = (String) request
                                .getSession()
                                .getAttribute("sessionLocale");
        if (locale == null) {
            locale = "en";
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(locale);
    }
}
