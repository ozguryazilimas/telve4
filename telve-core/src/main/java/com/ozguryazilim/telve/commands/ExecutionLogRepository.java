/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.commands;

import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.entities.ExecutionLog;
import java.util.Date;
import javax.enterprise.context.Dependent;
import org.apache.deltaspike.data.api.Modifying;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;

/**
 * EecutionLog için repository sınıfı.
 * @author Hakan Uygun
 */
@Repository
@Dependent
public abstract class ExecutionLogRepository extends RepositoryBase<ExecutionLog, ExecutionLog> implements CriteriaSupport<ExecutionLog>{

    @Modifying
    @Query("delete ExecutionLog where date < ?")
    public abstract void deleteByDate( Date date );
    
}
