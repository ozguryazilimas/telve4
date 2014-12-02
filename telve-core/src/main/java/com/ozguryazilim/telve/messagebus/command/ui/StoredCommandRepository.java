/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command.ui;

import com.google.gson.Gson;
import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.entities.StoredCommand;
import com.ozguryazilim.telve.messagebus.command.StorableCommand;
import javax.enterprise.context.Dependent;
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
    
}
