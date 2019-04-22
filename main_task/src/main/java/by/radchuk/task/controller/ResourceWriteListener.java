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
    private ReadableByteChannel inputChannel;// = Channels.newChannel(resource);
    private WritableByteChannel outputChannel;
    private ServletOutputStream stream;
    private ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
    private AsyncContext context;

    public ResourceWriteListener(InputStream resource, ServletOutputStream output) {
        try {
            inputChannel = Channels.newChannel(resource);
            outputChannel = Channels.newChannel(output);
            stream = context.getResponse().getOutputStream();
        } catch (Throwable exception) {
            onError(exception);
        }
    }

    @Override
    public void onWritePossible() throws IOException {
        while (inputChannel.read(buffer) != -1 && stream.isReady()) {
            buffer.flip();
            outputChannel.write(buffer);
            buffer.clear();
        }
    }

    @Override
    public void onError(Throwable t) {
        context.complete();
        log.error("Error during resource serving.", t);
    }
}
