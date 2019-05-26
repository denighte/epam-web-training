package by.radchuk.task.model;

import lombok.Data;

@Data
public class FilterConfig {
    private int top;
    private int skip;
    private String date;
    private String user;
}
