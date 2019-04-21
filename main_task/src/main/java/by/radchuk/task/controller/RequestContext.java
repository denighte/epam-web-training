package by.radchuk.task.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AllArgsConstructor
public class RequestContext {
    @Getter
    public HttpServletRequest request;
    @Getter
    public HttpServletResponse response;
}
