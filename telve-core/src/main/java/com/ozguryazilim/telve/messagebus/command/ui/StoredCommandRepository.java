/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command.ui;

import com.google.gson.Gson;
import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.entities.StoredCommand;
import com.ozguryazilim.telve.entities.StoredCommand_;
import com.ozguryazilim.telve.messagebus.command.StorableCommand;
import com.ozguryazilim.telve.query.QueryDefinition;
import com.ozguryazilim.telve.query.filters.Filter;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;

/**
 * Saklanmış komutlar için repository sınıfı.
 * 
 * @author Hakan Uygun 
 */
@Repository
@Dependent
public abstract class StoredCommandRepository extends RepositoryBase<StoredCommand, StoredCommand> implements CriteriaSupport<StoredCommand>{
    
    
    /**
     * Verilen Entity'e verilen komutu yerleştirip geri döndürür.
     * @param entity
     * @param command
     * @return 
     */
    public StoredCommand merge( StoredCommand entity, StorableCommand command ){
        //Entity üzerindeki isim daha geçerli kabul ediyoruz.
        command.setName(entity.getName());
        //Komutu entity üzerine serialize ediyoruz
        entity.setType(command.getClass().getName());
        entity.setCommand( serialize(command));
        return entity;
    }
    
    public String serialize( StorableCommand command ){
        Gson gson = new Gson();
        return gson.toJson(command);
    }
    
    public <C extends StorableCommand> C deserialize( String data, Class<C> clazz ){
        Gson gson = new Gson();
        return gson.fromJson(data, clazz);
    }
    
    public StoredCommand convert( StorableCommand command ){
        StoredCommand sc = new StoredCommand();
        
        sc.setName(command.getName());
        sc.setType(command.getClass().getName());
        sc.setCommand( serialize(command));
        
        return sc;
    }
    
    public StorableCommand convert( StoredCommand command ) throws ClassNotFoundException{
        
        Class<? extends StorableCommand> clazz = (Class<? extends StorableCommand>) this.getClass().getClassLoader().loadClass(command.getType());
        
        StorableCommand sc = deserialize(command.getCommand(), clazz);
        
        return sc;
    }

    @Override
    public List<StoredCommand> browseQuery(QueryDefinition queryDefinition) {
        List<Filter<StoredCommand, ?, ?>> filters = queryDefinition.getFilters();
        
        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        //Geriye PersonViewModel dönecek cq'yu ona göre oluşturuyoruz.
        CriteriaQuery<StoredCommand> criteriaQuery = criteriaBuilder.createQuery(StoredCommand.class);

        //From Tabii ki         
        Root<StoredCommand> from = criteriaQuery.from(StoredCommand.class);
        
        //Filtreleri ekleyelim.
        List<Predicate> predicates = new ArrayList<>();
        
        decorateFilters(filters, predicates, criteriaBuilder, from);
        
        //Person filtremize ekledik.
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));
        
        //İsme göre sıralayalım
        criteriaQuery.orderBy(criteriaBuilder.asc(from.get(StoredCommand_.name)));
        
        //Haydi bakalım sonuçları alalım
        TypedQuery<StoredCommand> typedQuery = entityManager().createQuery(criteriaQuery);
        List<StoredCommand> resultList = typedQuery.getResultList();

        return resultList;
    }
}
