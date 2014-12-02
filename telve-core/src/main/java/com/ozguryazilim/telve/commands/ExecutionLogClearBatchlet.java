/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.commands;

import java.util.Date;
import java.util.Properties;
import javax.batch.api.Batchlet;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Execution Log tablosunda temizlik yapar.
 * 
 * @author Hakan Uygun
 */
@Dependent
@Named("ExecutionLogClearBatchlet")
public class ExecutionLogClearBatchlet implements Batchlet{

    @Inject
    private JobContext jobCtx;
    
    @Inject
    private ExecutionLogger logger;
    
    @Inject
    private ExecutionLogRepository repository;
    
    @Override
    public String process() throws Exception {
        logger.jobInfo( jobCtx.getJobName(), jobCtx.getInstanceId(), jobCtx.getExecutionId(), "Started");
        
        String interval = getProperties().getProperty("INTERVAL");
        try{
            Long inv = Long.parseLong(interval);
        } catch (NumberFormatException ex) {
            logger.jobError( jobCtx.getJobName(), jobCtx.getInstanceId(), jobCtx.getExecutionId(), "Invalid parameter expireInterval : " + interval);
            throw ex;
        }
        
        //TODO: Burda interval üzerinden hesap yapılacak
        Date delDate = new Date();
        
        //ExecutionLofRepository üzerinden silme işlemi
        repository.deleteByDate(delDate);
        
        logger.jobInfo( jobCtx.getJobName(), jobCtx.getInstanceId(), jobCtx.getExecutionId(), "Completed");
        
        return "COMPLETED";
    }

    @Override
    public void stop() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * JobContext#getProperties ile gelmesi gereken bilgiler gelmediği için workaround.
     * 
     * XML'e tanımlanan property'ler JobContext üzerinden alınabilir. Runtime parametreleri için bunun kullanılması gerek.
     * 
     * @return 
     */
    protected Properties getProperties(){
        return BatchRuntime.getJobOperator().getJobExecution(jobCtx.getExecutionId()).getJobParameters();
    }
}
