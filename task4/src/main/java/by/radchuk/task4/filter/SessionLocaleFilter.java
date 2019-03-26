package by.radchuk.task4.filter;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter(urlPatterns = {"/process"})
public class SessionLocaleFilter implements Filter {
    /**
     * checks the locale of the user.
     * sets the default locale in case locale is undefined.
     * @param request request
     * @param response response
     * @param chain filter chain
     * @throws IOException in case filter errors.
     * @throws ServletException in case filter errors.
     */
    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain)
                            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        if (req.getParameter("sessionLocale") != null) {
            req.getSession().setAttribute("sessionLocale",
                    req.getParameter("sessionLocale"));
        } else {
            req.getSession().setAttribute("sessionLocale", "en");
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() { }

    @Override
    public void init(final FilterConfig arg) throws ServletException { }

}