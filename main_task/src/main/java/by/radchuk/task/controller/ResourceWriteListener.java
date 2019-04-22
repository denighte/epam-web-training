package by.radchuk.task.controller;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

@Slf4j
public class ResourceWriteListener implements WriteListener {
    private ReadableByteChannel inputChannel;
    private WritableByteChannel outputChannel;
    private ServletOutputStream stream;
    private ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
    private AsyncContext asyncContext;
    private long length;

    public ResourceWriteListener(AsyncContext context, InputStream resource, ServletOutputStream output) {
        try {
            inputChannel = Channels.newChannel(resource);
            outputChannel = Channels.newChannel(output);
            stream = context.getResponse().getOutputStream();
            asyncContext = context;
            length = 0;
        } catch (Throwable exception) {
            onError(exception);
        }
    }

    @Override
    public void onWritePossible() throws IOException {
        while ((length = inputChannel.read(buffer)) != -1 && stream.isReady()) {
            buffer.flip();
            length += outputChannel.write(buffer);
            buffer.clear();
        }
        if (length == -1) {
            asyncContext.complete();
        }
    }

    @Override
    public void onError(Throwable t) {
        asyncContext.complete();
        log.error("Error during resource serving.", t);
    }
}
