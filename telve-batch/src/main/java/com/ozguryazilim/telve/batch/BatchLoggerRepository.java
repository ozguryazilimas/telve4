/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.batch;

import com.ozguryazilim.telve.entities.JobExecutionLog;
import com.ozguryazilim.telve.entities.JobProcessLog;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;


/**
 * BatchLog Kayıtları için Repository işlemleri.
 * 
 * @author Hakan Uygun
 */
@Dependent
public class BatchLoggerRepository {
    
    
    @Inject
    private EntityManager entityManager;
    
    /**
     * Verilen log satırını kaydeder.
     * @param log 
     */
    @Transactional
    public void insertExecutionLog( JobExecutionLog log ){
        entityManager.persist(log);
    }

    
    /**
     * Verilen log satırını kaydeder.
     * @param log 
     */
    @Transactional
    public void updateExecutionLog( JobExecutionLog log ){
        entityManager.merge(log);
    }
    
    /**
     * Verilen ExecutionId'ye sahip satırların ilkini döndürür.
     * @param executionId
     * @return 
     */
    public JobExecutionLog findExecutionLogByExecutionId( Long executionId ){
        List<JobExecutionLog> ls = entityManager.createQuery("select c from JobExecutionLog c where executionId = :executionId")
                .setParameter("executionId", executionId)
                .getResultList();
        
        if( !ls.isEmpty()){
            return ls.get(0);
        }
        
        return null;
    }
    
    
    /**
     * Verilen log satırını kaydeder.
     * @param log 
     */
    @Transactional
    public void insertProcessLog( JobProcessLog log ){
        entityManager.persist(log);
    }

    
    /**
     * Verilen log satırını kaydeder.
     * @param log 
     */
    @Transactional
    public void updateProcessLog( JobProcessLog log ){
        entityManager.merge(log);
    }
    
    /**
     * Verilen tarihten eski process loglarını siler.
     * @param date 
     */
    @Transactional
    public int deleteProcessLog( Date date ){
        return entityManager.createQuery("delete JobProcessLog where logDate < :logDate")
                .setParameter("logDate", date)
                .executeUpdate();
    }
    
    /**
     * Verilen tarihten eski execution loglarını siler.
     * @param date 
     */
    @Transactional
    public int deleteExecutionLog( Date date ){
        return entityManager.createQuery("delete JobExecutionLog where startDate < :logDate")
                .setParameter("logDate", date)
                .executeUpdate();
    }
}
