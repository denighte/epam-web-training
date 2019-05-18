package by.radchuk.task.controller.impl;

import by.radchuk.task.controller.security.SecurityFilter;

//TODO: add implementation
public class SecurityFilterImpl implements SecurityFilter {
    @Override
    public boolean filter(String token, int securityLevel) {
        return false;
    }

    @Override
    public String encode(Object payload) {
        return null;
    }

    @Override
    public Object decode(String token) {
        return null;
    }
}
