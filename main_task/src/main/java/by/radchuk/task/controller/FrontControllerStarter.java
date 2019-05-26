package by.radchuk.task.controller;

import by.radchuk.task.controller.context.ControllerContext;
import by.radchuk.task.controller.annotation.WebHandler;
import by.radchuk.task.controller.annotation.WebRequestFilter;
import by.radchuk.task.controller.annotation.WebResponseFilter;
import by.radchuk.task.controller.filter.*;
import by.radchuk.task.controller.filter.impl.WebFilterContainerImpl;
import by.radchuk.task.controller.filter.impl.WebFilterFactoryImpl;
import by.radchuk.task.controller.security.SecurityFilter;
import by.radchuk.task.controller.security.SecurityFilterProcessor;
import by.radchuk.task.controller.task.impl.WebTaskContainerImpl;
import by.radchuk.task.controller.task.impl.WebTaskFactoryImpl;
import by.radchuk.task.controller.task.WebTask;
import by.radchuk.task.controller.task.WebTaskContainer;
import by.radchuk.task.controller.task.WebTaskFactory;
import by.radchuk.task.dao.framework.ConnectionManager;
import by.radchuk.task.dao.framework.H2Manger;
import by.radchuk.task.util.ClassScanner;
import by.radchuk.task.util.prefs.FilePreferencesFactory;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Collection;
import java.util.prefs.Preferences;

@Slf4j
@WebListener("Starts up controller servlet.")
public class FrontControllerStarter implements ServletContextListener {
    static {
        PreferencesController.initPreferences();
    }

    private static final String TASK_PACKAGE = "by.radchuk.task.service";
    private static final String FILTER_PACKAGE = "by.radchuk.task.filter";

    //@Autowired
    private ConnectionManager connectionManager = H2Manger.getInstance();
    //@Autowired
    private PreferencesController preferencesController = new PreferencesController();
    //@Autowired
    private ClassScanner classScanner = new ClassScanner();
    //@Autowired
    private WebTaskFactory taskFactory = new WebTaskFactoryImpl();
    //@Autowired
    private WebTaskContainer taskContainer = new WebTaskContainerImpl();
    //@Autowired
    private WebFilterFactory<RequestFilter> requestFilterFactory = new WebFilterFactoryImpl<>();
    //@Autowired
    private WebFilterFactory<ResponseFilter> responseFilterFactory = new WebFilterFactoryImpl<>();
    //@Autowired
    private SecurityFilterProcessor securityFilterProcessor = new SecurityFilterProcessor();
    //@Autowired
    private SecurityFilter securityFilter = new SecurityFilter();
    //@Autowired
    private WebFilterContainer<RequestFilter> requestFilterContainer
            = new WebFilterContainerImpl<RequestFilter>();
    //@Autowired
    private WebFilterContainer<ResponseFilter> responseFilterContainer
            = new WebFilterContainerImpl<ResponseFilter>();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            //Initializing preferences controller
            ServletContext servletContext = sce.getServletContext();
            preferencesController.init(servletContext);

            //Initializing controller context
            ControllerContext controllerContext
                    = ControllerContext.builder()
                                       .taskContainer(taskContainer)
                                       .connectionManager(connectionManager)
                                       .requestFilterContainer(requestFilterContainer)
                                       .responseFilterContainer(responseFilterContainer)
                                       .securityFilter(securityFilter)
                                       .build();

            //Setting controller context on all dependent classes.
            taskFactory.setControllerContext(controllerContext);
            requestFilterFactory.setControllerContext(controllerContext);
            responseFilterFactory.setControllerContext(controllerContext);

            //initializing db;
            connectionManager.init();

            //Scanning for WebTask classes.
            Collection<Class> taskClasses = classScanner
                        .scan(Preferences.userNodeForPackage(WebTaskContainerImpl.class)
                        .get("ct_task_package", TASK_PACKAGE), WebHandler.class);
            //Adding them to task container.
            for (var cls : taskClasses) {
                for (var task : taskFactory.create(cls)) {
                    taskContainer.addTask(task);
                }
            }

            //Scanning for request filters.
            Collection<Class> requestFilterClasses = classScanner
                        .scan(Preferences.userNodeForPackage(WebTaskContainerImpl.class)
                        .get("ct_filter_package", FILTER_PACKAGE), WebRequestFilter.class);
            //Adding them to request filter container.
            for(var cls : requestFilterClasses) {
                if (RequestFilter.class.isAssignableFrom(cls)) {
                    requestFilterContainer.addFilter(requestFilterFactory.create(cls));
                }
            }
            //adding security filter;
            securityFilter.init(controllerContext);
            FilterInfo info = securityFilterProcessor.getInfo(taskContainer.tasks());
            requestFilterContainer.addFilter(securityFilter, info);
            //Commit filter adding.
            requestFilterContainer.init();

            //Scanning for response filters.
            Collection<Class> responseFilterClasses = classScanner
                    .scan(Preferences.userNodeForPackage(WebTaskContainerImpl.class)
                    .get("ct_filter_package", FILTER_PACKAGE), WebResponseFilter.class);
            //Adding them to response filter container.
            for(var cls : responseFilterClasses) {
                 if (ResponseFilter.class.isAssignableFrom(cls)) {
                    responseFilterContainer.addFilter(responseFilterFactory.create(cls));
                }
            }
            //Commit filter adding.
            responseFilterContainer.init();

            //initializing Front Controller.
            var controllerServlet = new FrontControllerServlet(taskContainer, requestFilterContainer, responseFilterContainer);
            var controllerServletRegistration = servletContext.addServlet("FrontControllerServlet", controllerServlet);
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
            connectionManager.close();
        } catch (Exception exception) {
            log.error("Error during resources closing.", exception);
            throw new RuntimeException(exception);
        }
    }
}
