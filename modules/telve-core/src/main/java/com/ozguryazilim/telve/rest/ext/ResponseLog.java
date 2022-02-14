package com.ozguryazilim.telve.rest.ext;

import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;

public class ResponseLog {
    private int status;
    private MultivaluedMap<String, String> headers;
    private Object entity;

    public ResponseLog() { }

    public ResponseLog(int status, MultivaluedMap<String, String> headers, Object entity) {
        this.status = status;
        this.headers = headers;
        this.entity = entity;
    }

    public ResponseLog(ContainerResponseContext responseContext) {
        this.status = responseContext.getStatus();
        this.headers = responseContext.getStringHeaders();
        this.entity = responseContext.getEntity();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public MultivaluedMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(MultivaluedMap<String, String> headers) {
        this.headers = headers;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

}
