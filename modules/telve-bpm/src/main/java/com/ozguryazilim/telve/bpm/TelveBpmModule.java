package com.ozguryazilim.telve.bpm;

import com.ozguryazilim.telve.api.module.TelveModule;
import com.ozguryazilim.telve.bpm.handlers.TaskAssingmentResultCommand;
import static com.ozguryazilim.telve.bpm.handlers.TaskResultCommand.ACCEPT;
import static com.ozguryazilim.telve.bpm.handlers.TaskResultCommand.COMPLETE;
import static com.ozguryazilim.telve.bpm.handlers.TaskResultCommand.REJECT;
import com.ozguryazilim.telve.bpm.handlers.TaskResultCommandRegistery;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Telve BPM Module Definition
 * @author Hakan Uygun
 */
@TelveModule
public class TelveBpmModule {
    
    private static final Logger LOG = LoggerFactory.getLogger(TelveBpmModule.class);
    
    @PostConstruct
    public void init(){
        LOG.info("Telve BPM Module Loaded!");
        
        TaskResultCommandRegistery.registerCommand( COMPLETE );
        TaskResultCommandRegistery.registerCommand( ACCEPT );
        TaskResultCommandRegistery.registerCommand( REJECT );
        
        TaskResultCommandRegistery.registerCommand(new TaskAssingmentResultCommand("ACCEPT_ASSIGMENT", "fa fa-check", "btn-success"));
        
        
    }
    
}
