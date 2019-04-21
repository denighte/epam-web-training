package by.radchuk.task.controller;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {
    int status;
    String data;
}
