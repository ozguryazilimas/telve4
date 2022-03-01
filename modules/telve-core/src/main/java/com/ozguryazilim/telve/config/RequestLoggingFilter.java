package com.ozguryazilim.telve.config;

import com.google.gson.Gson;
import com.ozguryazilim.telve.rest.ext.Logged;
import com.ozguryazilim.telve.rest.ext.RequestLog;
import com.ozguryazilim.telve.rest.ext.ResponseLog;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Logged
@Provider
public class RequestLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private Logger LOG = LoggerFactory.getLogger(RequestLoggingFilter.class);

    private Gson gson = new Gson();

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        LOG.info("Response: " + gson.toJson(new ResponseLog(responseContext)));

    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        byte[] entityByteArray = IOUtils.toByteArray(requestContext.getEntityStream());
        LOG.info("Request: " + gson.toJson(new RequestLog(requestContext, entityByteArray)));
        requestContext.setEntityStream(new ByteArrayInputStream(entityByteArray));
    }
}
