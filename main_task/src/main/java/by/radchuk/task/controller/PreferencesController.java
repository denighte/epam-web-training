package by.radchuk.task.controller;

import by.radchuk.task.controller.task.impl.WebTaskContainerImpl;
import by.radchuk.task.dao.framework.H2Manger;
import by.radchuk.task.util.prefs.FilePreferencesFactory;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.*;
import java.util.prefs.Preferences;

@Slf4j
public final class PreferencesController {

    public static void initPreferences() {
        System.setProperty("java.util.prefs.PreferencesFactory", FilePreferencesFactory.class.getName());
        Path prefsPath = Paths.get("prefs", "app.preferences");
        if(!Files.exists(prefsPath)) {
            try {
                Path file = Files.createDirectories(prefsPath.getParent());
                @Cleanup
                var writer = Files.newBufferedWriter(prefsPath, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
                        + "<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">\n"
                        + "<properties>\n"
                        + "</properties>");
            } catch (IOException exception) {
                log.error("Can't create preferences file.", exception);
                //ignore
            }
        }
        System.setProperty("by.radchuk.task.util.prefs.file", prefsPath.toString());
    }

    public void init(ServletContext context) throws IOException {
        for (var pref : PreferencesKeys.values()) {
            String contextParam = context.getInitParameter(pref.key);
            if (contextParam != null) {
                Preferences.userRoot().node(pref.nodeName).put(pref.key, contextParam);
            }
        }
    }


    /**
     * Preferences keys enum.
     * Stores special string keys for
     * {@link by.radchuk.task.util.prefs.FilePreferences} class.
     */
    @AllArgsConstructor
    private enum PreferencesKeys {
        /**
         * Database pool size property.
         * Used to set max pool size.
         * @see by.radchuk.task.dao.framework.FixedConnectionPool
         * @see by.radchuk.task.dao.framework.H2Manger
         */
        DB_POOL_SIZE("db_pool_size", nodeName(H2Manger.class)),
        /**
         * Database run option.
         * init -> run table creation;
         * test -> run table creation + test data init.
         */
        DB_RUN_OPTION("db_run_option", nodeName(H2Manger.class)),
        /**
         * Package to scan for service classes.
         * {@link WebTaskContainerImpl} class
         * loads service classes in a special map on startup.
         * All client requests dispatched to these classes.
         */
        CT_PACKAGE_TO_SCAN("ct_scan_package", nodeName(WebTaskContainerImpl.class)),
        /**
         * Path to public resources, which can be downloaded/
         * viewed by users.
         */
        PUBlIC_RESOURCES_PATH("public_resources_path", "app");
        /**
         * Preferences key name.
         */
        private String key;
        private String nodeName;

        /**
         * Returns the absolute path name of the node corresponding to the package
         * of the specified object.
         *
         * @throws IllegalArgumentException if the package has node preferences
         *         node associated with it.
         */
        private static String nodeName(Class<?> c) {
            if (c.isArray())
                throw new IllegalArgumentException(
                        "Arrays have no associated preferences node.");
            String className = c.getName();
            int pkgEndIndex = className.lastIndexOf('.');
            if (pkgEndIndex < 0)
                return "/<unnamed>";
            String packageName = className.substring(0, pkgEndIndex);
            return "/" + packageName.replace('.', '/');
        }
    }
}
