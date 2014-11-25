/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.batch;

import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.entities.JobDefinition;
import javax.enterprise.context.Dependent;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;

/**
 * JobDefinition entitysi için repository kontrol sınıfı.
 * 
 * @author Hakan Uygun
 */
@Dependent
@Repository
public abstract class JobDefinitionRepository extends RepositoryBase<JobDefinition, JobDefinition> implements CriteriaSupport<JobDefinition>{
    
}
