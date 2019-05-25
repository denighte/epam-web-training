package by.radchuk.task.model;

import com.google.gson.annotations.Expose;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhotoPost {
    @Expose(deserialize = false)
    private int id;
    private String description;
    @Expose(deserialize = false)
    private int userId;
    private String creationDate;
    @Expose(deserialize = false)
    private String src;
}
