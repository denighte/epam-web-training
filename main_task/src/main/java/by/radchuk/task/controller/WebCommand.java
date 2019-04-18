package by.radchuk.task.controller;

import javax.servlet.AsyncContext;

public abstract class WebCommand implements Runnable {
    protected AsyncContext context;
    public WebCommand(AsyncContext ac) {
        context = ac;
    }
}
