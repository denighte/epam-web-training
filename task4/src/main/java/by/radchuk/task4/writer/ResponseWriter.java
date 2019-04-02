package by.radchuk.task4.writer;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Response writer class.
 * writes objects to response output stream.
 */
@Slf4j
@AllArgsConstructor
public class ResponseWriter {
    /**
     * Http response object.
     */
    private HttpServletResponse response;

    /**
     * writes object to response output stream.
     * @param status response status.
     * @param object object to write.
     * @throws IOException in case IO errors.
     */
    public void write(final ResponseStatus status,
                      final Object object)
                                throws IOException {
        log.debug("writing to response output stream ...");
        Gson gson = new Gson();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter()
                .write(gson.toJson(new ResponseObject(status, object)));
        log.debug("response output stream ready to flush.");
    }

    /**
     * Response object.
     * Used to wrap the data for easier frontend objects access.
     */
    @Value
    @AllArgsConstructor
    static private class ResponseObject {
        /**
         * Response status.
         */
        private ResponseStatus status;
        /**
         * object to write.
         */
        private Object object;
    }
}
