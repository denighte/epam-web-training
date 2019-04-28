package by.radchuk.task.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.io.*;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A file servlet.
 * Supports:
 * * resume of downloads
 * * client-side caching
 * * GZIP of text content.
 * * Etag processing.
 * @author Dmitry Radchuk
 */
@Slf4j
@WebServlet(value = "/resources/*", loadOnStartup = 1, asyncSupported = true)
public class FileServlet extends HttpServlet {

    // Constants --------------------------------------------------------------

    /**
     * Default buffer size.
     */
    // ..bytes = 5KB.
    private static final int DEFAULT_BUFFER_SIZE = 5120;
    /**
     * Default resource expire time.
     */
    // ..ms = 1 week.
    private static final long DEFAULT_EXPIRE_TIME = 604800000L;
    /**
     * multipart boundary type.
     */
    private static final String MULTIPART_BOUNDARY = "MULTIPART_BYTERANGES";

    // Properties --------------------------------------------------------------

    /**
     * path to resources.
     */
    private String resourcesPath;

    // Actions -----------------------------------------------------------------

    /**
     * Initialize the servlet.
     * @see HttpServlet#init().
     * @throws ServletException if an exception occurs that
     *                 interrupts the servlet's normal operation
     */
    public void init() throws ServletException {

        // Get base path (path to get all resources from) as init parameter.
        this.resourcesPath = getInitParameter("resourcesPath");
        //temp
        this.resourcesPath = getServletContext().getRealPath("");

        // Validate base path.
        if (this.resourcesPath == null) {
            throw new ServletException("FileServlet init param "
                                     + "'resourcesPath' is required.");
        } else {
            Path path = Paths.get(this.resourcesPath);
            if (!Files.exists(path)) {
                throw new ServletException("FileServlet init param "
                                         + "'resourcesPath' value '"
                                         + this.resourcesPath
                                         + "' does actually not exist "
                                         + "in file system.");
            } else if (!Files.isDirectory(path)) {
                throw new ServletException("FileServlet init param "
                                         + "'resourcesPath' value '"
                                         + this.resourcesPath
                                         + "' is actually not a directory "
                                         + "in file system.");
            } else if (!Files.isReadable(path)) {
                throw new ServletException("FileServlet init param "
                                         + "'resourcesPath' value '"
                                         + this.resourcesPath
                                         + "' is actually not readable "
                                         + "in file system.");
            }
        }
    }

    /**
     * Process HEAD request. This returns the same headers as GET request,
     * but without content.
     * @param request   the request object that is passed to the servlet
     *
     * @param response  the response object that the servlet
     *                  uses to return the headers to the client
     *
     * @see HttpServlet#doHead(HttpServletRequest, HttpServletResponse).
     * @throws IOException in case I/O exceptions.
     */
    protected void doHead(final HttpServletRequest request,
                          final HttpServletResponse response)
            throws IOException {
        // Process request without content.
        processRequest(request, response, false);
    }

    /**
     * Process GET request.
     * @param request   the request object that is passed to the servlet
     *
     * @param response  the response object that the servlet
     *                  uses to return the headers to the client
     * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse).
     * @throws IOException in case I/O exceptions.
     */
    protected void doGet(final HttpServletRequest request,
                         final HttpServletResponse response)
            throws IOException {
        // Process request with content.
        processRequest(request, response, true);
    }

