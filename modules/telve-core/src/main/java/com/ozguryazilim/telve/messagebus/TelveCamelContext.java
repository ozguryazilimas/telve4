package com.ozguryazilim.telve.messagebus;

import com.ozguryazilim.telve.workarounds.TelveCamelThreadPoolFactory;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedThreadFactory;
import jakarta.enterprise.context.ApplicationScoped;

import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.engine.DefaultShutdownStrategy;
//import org.apache.camel.cdi.CdiCamelContext;
//import org.apache.camel.cdi.ContextName;
//import org.apache.camel.impl.DefaultShutdownStrategy;
import org.apache.camel.spi.ShutdownStrategy;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Telve Camel Context Bean
 * @author Hakan Uygun
 */
@ApplicationScoped
// FIXME: jakarta: camel 4 ile beraber camel-cdi kalkmış yerine ne kullanacağız?
//@ContextName("telve")
public class TelveCamelContext extends DefaultCamelContext{
    
    private static final Logger LOG = LoggerFactory.getLogger(TelveCamelContext.class);

    @Resource
    ManagedThreadFactory mtf;
    
    @PostConstruct
    public void init(){
        setAutoStartup(true);
        initEagerMandatoryServices();
        

        LOG.info("Camel CDI Context Create");

        

        setTracing("true".equals(ConfigResolver.getProjectStageAwarePropertyValue("camel.tracer", "false")));
       
        //TODO: Belki buraya project stage eklenebilir. Debug amaçlı camel yavaşlatıcı
        //setDelayer(1000l);
        ShutdownStrategy ss = new DefaultShutdownStrategy(); 
        //ss.setShutdownNowOnTimeout(true);
        ss.setTimeout(15l);
        setShutdownStrategy(ss);
        
        getExecutorServiceManager().setThreadPoolFactory(new TelveCamelThreadPoolFactory(mtf));
        
        
    }

    @Override
    public void start() {
        // TODO Auto-generated method stub
        super.start();
    }
}
