package by.radchuk.task.model;

import com.google.gson.annotations.Expose;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private int id;
    private String name;
    @Expose(deserialize = false)
    private String password;
}

