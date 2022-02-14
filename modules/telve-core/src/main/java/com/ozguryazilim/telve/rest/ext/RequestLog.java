package com.ozguryazilim.telve.rest.ext;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;

public class RequestLog {
    private String path;
    private MultivaluedMap<String, String> headers;
    private Request request;

    public RequestLog() { }

    public RequestLog(String path, MultivaluedMap<String, String> headers, Request request) {
        this.path = path;
        this.headers = headers;
        this.request = request;
    }

    public RequestLog(ContainerRequestContext requestContext) {
        this.path = requestContext.getUriInfo().getPath();
        this.headers = requestContext.getHeaders();
        this.request = requestContext.getRequest();
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

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
