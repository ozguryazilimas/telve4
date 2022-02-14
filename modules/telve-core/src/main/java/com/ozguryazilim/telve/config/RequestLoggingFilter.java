package com.ozguryazilim.telve.config;

import com.google.gson.Gson;
import com.ozguryazilim.telve.rest.ext.Logged;
import com.ozguryazilim.telve.rest.ext.RequestLog;
import com.ozguryazilim.telve.rest.ext.ResponseLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Logged
@Provider
public class RequestLoggingFilter implements ContainerResponseFilter {

    private Logger LOG = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        Gson gson = new Gson();
        LOG.info(gson.toJson(new RequestLog(requestContext)));
        LOG.info(gson.toJson(new ResponseLog(responseContext)));

    }
}
