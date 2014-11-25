/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.batch;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

/**
 * JBatch için producerlar.
 * 
 * @author Hakan Uygun
 */
@Dependent
public class BatchProducer {
   
    @Produces
    public JobOperator produceJobOperator(){
        return BatchRuntime.getJobOperator();
    }
    
}
