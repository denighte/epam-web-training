package by.radchuk.task.context;

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
     * Queries files directory.
     * {@link by.radchuk.task.dao.framework.Queries} class
     * loads these files in a special map on startup.
     */
    DB_QUERIES_DIR("db_queries_dir"),
    /**
     * Package to scan for service classes.
     * {@link by.radchuk.task.controller.WebServiceMap} class
     * loads service classes in a special map on startup.
     * All client requests dispatched to these classes.
     */
    CT_PACKAGE_TO_SCAN("ct_scan_package");
    /**
     * Preferences key name.
     */
    @Getter
    private String value;
}
