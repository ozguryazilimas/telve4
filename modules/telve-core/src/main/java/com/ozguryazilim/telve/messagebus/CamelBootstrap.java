package com.ozguryazilim.telve.messagebus;

import java.io.Serializable;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
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
    
    @Inject //@ContextName("telve")
    //private CdiCamelContext camelContext;
    private TelveCamelContext camelContext;
    
    @Inject @Any
    private Instance<RouteBuilder> routes; 

    @PostConstruct
    public void init(){
        LOG.info("Camel CDI Context Init");
        
        
        try {

            

            routes.forEach(r -> {
                LOG.info("Route: " + r.getClass().getSimpleName());    
                try {
                    r.addRoutesToCamelContext(camelContext);
                } catch (Exception e) {
                    LOG.error("Camel build route ", e);
                }

            });
            
            camelContext.getRouteDefinitions().forEach( r->{
                LOG.info( "Route {} : {} {} {}", r.getDisabled(), r.getRouteId(), r.getAutoStartup(), r.getEndpointUrl() );
                
                r.autoStartup(true);
            });


            LOG.info("Routes Starts All ");
            camelContext.getRouteController().startAllRoutes();

            //LOG.info(" Poolfactory name {}", camelContext.getExecutorServiceManager().getThreadPoolFactory().getClass().getName());
            LOG.info(" Poolfactory name {}", camelContext.getExecutorServiceManager().getThreadNamePattern());
            camelContext.start();
            
            LOG.info( "Routes: {}", camelContext.getRouteDefinitions());
            
                        
            
            //LOG.info(" Poolfactory name {}", camelContext.getExecutorServiceManager().getThreadPoolFactory().getClass().getName());
        } catch (Exception ex) {
            LOG.error("Camel cannot started!", ex);
        }
        
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
