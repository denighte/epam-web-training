package by.radchuk.task.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ContentType {
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded"),
    MULTIPART_FORM_DATA("multipart/form-data"),
    APPLICATION_JSON("application/json"),
    APPLICATION_OCTET_STREAM("application/octet-stream"),
    TEXT_HTML("text/html"),
    TEXT_PLAIN("text/plain"),
    TEXT_XML("text/xml"),
    APPLICATION_SVG_XML("application/svg+xml"),
    APPLICATION_XML("application/xml"),
    APPLICATION_ATOM_XML("application/atom+xml"),
    APPLICATION_XHTML_XML("application/xhtml+xml"),
    WILDCARD("*/*");

    @Getter
    private String type;
}
