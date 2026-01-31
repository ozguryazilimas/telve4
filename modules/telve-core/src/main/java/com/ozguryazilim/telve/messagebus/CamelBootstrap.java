package com.ozguryazilim.telve.messagebus;

import java.io.Serializable;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
//import org.apache.camel.cdi.CdiCamelContext;
//import org.apache.camel.cdi.ContextName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Camel Context Bootstrap
 * @author Hakan Uygun
 */
// FIXME: jakarta: camel 4 ile beraber camel-cdi kalkmış yerine ne kullanacağız?
@Singleton
@Startup
public class CamelBootstrap implements Serializable{
    
    private static final Logger LOG = LoggerFactory.getLogger(CamelBootstrap.class);
    
    //@Inject //@ContextName("telve")
    //private CdiCamelContext camelContext;
    private Object camelContext;
    
    @PostConstruct
    public void init(){
        LOG.info("Camel CDI Context Init");
        
        /*
        try {
            LOG.info(" Poolfactory name {}", camelContext.getExecutorServiceManager().getThreadPoolFactory().getClass().getName());
            camelContext.start();
            LOG.info(" Poolfactory name {}", camelContext.getExecutorServiceManager().getThreadPoolFactory().getClass().getName());
        } catch (Exception ex) {
            LOG.error("Camel cannot started!", ex);
        }
            */
        LOG.info("Camel CDI Context Started");
    }
    
    
    @PreDestroy
    public void shutdown(){
        try {
            //camelContext.stop();
        } catch (Exception ex) {
            LOG.error("Camel stop error!", ex);
        }
        LOG.info("Camel CDI Context Stoped");
    }
}
