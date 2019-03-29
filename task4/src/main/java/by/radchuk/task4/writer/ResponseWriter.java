package by.radchuk.task4.writer;

import by.radchuk.task4.model.ResponseStatus;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class ResponseWriter {
    private HttpServletResponse response;

    public void write(ResponseStatus status, Object object) throws IOException {
        log.debug("writing to response output stream ...");
        Gson gson = new Gson();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(gson.toJson(new ResponseObject(status, object)));
        log.debug("response output stream ready to flush.");
    }

    @Value
    @AllArgsConstructor
    static private class ResponseObject {
        private ResponseStatus status;
        private Object object;
    }
}
