/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.batch;

import java.io.Serializable;
import java.util.Properties;
import javax.batch.api.listener.JobListener;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * BatchLog için gerekli init ve done işlemlerini takip eder.
 * 
 * @author Hakan Uygun 
 */
@Dependent
@Named("BatchStateListener")
public class BatchStateListener implements JobListener, Serializable{

    @Inject
    private JobContext jobCtx;
    
    @Inject
    private BatchLogger logger;
    
    @Override
    public void beforeJob() throws Exception {
        logger.batchStartLog( jobCtx.getJobName(), jobCtx.getInstanceId(), jobCtx.getExecutionId(), getProperties().toString(), getProperties().getProperty("ACTOR"));
    }

    @Override
    public void afterJob() throws Exception {
        logger.batchStopLog( jobCtx.getJobName(), jobCtx.getInstanceId(), jobCtx.getExecutionId(), jobCtx.getBatchStatus());
    }
    
    
    protected Properties getProperties(){
        return BatchRuntime.getJobOperator().getJobExecution(jobCtx.getExecutionId()).getJobParameters();
    }
}
