/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.batch;

import com.ozguryazilim.telve.entities.JobDefinition;
import java.io.Serializable;
import java.util.List;
import java.util.Properties;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

/**
 * Telve Batch için gereken iş tanımları için temel kontrol sınıfı
 * @author Hakan Uygun
 */
@SessionScoped
@Named
public class JobDefinitionControl implements Serializable{
    
    @Inject
    private JobDefinitionRepository repository; 
    
    @Inject
    private BatchScheduler scheduler;
    
    //@Inject
    //private transient JobOperator jobOperator;
    
    /**
     * Geriye sistemde tanımlı olan JBatch Job isimlerini döndürür.
     * @return 
     */
    public List<String> getJobNameList(){
        return JobRegistery.getJobNames();
    }
    
    /**
     * Verilen Job için yeni bir iş tanımı oluşturur.
     * @param jobName
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException 
     */
    public JobDefinition createNewJobDefinition( String jobName ) throws InstantiationException, IllegalAccessException{
        JobDefinition jd = repository.createNew();
        jd.setActive(Boolean.FALSE);
        jd.setJobName(jobName);
        jd.setSchedule("NONE");
        jd.setName("Yeni" + jobName);
        jd.setCode(jobName);
        jd.setProperties(new Properties());
        return jd;
    }
    
    
    /**
     * Verilen iş tanımını saklar.
     * @param jd 
     */
    @Transactional
    public void saveJobDefinition( JobDefinition jd ){
        repository.save(jd);
        //Aktifse zamanlamaya da ekleyelim.
        if( jd.getActive() ){
            scheduler.addToScedular(jd);
        }
    }
}
