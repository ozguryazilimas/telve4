/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.audit;

import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.entities.AuditLog;
import com.ozguryazilim.telve.entities.AuditLog_;
import com.ozguryazilim.telve.query.QueryDefinition;
import com.ozguryazilim.telve.query.filters.Filter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.deltaspike.data.api.Modifying;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;

/**
 * AuditLog için repository.
 * 
 * @author Hakan Uygun
 */
@Repository
@Dependent
public abstract class AuditLogRepository extends RepositoryBase<AuditLog, AuditLog> implements CriteriaSupport<AuditLog> {
    
    
    @Override
    public List<AuditLog> browseQuery(QueryDefinition queryDefinition) {
        List<Filter<AuditLog, ?, ?>> filters = queryDefinition.getFilters();
        
        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        //Geriye PersonViewModel dönecek cq'yu ona göre oluşturuyoruz.
        CriteriaQuery<AuditLog> criteriaQuery = criteriaBuilder.createQuery(AuditLog.class);

        //From Tabii ki         
        Root<AuditLog> from = criteriaQuery.from(AuditLog.class);
        
        //Filtreleri ekleyelim.
        List<Predicate> predicates = new ArrayList<>();
        
        decorateFilters(filters, predicates, criteriaBuilder, from);
        
        //Person filtremize ekledik.
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));
        
        //İsme göre sıralayalım
        criteriaQuery.orderBy(criteriaBuilder.desc(from.get(AuditLog_.date)));
        
        
        
        
        //Haydi bakalım sonuçları alalım
        TypedQuery<AuditLog> typedQuery = entityManager().createQuery(criteriaQuery);
        typedQuery.setMaxResults(queryDefinition.getResultLimit());
        List<AuditLog> resultList = typedQuery.getResultList();

        return resultList;
    }
    
    
    @Modifying
    @Query("delete AuditLog as al where al.date <= ?1")
    public abstract void deleteBeforeDate( Date date );
}
