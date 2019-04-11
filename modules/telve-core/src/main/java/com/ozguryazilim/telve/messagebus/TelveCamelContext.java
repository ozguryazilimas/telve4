package com.ozguryazilim.telve.messagebus;

import com.ozguryazilim.telve.workarounds.TelveCamelThreadPoolFactory;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.enterprise.context.ApplicationScoped;
import org.apache.camel.cdi.CdiCamelContext;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.impl.DefaultShutdownStrategy;
import org.apache.camel.spi.ShutdownStrategy;
import org.apache.deltaspike.core.api.config.ConfigResolver;

/**
 * Telve Camel Context Bean
 * @author Hakan Uygun
 */
@ApplicationScoped
@ContextName("telve")
public class TelveCamelContext extends CdiCamelContext{
    
    @Resource
    ManagedThreadFactory mtf;
    
    @PostConstruct
    public void init(){
        setTracing("true".equals(ConfigResolver.getProjectStageAwarePropertyValue("camel.tracer", "false")));
        //TODO: Belki buraya project stage eklenebilir. Debug amaçlı camel yavaşlatıcı
        //setDelayer(1000l);
        ShutdownStrategy ss = new DefaultShutdownStrategy(); 
        //ss.setShutdownNowOnTimeout(true);
        ss.setTimeout(15l);
        setShutdownStrategy(ss);
        
        getExecutorServiceManager().setThreadPoolFactory(new TelveCamelThreadPoolFactory(mtf));
        
    }
}
