package com.ozguryazilim.telve.jcr;

import com.ozguryazilim.telve.api.module.TelveModule;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Telve JCR Module Definition
 * @author Hakan Uygun
 */
@TelveModule
public class TelveJcrModule {
    
    private static final Logger LOG = LoggerFactory.getLogger(TelveJcrModule.class);
    
    @PostConstruct
    public void init(){
        LOG.info("Telve JCR Module Loaded!");
    }
}
