/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.cache;

import com.ozguryazilim.telve.cache.repository.AbstractCacheEntityRepository;
import com.ozguryazilim.telve.entities.Option;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author haky
 */
@RequestScoped
public class OptionCacheRepository extends AbstractCacheEntityRepository<String, Option> implements Serializable{

    @Override
    protected Class<Option> getEntityClass() {
        return Option.class;
    }
    
}
