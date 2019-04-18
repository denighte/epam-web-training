package by.radchuk.task.controller;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import java.io.IOException;

@Slf4j
public class ControllerListener implements AsyncListener {
    @Override
    public void onComplete(AsyncEvent event) throws IOException {
    }

    @Override
    public void onTimeout(AsyncEvent event) throws IOException {

    }

    @Override
    public void onError(AsyncEvent event) throws IOException {
        //TODO: dispatch to error page
        event.getAsyncContext().dispatch();
    }

    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {

    }
}
