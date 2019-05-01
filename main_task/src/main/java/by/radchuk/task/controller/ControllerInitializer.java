package by.radchuk.task.controller;

import by.radchuk.task.context.AppContext;
import by.radchuk.task.dao.framework.ConnectionManager;
import by.radchuk.task.dao.framework.H2Manger;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.prefs.Preferences;

@Slf4j
@WebListener("Starts up controller servlet.")
public class ControllerInitializer implements ServletContextListener {
    private static final String PACKAGE_TO_SCAN = "by.radchuk.task.service";

    private ConnectionManager manager = H2Manger.getInstance();
    private AppContext appContext = new AppContext();
    private WebTaskContainer taskContainer = new WebTaskContainer();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ServletContext context = sce.getServletContext();
            appContext.init(context);
            var controllerServlet = new FrontControllerServlet();
            //do init here
            //scan, injection ect.
            taskContainer.scan(Preferences.userNodeForPackage(WebTaskContainer.class).get("ct_scan_package", PACKAGE_TO_SCAN));
            var controllerServletRegistration = context.addServlet("FrontControllerServlet", controllerServlet);
            //do config here
            controllerServletRegistration.setMultipartConfig(new MultipartConfigElement(System.getProperty("java.io.tmpdir")));
        } catch (Exception exception) {
            log.error("Error during context initialization.", exception);
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            manager.close();
        } catch (Exception exception) {
            log.error("Error during resources closing.", exception);
            throw new RuntimeException(exception);
        }
    }
}
