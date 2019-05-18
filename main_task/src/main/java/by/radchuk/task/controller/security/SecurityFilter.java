package by.radchuk.task.controller.security;

public interface SecurityFilter {
    String AUTHORIZATION_HEADER_NAME = "Authorization";
    boolean filter(String token, int securityLevel);
    String encode(Object payload);
    Object decode(String token);
}
