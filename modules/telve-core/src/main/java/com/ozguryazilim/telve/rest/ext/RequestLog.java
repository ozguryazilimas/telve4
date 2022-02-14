package com.ozguryazilim.telve.rest.ext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CloseShieldInputStream;

import javax.persistence.criteria.CriteriaBuilder;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class RequestLog {
    private String path;
    private MultivaluedMap<String, String> headers;
    private String entity;

    public RequestLog() { }

    public RequestLog(String path, MultivaluedMap<String, String> headers, String entity) {
        this.path = path;
        this.headers = headers;
        this.entity = entity;
    }

    public RequestLog(ContainerRequestContext requestContext, byte[] entityByteArray) throws IOException {
        this.path = requestContext.getUriInfo().getPath();
        this.headers = requestContext.getHeaders();
        this.entity = new String(entityByteArray, StandardCharsets.UTF_8);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public MultivaluedMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(MultivaluedMap<String, String> headers) {
        this.headers = headers;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }
}
