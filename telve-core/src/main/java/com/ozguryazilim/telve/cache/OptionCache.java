/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.cache;

import com.ozguryazilim.telve.cache.repository.RepositoryCache;
import com.ozguryazilim.telve.cache.repository.AbstractCacheEntityRepository;
import com.ozguryazilim.telve.entities.Option;
import java.util.List;
import javax.cache.Cache;

/**
 *
 * @author Hakan Uygun
 */
public class OptionCache extends RepositoryCache<String, Option>{

    public OptionCache(Cache<String, Option> cache, AbstractCacheEntityRepository<String, Option> repository) {
        super(cache, repository);
    }

    @Override
    public Option get(String k) {
        return getWrappedCache().get(k);
    }
    
    public void loadAll(){
        List<Option> ls = getWrappedRepository().findAll();
        for( Option o : ls ){
            getWrappedCache().put(o.getKey(), o);
        }
    }
    
}
