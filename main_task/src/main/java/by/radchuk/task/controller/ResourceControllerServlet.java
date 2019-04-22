package by.radchuk.task.controller;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

@Slf4j
@WebServlet(urlPatterns = {"/resources/*"}, asyncSupported = true)
public class ResourceControllerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
                        throws ServletException, IOException {
        String resourcePath = request.getRequestURI().substring(10);
        InputStream resource = getServletContext().getResourceAsStream(resourcePath);
        if (resource == null) {
            response.setStatus(404);
            return;
        }
        ServletOutputStream output = response.getOutputStream();
        AsyncContext asyncContext = request.startAsync();
        ((ServletOutputStream) output).setWriteListener(new ResourceWriteListener(resource, output));
    }
}
