package by.radchuk.task.controller;

import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Response {
    @Getter
    private int status = 200;
    @Getter
    private String encoding = "UTF-8";
    @Getter
    private String data;
    @Setter
    @Getter
    private String type;
    @Getter
    private List<Header> headers = new ArrayList<>();
    @Getter
    private List<Cookie> cookies = new ArrayList<>();
    @Getter
    private String dispatcherName;
    @Getter
    private String dispatchPath;

    public static Builder builder() {
        return new Builder();
    }

    @AllArgsConstructor
    @Data
    static class Header {
        private String key;
        private String value;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {
        private Response newResponse = new Response();
        private boolean isDataSet = false;
        private boolean isDispatched = false;

        public Builder status(int status) {
            newResponse.status = status;
            return this;
        }

        public Builder error(int status, String message) {
            if(isDataSet) {
                throw new IllegalStateException("Data has been already set!");
            }
            newResponse.type = ContentType.TEXT_PLAIN.getType();
            newResponse.status = status;
            newResponse.data = message;
            isDataSet = true;
            return this;
        }

        public Builder encoding(String encoding) {
            newResponse.encoding = encoding;
            return this;
        }

        public Builder type(String type) {
            newResponse.type = type;
            return this;
        }

        public Builder type(ContentType type) {
            newResponse.type = type.getType();
            return this;
        }

        public Builder header(String key, String value) {
            newResponse.headers.add(new Header(key, value));
            return this;
        }

        public Builder cookie(Cookie cookie) {
            newResponse.cookies.add(cookie);
            return this;
        }

        public Builder data(String data) {
            validate();
            newResponse.data = data;
            isDataSet = true;
            return this;
        }

        public Builder entity(Object entity) {
            validate();
            Gson gson = new Gson();
            newResponse.data = gson.toJson(entity);
            isDataSet = true;
            return this;
        }

        public Builder dispatch(String path) {
            validate();
            newResponse.dispatchPath = path;
            isDispatched = true;
            return this;
        }

        public Builder namedDispatch(String handlerName) {
            validate();
            newResponse.dispatcherName = handlerName;
            isDispatched = true;
            return this;
        }

        public Response build() {
            return newResponse;
        }

        private void validate() {
            if (isDataSet) {
                throw new IllegalStateException("Data already has been set!");
            }
            if (isDispatched) {
                throw new IllegalStateException("Response is already dispatched!");
            }
        }

    }
}
