/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.batch;

import com.ozguryazilim.telve.entities.JobExecutionLog;
import com.ozguryazilim.telve.entities.JobProcessLog;
import java.util.Date;
import javax.batch.runtime.BatchStatus;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Çalışan JBatch'ler içinden çalışma bilgilerini Definition ile ilişkilendirerek kaydeder.
 * 
 * @author Hakan Uygun
 */
@Dependent
@Named
public class BatchLogger {
    private static final Logger LOG = LoggerFactory.getLogger(BatchLogger.class);
    
    @Inject
    private BatchLoggerRepository repository;
    
    
    public void batchStartLog( String jobName, long instanceId, long executionId, String params, String startedBy ){
        LOG.info("{}-{}:{}({}) started by {}", new Object[]{ jobName, instanceId, executionId, params, startedBy });
        
        JobExecutionLog log = new JobExecutionLog(jobName, instanceId, executionId, new Date(), params, startedBy);
        repository.insertExecutionLog(log);
        
    }
    
    
    public void batchStopLog( String jobName, long instanceId, long executionId, BatchStatus status ){
        LOG.info("{}-{}:{} stop {}", new Object[]{ jobName, instanceId, executionId, status });
        
        JobExecutionLog log = repository.findExecutionLogByExecutionId(executionId);
        if( log != null ){
            log.setEndDate(new Date());
            log.setStatus(status.name());
            repository.updateExecutionLog(log);
        } else {
            LOG.warn("JobExecutionLog line not found for {}-{}:{}", new Object[]{ jobName, instanceId, executionId});
        }
        
    }
    
    public void info( String jobName, long instanceId, long executionId, String message ){
        LOG.info("{}-{}:{} {}", new Object[]{ jobName, instanceId, executionId, message });
        
        JobProcessLog jpl = new JobProcessLog(jobName, instanceId, executionId, new Date(), "INFO", message);
        repository.insertProcessLog(jpl);
        
    }
    
    public void debug( String jobName, long instanceId, long executionId, String message ){
        LOG.debug("{}-{}:{} {}", new Object[]{ jobName, instanceId, executionId, message });
        JobProcessLog jpl = new JobProcessLog(jobName, instanceId, executionId, new Date(), "DEBUG", message);
        repository.insertProcessLog(jpl);
    }
    
    public void warn( String jobName, long instanceId, long executionId, String message ){
        LOG.warn("{}-{}:{} {}", new Object[]{ jobName, instanceId, executionId, message });
        JobProcessLog jpl = new JobProcessLog(jobName, instanceId, executionId, new Date(), "WARN", message);
        repository.insertProcessLog(jpl);
    }
    
    public void error( String jobName, long instanceId, long executionId, String message ){
        LOG.error("{}-{}:{} {}", new Object[]{ jobName, instanceId, executionId, message });
        JobProcessLog jpl = new JobProcessLog(jobName, instanceId, executionId, new Date(), "ERROR", message);
        repository.insertProcessLog(jpl);
    }
    
}