    /**
     * Process the actual request.
     * @param request The request to be processed.
     * @param response The response to be created.
     * @param content Whether the request body should be written
     *                (GET) or not (HEAD).
     * @throws IOException If something fails at I/O level.
     */
    private void processRequest(final HttpServletRequest request,
                                final HttpServletResponse response,
                                final boolean content)
            throws IOException {

        // Validating the requested file ---------------------------------------
        // Getting requested file by URI.
        String requestedFile = request.getRequestURI();

        // Checking if file is actually supplied to the request URL.
        if (requestedFile == null) {
            // sending 404 (probably ignore?)
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // URL-decoding the file name (might contain spaces and on).
        Path file = Paths.get(resourcesPath,
                              URLDecoder.decode(requestedFile, "UTF-8"));

        // Checking if file actually exists in filesystem.
        if (!Files.exists(file)) {
            // sending 404 error (probably ignore?)
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Preparing some variables.
        String fileName = file.getFileName().toString();
        long length = Files.size(file);
        long lastModified = Files.getLastModifiedTime(file).toMillis();
        //The ETag is an unique identifier of the file.
        String eTag = fileName + "_" + length + "_" + lastModified;
        long expires = System.currentTimeMillis() + DEFAULT_EXPIRE_TIME;

        // Validating request headers for caching ------------------------------

        // If-None-Match header should contain "*" or ETag.
        // If so, then returning 304.
        String ifNoneMatch = request.getHeader("If-None-Match");
        if (ifNoneMatch != null && HeaderUtils.matches(ifNoneMatch, eTag)) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            // Required in 304.
            response.setHeader("ETag", eTag);
            // Postpone cache with 1 week.
            response.setDateHeader("Expires", expires);
            return;
        }

        // If-Modified-Since header should be greater than LastModified.
        // If so, then returning 304.
        // This header is ignored if any If-None-Match header is specified.
        long ifModifiedSince = request.getDateHeader("If-Modified-Since");
        if (ifNoneMatch == null
            && ifModifiedSince != -1
            && ifModifiedSince > lastModified) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            // Required in 304.
            response.setHeader("ETag", eTag);
            // Postpone cache with 1 week.
            response.setDateHeader("Expires", expires);
            return;
        }

        // Validating request headers for resume -------------------------------

        // If-Match header should contain "*" or ETag. If not, then return 412.
        String ifMatch = request.getHeader("If-Match");
        if (ifMatch != null && !HeaderUtils.matches(ifMatch, eTag)) {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return;
        }

        // If-Unmodified-Since header should be greater than LastModified.
        // If not, then return 412.
        long ifUnmodifiedSince = request.getDateHeader("If-Unmodified-Since");
        if (ifUnmodifiedSince != -1 && ifUnmodifiedSince <= lastModified) {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return;
        }

        // Validating and process range ----------------------------------------

        // Preparing some variables.
        // The full Range represents the complete file.
        Range full = new Range(0, length - 1, length);
        List<Range> ranges = new ArrayList<>();

        // Validating and processing Range and If-Range headers.
        String range = request.getHeader("Range");
        if (range != null) {

            // Range header should match format "bytes=n-n,n-n,n-n...".
            // If not, then returning 416.
            if (!range.matches("^bytes=\\d*-\\d*(,\\d*-\\d*)*$")) {
                // Required in 416.
                response.setHeader("Content-Range", "bytes */" + length);
                response.sendError(HttpServletResponse
                                   .SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                return;
            }

            // If-Range header should either match ETag
            // or be greater then LastModified.
            // If not, then return full file.
            String ifRange = request.getHeader("If-Range");
            if (ifRange != null && !ifRange.equals(eTag)) {
                try {
                    // Throws <code>IllegalArgumentException</code> if invalid.
                    long ifRangeTime = request.getDateHeader("If-Range");
                    if (ifRangeTime != -1 && ifRangeTime < lastModified) {
                        ranges.add(full);
                    }
                } catch (IllegalArgumentException ignore) {
                    ranges.add(full);
                }
            }

            // If any valid If-Range header is present,
            // then process each part of byte range.
            if (ranges.isEmpty()) {
                try {
                    ranges.addAll(HeaderUtils.parseRanges(range, length));
                } catch (HeaderException exception) {
                    //if Range is not syntactically valid returning 416.
                    // Required in 416.
                    response.setHeader("Content-Range", "bytes */" + length);
                    response.sendError(HttpServletResponse
                                       .SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                    return;
                }
            }
        }

        // Preparing and initialize response -----------------------------------

        // Get content type by file name and set default
        // GZIP support and content disposition.
        String contentType = getServletContext().getMimeType(fileName);
        boolean acceptsGzip = false;
        String disposition = "inline";

        // If content type is unknown, then set the default value.
        // !!!NOTE: To add new content types,
        //    can add new mime-mapping entry in web.xml.
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        // If content type is text, then determining whether
        // GZIP content encoding is supported by.
        // The browser and expand content type with
        // the one and right character encoding.
        if (contentType.startsWith("text")) {
            String acceptEncoding = request.getHeader("Accept-Encoding");
            acceptsGzip = acceptEncoding != null
                        && HeaderUtils.acceptsEncoding(acceptEncoding, "gzip");
            contentType += ";charset=UTF-8";
        } else if (!contentType.startsWith("image")) {
            // Else, expected for images, determining content disposition.
            // If content type is supported by the browser, then set to inline,
            // else attachment which will pop a 'save as' dialogue.
            String accept = request.getHeader("Accept");
            if (accept != null && HeaderUtils.accepts(accept, contentType)) {
                disposition = "inline";
            } else {
                disposition = "attachment";
            }
        }

        // Initializing response.
        response.reset();
        response.setBufferSize(DEFAULT_BUFFER_SIZE);
        response.setHeader("Content-Disposition",
                     disposition + ";filename=\"" + fileName + "\"");
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("ETag", eTag);
        response.setDateHeader("Last-Modified", lastModified);
        response.setDateHeader("Expires", expires);

        // Sending requested file (part(s)/file) to client ---------------------

        //If it was doPut, committing response object.
        if (!content) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        //if ranges is empty, than a full file was requested.
        if (ranges.isEmpty()) {
            ranges.add(full);
        }

        //determining if a full file / part(s) was request.
        //setting fitting headers.
        if (ranges.get(0) == full) {
            //return full file.
            Range r = ranges.get(0);
            response.setContentType(contentType);
            if (!acceptsGzip) {
                response.setHeader("Content-Length", String.valueOf(r.length));
            }
        } else if (ranges.size() == 1) {
            //Return single part of file.
            Range r = ranges.get(0);
            response.setContentType(contentType);
            response.setHeader("Content-Range",
                    "bytes " + r.start + "-" + r.end + "/" + r.total);
            response.setHeader("Content-Length", String.valueOf(r.length));
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.
        } else {
            // Return multiple parts of file.
            response.setContentType("multipart/byteranges; boundary="
                                    + MULTIPART_BOUNDARY);
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.
        }

        OutputStream output = response.getOutputStream();

        //Processing gzip as blocking output.
        //Because we gzip only text/* files, ad we gzip on-the-fly.
        //So there is no need for async processing.
        //(And, also it won't work with on-the-fly encoding)
        if (ranges.get(0) == full && acceptsGzip) {
            //return gzip
            response.setHeader("Content-Encoding", "gzip");
            //tomcat sets it by default.
            //response.setHeader("Transfer-Encoding", "chunked");
            InputStream input = null;
            try {
                input = Files.newInputStream(file);
                output = new GZIPOutputStream(output);
                blockingCopy(input, output);
            } finally {
                output.flush();
                closeQuietly(input);
                closeQuietly(output);
            }
            return;
        }
        
        //starting async processing.
        AsyncContext context = request.startAsync();
        SeekableByteChannel channelInput = null;
        try {
            //getting new channel for file read.
            channelInput = Files.newByteChannel(file);
            context.getResponse()
                   .getOutputStream()
                   .setWriteListener(
                           new FileWriteListener(context,
                                                 channelInput,
                                                 output,
                                                 contentType,
                                                 ranges)
                   );
        } catch (Exception exception) {
            context.complete();
            closeQuietly(channelInput);
            closeQuietly(output);
        }

    }

    // Helper methods ----------------------------------------------------------

    protected void closeQuietly(AutoCloseable resource) {
        try {
            if (resource != null) {
                resource.close();
            }
        } catch (Exception exception) {
            //ignore.
            //nothing we can do.
        }
    }

    protected void blockingCopy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int read;
        while ((read = input.read(buffer)) > 0) {
            output.write(buffer, 0, read);
        }
    }

    // Inner classes -----------------------------------------------------------

    /**
     * Header utility class.
     * Provides methods for working with http headers.
     */
    protected static class HeaderUtils {
        /**
         * Returns true if the given accept header accepts the given value.
         * @param headerData The Accept-Encoding header values.
         * @param toAccept The value to be accepted.
         * @return True if the given Accept-Encoding header
         * accepts the given value false otherwise.
         */
        static boolean acceptsEncoding(final String headerData,
                                       final String toAccept) {
            for (var value : headerData.split(",\\s?")) {
                if (value.startsWith(toAccept)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Returns true if the given accept header accepts the given value.
         * @param headerData The Accept header raw value.
         * @param toAccept The value to be accepted.
         * @return True if the given Accept header
         * accepts the given value false otherwise.
         */
        static boolean accepts(final String headerData,
                               final String toAccept) {
            for (var value : headerData.split(",\\s?")) {
                if (value.equals(toAccept)) {
                    return true;
                }
                if (value.endsWith("/*")) {
                    if (toAccept.endsWith(value.substring(0,
                                                         value.length() - 2))) {
                        return true;
                    }
                }
                if (value.equals("*/*")) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Returns true if the given match header matches the given value.
         * @param headerData The match header raw value.
         * @param toMatch The value to be matched.
         * @return True if the given match header
         * matches the given value false otherwise.
         */
        static boolean matches(final String headerData,
                               final String toMatch) {
            for (var value : headerData.split(",\\s?")) {
                if (value.equals(toMatch) || value.equals("*")) {
                    return true;
                }
            }
            return false;
        }

        /**
         * utility method.
         * Returns a substring of the given string value
         * from the given begin index to the given end
         * index as a long. If the substring is empty,
         * then -1 will be returned.
         * @param value The string value to return a substring as long for.
         * @param beginIndex start of the substring to be returned as long.
         * @param endIndex end of the substring to be returned as long.
         * @return A substring of the given string value as long
         * or -1 if substring is empty.
         */
        static long subLong(final String value,
                            final int beginIndex,
                            final int endIndex) {
            String substring = value.substring(beginIndex, endIndex);
            if (substring.length() > 0) {
                return Long.parseLong(substring);
            }
            return -1;
        }

        /**
         * parses Range header values.
         * @param headerData Range header data.
         * @param fileSize resource file size.
         * @return list of <code>Range</code> objects.
         * @throws HeaderException if Range is syntactically invalid.
         *
         * Assuming a file with length of 100,
         * the following examples returns bytes at:
         * 50-80 (50 to 80),
         * 40- (40 to length=100),
         * -20 (length-20=80 to length=100).
         * @see Range
         */
        static List<Range> parseRanges(final String headerData,
                                       final long fileSize)
                                            throws HeaderException {
            List<Range> ranges = new ArrayList<>();
            for (String part
                 : headerData.substring("bytes ".length()).split(",")) {
                long start = subLong(part, 0, part.indexOf("-"));
                long end = subLong(part,
                                   part.indexOf("-") + 1, part.length());

                if (start == -1) {
                    start = fileSize - end;
                    end = fileSize - 1;
                } else if (end == -1 || end > fileSize - 1) {
                    end = fileSize - 1;
                }

                // Checking if Range is syntactically valid.
                if (start > end) {
                    throw new HeaderException();
                }

                // Add range.
                ranges.add(new Range(start, end, fileSize));
            }
            return ranges;
        }
    }

    /**
     * Header exception class.
     */
    protected static class HeaderException extends Exception {
        /**
         * Constructs a new exception with <code>null</code>
         * as its detail message.
         * The cause is not initialized,
         * and may subsequently be initialized by a
         * call to {@link #initCause}.
         */
        public HeaderException() {
            super();
        }

    }

    /**
     * Callback notification mechanism that signals it's possible
     * to write content without blocking.
     */
    protected static class FileWriteListener implements WriteListener {
        /**
         * Resource byte channel.
         */
        private SeekableByteChannel input;
        /**
         * <code>ServletOutputStream</code> object instance
         * of the given async context.
         * Used to determine if write operation is available.
         */
        private ServletOutputStream servletStream;
        /**
         * Content MIME type.
         */
        private String content;
        /**
         * <code>ServletOutputStream</code> object
         * wrapped by <code>WritableByteChannel</code>.
         */
        private WritableByteChannel output;
        /**
         * Direct <code>ByteBuffer</code> object.
         * Used as buffer between <code>input</code> and <code>output</code>.
         */
        private ByteBuffer buffer
                = ByteBuffer.allocateDirect(DEFAULT_BUFFER_SIZE);
        /**
         * <code>AsyncContext</code> object of the request.
         */
        private AsyncContext asyncContext;
        /**
         * List of ranges to be written.
         */
        private List<Range> ranges;
        /**
         * Current range written to the output.
         */
        private Range currentRange;
        /**
         * Current range position in <code>ranges</code> list.
         */
        private int i;
        /**
         * the number of bytes remaining to write.
         */
        private long toWrite;
        /**
         * indicates that <code>buffer</code> object range was reduced.
         * This is used to indicate that the current chunk is almost written.
         */
        private boolean wasLimited;

        /**
         * Write listener constructor.
         * @param context async context object.
         * @param resource The input to copy to output.
         * @param stream The output.
         * @param contentType MIME type of the content.
         * @param rangeList list of ranges to be written.
         */
        FileWriteListener(final AsyncContext context,
                          final SeekableByteChannel resource,
                          final OutputStream stream,
                          final String contentType,
                          final List<Range> rangeList) {
            try {
                input = resource;
                output = Channels.newChannel(stream);
                content = contentType;
                servletStream = context.getResponse().getOutputStream();
                asyncContext = context;
                ranges = rangeList;
                i = 0;
                buffer.flip();
                nextRange();
            } catch (Throwable exception) {
                onError(exception);
            }
        }

        /**
         * When an instance of the WriteListener is registered
         * with a <code>ServletOutputStream</code>,
         * this method will be invoked by the container
         * the first time when it is possible to write data.
         * Subsequently the container will invoke this method if and only
         * if {@link javax.servlet.ServletOutputStream#isReady()} method
         * has been called and has returned <code>false</code>.
         *
         * @throws IOException if an I/O related error
         *                     has occurred during processing
         */
        @Override
        public void onWritePossible() throws IOException {
            //number of read bytes from the input.
            int read = -1;

            //if buffer is not empty, writing it the output
            while (buffer.hasRemaining() && servletStream.isReady()) {
                toWrite -= output.write(buffer);
            }

            //if all bytes was successfully written and the previous
            //onWritePossible call almost wrote the chunk (@see wasLimited)
            //than next range should be processed.
            if (!buffer.hasRemaining() && wasLimited) {
                nextRange();
                wasLimited = false;
            }

            //if servlet output stream is not ready, than
            //we should wait until new onWritePossible() call.
            if (!servletStream.isReady()) {
                return;
            }

            //clearing the buffer
            //(The position is set to zero, the limit is set to the capacity
            //of the buffer)
            buffer.clear();
            //trying to read the data.
            while (servletStream.isReady() && (read = input.read(buffer)) > 0) {
                //flipping the buffer, if data was successfully written.
                //@see ByteBuffer#filp()
                buffer.flip();
                // if the remaining number of bytes is bigger
                // than read number of bytes
                // than we just writing them to the output.
                // if not, we should indicate that chunk is almost written.
                if ((toWrite - read) > 0) {
                    toWrite -= output.write(buffer);
                    if (!buffer.hasRemaining() && servletStream.isReady()) {
                        buffer.clear();
                    }
                } else {
                    buffer.limit((int) toWrite);
                    toWrite -= output.write(buffer);
                    wasLimited = true;
                    break;
                }
            }

            //If there are no remaining bytes and no next range
            //than all data has been written to the output.
            if (toWrite == 0 && !nextRange()) {
                if (ranges.size() > 1) {
                    // End with multipart boundary.
                    // as specified by MDN web docs.
                    servletStream.println();
                    servletStream.println("--" + MULTIPART_BOUNDARY + "--");
                }
                //completing async
                complete();
            }  else if (read < 1) {
                //completing async if there is nothing to read.
                complete();
            } else if (servletStream.isReady()) {
                //if servlet output stream is ready, than manually calling
                //onWritePosible function.
                onWritePossible();
            }
        }

        /**
         * Invoked when an error occurs writing data
         * using the non-blocking APIs.
         */
        @Override
        public void onError(final Throwable t) {
            complete();
            log.error("Error during resource serving.", t);
        }

        /**
         * initializes next <code>currentRange</code>.
         * @return tru if new range was initialized, false otherwise.
         * @throws IOException if an I/O related error
         *                     has occurred during processing
         */
        boolean nextRange() throws IOException {
            if (i == ranges.size()) {
                //complete();
                return false;
            }
            currentRange = ranges.get(i++);
            //if multipart/byteranges content type
            //splitting the ranges as specified by MDN web docs.
            if (ranges.size() > 1) {
                servletStream.println();
                servletStream.println("--" + MULTIPART_BOUNDARY);
                servletStream.println("Content-Type: " + content);
                servletStream.println("Content-Range: bytes "
                                      + currentRange.start
                                      + "-"
                                      + currentRange.end
                                      + "/"
                                      + currentRange.total);
            }
            toWrite = currentRange.length;
            input.position(currentRange.start);
            return true;
        }

        /**
         * completes the async request.
         */
        void complete() {
            try {
                input.close();
                output.close();
                servletStream.close();
            } catch (IOException exception) {
                log.error("Error during closing resource/output stream",
                          exception);
            } finally {
                asyncContext.complete();
            }
        }
    }

    /**
     * This class represents a byte range.
     */
    @Data
    protected static class Range {
        /**
         * byte range start.
         */
        private long start;
        /**
         * byte range end.
         */
        private long end;
        /**
         * byte range length.
         * (simply end - start + 1)
         */
        private long length;
        /**
         * total bytes of the resource.
         */
        private long total;

        /**
         * Construct a byte range.
         * @param rstart Start of the byte range.
         * @param rend End of the byte range.
         * @param rtotal Total length of the byte source.
         */
        Range(final long rstart,
              final long rend,
              final long rtotal) {
            start = rstart;
            end = rend;
            length = rend - rstart + 1;
            total = rtotal;
        }

    }
}

