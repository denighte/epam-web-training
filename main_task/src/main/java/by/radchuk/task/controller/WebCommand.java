package by.radchuk.task.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public abstract class WebCommand implements Runnable {
    protected AsyncContext context;
    protected ServiceTask task;
    protected AsyncListener errorListener;
    @Getter @Setter protected CommandType type;

    protected WebCommand(AsyncContext asyncContext,
                         ServiceTask serviceTask) {
        context = asyncContext;
        task = serviceTask;
    }

    void addListener(AsyncListener listener) {
        errorListener = listener;
    }

    @Override
    public void run() {
        HttpServletRequest request = (HttpServletRequest)context.getRequest();
        HttpServletResponse response = (HttpServletResponse)context.getResponse();
        //header should be set by ServiceTask;
        try {
            String data = task.execute(request, response);
            //could be non blocking
            response.getWriter().write(data);
        } catch (Throwable throwable) {
            try {
                errorListener.onError(new AsyncEvent(context, throwable));
            } catch (IOException exception) {
                log.error("AsyncListener failed.", exception);
            }
        } finally {
            context.complete();
        }
    }
}
