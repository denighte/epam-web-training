package by.radchuk.task.dao.framework;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * A map for sql application sql queries.
 * It loads queries from the specified folder.
 * Singleton.
 *
 * @author Dimtry Radchuk
 */
@Slf4j
public final class Queries {
    /**
     * Path to the folder with queries.
     */
    private static final String QUERIES_DIRECTORY = "sql/query";
    /**
     * Singleton instance.
     */
    private static final Queries INSTANCE = new Queries(QUERIES_DIRECTORY);

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
     * scans the specified folder for .property files.
     * Loads them into memory as <code>HashMap</code> object.
     * @param dir directory to scan.
     */
    @SneakyThrows(UnsupportedEncodingException.class)
    private Queries(final String dir) {
        queries = new HashMap<>();
        String encodedPath = this.getClass().getClassLoader().getResource("").getPath();
        String fullPath = URLDecoder.decode(encodedPath, "UTF-8");
        try {
            Files.list(Paths.get(fullPath + dir)).forEach(file -> {
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
