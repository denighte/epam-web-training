package by.radchuk.task.io;

import com.google.gson.Gson;

public class RestService {
    private Gson gson = new Gson();

    public <T> T getObject(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }


}
