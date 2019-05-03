package by.radchuk.task.controller;

import by.radchuk.task.context.AppContext;
import by.radchuk.task.controller.impl.WebContainerImpl;
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
    private WebContainer taskContainer = new WebContainerImpl();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ServletContext context = sce.getServletContext();
            appContext.init(context);
            taskContainer.scan(Preferences.userNodeForPackage(WebContainerImpl.class).get("ct_scan_package", PACKAGE_TO_SCAN));
            var controllerServlet = new FrontControllerServlet(taskContainer);

            var controllerServletRegistration = context.addServlet("FrontControllerServlet", controllerServlet);
            controllerServletRegistration.setMultipartConfig(new MultipartConfigElement(System.getProperty("java.io.tmpdir")));
            controllerServletRegistration.addMapping(taskContainer.tasks().stream().map(WebTask::getPath).toArray(String[]::new));
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
