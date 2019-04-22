package by.radchuk.task.dao.framework;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.prefs.Preferences;

/**
 * A map for sql application sql queries.
 * It loads queries from the specified folder.
 * Singleton.
 *
 * @author Dimtry Radchuk
 */
//TODO: FIX QUERIES LOADING
@Slf4j
public final class Queries {
    /**
     * Queries <code>Preferences</code> object.
     * contains directory path, where sql queries are stored.
     */
    private static final Preferences QUERIES_PREFERENCES
            = Preferences.userNodeForPackage(Queries.class);
    /**
     * Path to the folder with queries preferences key.
     */
    private static final String QUERIES_DIRECTORY = "db_queries_dir";
    /**
     * Default path to the folder with queries.
     */
    private static final String QUERIES_DEFAULT_DIRECTORY = "sql/query";
    /**
     * Singleton instance.
     */
    private static final Queries INSTANCE = new Queries();

    /**
     * Singleton get instance method.
     * @return <code>Queries</code> instance.
     */
    public static Queries getInstance() {
        return INSTANCE;
    }

    /**
     * hash map with queries.
     */
    private Map<String, String> queries;

    /**
     * Default constructor.
     * scans the specified in <code>Preferences</code> object
     * folder for .property files.
     * Loads them into memory as <code>HashMap</code> object.
     */
    @SneakyThrows(UnsupportedEncodingException.class)
    private Queries() {
        queries = new HashMap<>();
        String encodedPath = this.getClass().getClassLoader()
                                 .getResource("").getPath();
        String fullPath = URLDecoder.decode(encodedPath, "UTF-8")
                          + QUERIES_PREFERENCES.get(QUERIES_DIRECTORY,
                                                    QUERIES_DEFAULT_DIRECTORY);
        try {
            Files.list(Paths.get(fullPath)).forEach(file -> {
                Properties properties = new Properties();
                String filename = file.getFileName().toString();
                filename = filename.substring(0, filename.indexOf('.'));
                try {
                    properties.load(Files.newInputStream(file));
                } catch (IOException exception) {
                    log.error("Failed to load the queries file!", exception);
                    return;
                }
                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    queries.put(filename + ':' + entry.getKey(),
                                entry.getValue().toString());
                }
            });
        } catch (IOException exception) {
            log.error("Failed to scan the directory!", exception);
        }
    }

    /**
     * Get query object.
     * @param key query filename + ':' + query name in the property file.
     *            for example: 'users:getAll'
     * @return query string
     */
    public String getQuery(final String key) {
        return queries.get(key);
    }
}
