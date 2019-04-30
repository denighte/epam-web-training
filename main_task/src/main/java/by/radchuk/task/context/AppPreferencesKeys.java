package by.radchuk.task.context;

import by.radchuk.task.controller.WebTaskMap;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Preferences keys enum.
 * Stores special string keys for
 * {@link by.radchuk.task.util.prefs.FilePreferences} class.
 */
@AllArgsConstructor
public enum AppPreferencesKeys {
    /**
     * Database pool size property.
     * Used to set max pool size.
     * @see by.radchuk.task.dao.framework.FixedConnectionPool
     * @see by.radchuk.task.dao.framework.H2Manger
     */
    DB_POOL_SIZE("db_pool_size"),
    /**
     * Database run option.
     */
    DB_RUN_OPTION("db_run_option"),
    /**
     * Database init file.
     * Used to create tables,
     * init stored procedures, ect..
     */
    DB_INIT_FILE("db_init_file"),
    /**
     * Database test file.
     * Used to init test data for tables.
     */
    DB_TEST_FILE("db_test_file"),
    /**
     * Package to scan for service classes.
     * {@link WebTaskMap} class
     * loads service classes in a special map on startup.
     * All client requests dispatched to these classes.
     */
    CT_PACKAGE_TO_SCAN("ct_scan_package"),
    /**
     * Path to public resources, which can be downloaded/
     * viewed by users.
     */
    PUBlIC_RESOURCES_PATH("public_resources_path"),
    /**
     * Path to application private resources.
     */
    APPLICATION_RESOURCES_PATH("app_resources_path");
    /**
     * Preferences key name.
     */
    @Getter
    private String value;
}
