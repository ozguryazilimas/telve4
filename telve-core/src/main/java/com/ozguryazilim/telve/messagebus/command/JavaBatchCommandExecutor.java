/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command;

import com.ozguryazilim.telve.commands.ExecutionLogger;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.Stateful;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JBatch Komutlarını sistemden alarak JBatch'e çalıştırır.
 * 
 * AbstractJBatchCommand'ı miras alan sınıf/komutlar için bu Executor otomatik çalışır.
 * 
 * @author Hakan Uygun
 */
@Stateful
@CommandExecutor(command = AbstractJBatchCommand.class, dispacher = "seda:JBatchCommandExecutor" )
public class JavaBatchCommandExecutor extends AbstractCommandExecuter<AbstractJBatchCommand>{

    private static final Logger LOG = LoggerFactory.getLogger(JavaBatchCommandExecutor.class);
    
    @Inject
    private ExecutionLogger logger;
    
    @Override
    public void execute(AbstractJBatchCommand command) {
        try{
            BatchRuntime.getJobOperator().start(command.getJobName(), command.getProperties());
        } catch (Exception e){
            LOG.error("Java Batch execution error", e);
            logger.error(command, e.getLocalizedMessage());
        }
    }
    
}
