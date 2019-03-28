package by.radchuk.task4.writer;

import by.radchuk.task4.model.ResponseStatus;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class ResponseWriter {
    private HttpServletResponse response;

    public void write(ResponseStatus status, Object object) throws IOException {
        Gson gson = new Gson();
        switch (status) {
            case OK:
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().write(gson.toJson(object));
                break;
            case ERROR:
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().write(gson.toJson(object));
                break;
        }
        return;
    }
}
