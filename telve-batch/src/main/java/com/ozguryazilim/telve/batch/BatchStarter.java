/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.batch;

import com.ozguryazilim.telve.entities.JobDefinition;
import java.io.Serializable;
import java.util.Properties;
import javax.batch.runtime.BatchRuntime;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * JBatch joblarını çalıştırmak için API.
 * 
 * @author Hakan Uygun
 */
@Dependent
@Named
public class BatchStarter implements Serializable{
    
    @Inject
    private BatchLogger batchLogger;
    
    public void start( JobDefinition jd ){
        Properties props = new Properties();
        props.putAll(jd.getProperties());
        props.setProperty("ACTOR", "SYSTEM-" + jd.getName());
        props.setProperty("JD-ID", jd.getId().toString());
        BatchRuntime.getJobOperator().start(jd.getJobName(), props);
    }
    
    
    public void start( String jobName, Properties props, String actor ){
        props.setProperty("ACTOR", actor);
        BatchRuntime.getJobOperator().start( jobName, props );
    }
    
}
