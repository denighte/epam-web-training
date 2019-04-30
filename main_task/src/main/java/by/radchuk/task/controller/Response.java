package by.radchuk.task.controller;

import com.google.gson.Gson;
import lombok.*;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Response {
    @Getter(AccessLevel.PACKAGE)
    private int status = 200;
    @Getter(AccessLevel.PACKAGE)
    private String encoding = "UTF-8";
    @Getter(AccessLevel.PACKAGE)
    private String data;
    @Setter(AccessLevel.PACKAGE)
    @Getter(AccessLevel.PACKAGE)
    private String type;
    @Getter(AccessLevel.PACKAGE)
    private List<Header> headers = new ArrayList<>();
    @Getter(AccessLevel.PACKAGE)
    private List<Cookie> cookies = new ArrayList<>();

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

        public Builder status(int status) {
            newResponse.status = status;
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
            if(isDataSet) {
                throw new IllegalStateException("Data has been already set!");
            }
            newResponse.data = data;
            isDataSet = true;
            return this;
        }

        public Builder entity(Object entity) {
            if(isDataSet) {
                throw new IllegalStateException("Data has been already set!");
            }
            Gson gson = new Gson();
            newResponse.data = gson.toJson(entity);
            isDataSet = true;
            return this;
        }

        public Response build() {
            return newResponse;
        }

    }
}
