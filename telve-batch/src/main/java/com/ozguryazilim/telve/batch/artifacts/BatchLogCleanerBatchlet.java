/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.batch.artifacts;

import com.ozguryazilim.telve.batch.BatchLogger;
import com.ozguryazilim.telve.batch.BatchLoggerRepository;
import java.util.Date;
import java.util.Properties;
import javax.batch.api.Batchlet;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Batch işlemleri sonucu oluşan logları silecek olan Batchlet.
 * 
 * TODO: Mesajlar dil destekli hale gelmeli...
 * 
 * @author Hakan Uygun
 */
@Dependent
@Named("BatchLogCleanerBatchlet")
public class BatchLogCleanerBatchlet implements Batchlet{
    @Inject
    private JobContext jobCtx;
    
    @Inject
    private StepContext stepContext;

    @Inject
    private BatchLogger logger;
    
    @Inject
    private BatchLoggerRepository repository;
    
    @Override
    public String process() throws Exception {

        System.out.println(stepContext.getProperties());
        System.out.println(  ); 
        System.out.println( jobCtx.getProperties());
        System.out.println( jobCtx.getProperties().getProperty("expireInterval"));
        System.out.println( jobCtx.getProperties().getProperty("executionLog"));
        System.out.println( jobCtx.getTransientUserData());
        
        String interval = getProperties().getProperty("expireInterval");
        try{
            Long inv = Long.parseLong(interval);
        } catch (NumberFormatException ex) {
            logger.error( jobCtx.getJobName(), jobCtx.getInstanceId(), jobCtx.getExecutionId(), "Invalid parameter expireInterval : " + interval);
            throw ex;
        }
        
        boolean deleteProcLog = !"FALSE".equals( getProperties().getProperty("processLog"));
        boolean deleteExecLog = !"FALSE".equals( getProperties().getProperty("executionLog"));
        
        //TODO: Burda interval üzerinden hesap yapılacak
        Date delDate = new Date();
                
        if( deleteExecLog ){
            int delc = repository.deleteExecutionLog( delDate );
            logger.info( jobCtx.getJobName(), jobCtx.getInstanceId(), jobCtx.getExecutionId(), delc + " items deleted from ExecutionLog");
        }
        
        if( deleteProcLog ){
            int dplc = repository.deleteProcessLog(delDate);
            logger.info( jobCtx.getJobName(), jobCtx.getInstanceId(), jobCtx.getExecutionId(), dplc + " items deleted from ProcessLog");
        }
        
        return "COMPLETED";
    }

    @Override
    public void stop() throws Exception {
        //
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
