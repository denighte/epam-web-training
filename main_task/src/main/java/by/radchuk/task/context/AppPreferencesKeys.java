package by.radchuk.task.context;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum AppPreferencesKeys {
    DB_POOL_SIZE("db_pool_size"),
    DB_RUN_OPTION("db_run_option"),
    DB_INIT_FILE("db_init_file"),
    DB_TEST_FILE("db_test_file"),
    DB_QUERIES_DIR("db_queries_dir");
    @Getter
    private String value;
}
