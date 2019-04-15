package by.radchuk.task.dao.framework;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class Queries {
    private static final Path dir = Paths.get("src/main/resources/sql/query");
    private static final Queries INSTANCE = new Queries(dir);
    public static Queries getInstance() {
        return INSTANCE;
    }

    //"src/main/resources/sql/query/";
    private Map<String, String> queries;

    private Queries(Path dir){
        queries = new HashMap<>();
        try {
            Files.list(dir).forEach(file -> {
                Properties properties = new Properties();
                String filename = file.getFileName().toString();
                filename = filename.substring(0, filename.indexOf('.'));
                try {
                    properties.load(Files.newInputStream(file));
                } catch (IOException exception) {
                    log.error("Failed to load the queries file!", exception);
                    return;
                }
                for(Map.Entry<Object, Object> entry : properties.entrySet()) {
                    queries.put(filename + ':' + entry.getKey(), entry.getValue().toString());
                }
            });
        } catch (IOException exception) {
            log.error("Failed to scan the directory!", exception);
        }
    }

    public Map<String, String> getConfiguration() {
        return queries;
    }


    public String getQuery(String key) {
        return queries.get(key);
    }
}
