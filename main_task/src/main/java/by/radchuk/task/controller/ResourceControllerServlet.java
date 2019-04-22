package by.radchuk.task.controller;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
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
//        AsyncContext asyncContext = request.startAsync();
//        asyncContext.start(new Runnable() {
//            @Override
//            public void run() {
//                try {
//
//                }
//                String resourcePath = request.getRequestURI().substring(10);
//                response.setContentType("application/octet-stream");
//                InputStream resource = getServletContext().getResourceAsStream(resourcePath);
//                OutputStream output = response.getOutputStream();
//                if (resource == null) {
//                    response.setStatus(404);
//                    return;
//                }
//                try (
//                        ReadableByteChannel inputChannel = Channels.newChannel(resource);
//                        WritableByteChannel outputChannel = Channels.newChannel(output);
//                ) {
//                    ByteBuffer buffer = ByteBuffer.allocateDirect(10240);
//                    long size = 0;
//                    while (inputChannel.read(buffer) != -1) {
//                        buffer.flip();
//                        size += outputChannel.write(buffer);
//                        buffer.clear();
//                    }
//                }
//            }
//        });
    }
}
