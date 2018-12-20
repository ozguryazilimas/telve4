package com.ozguryazilim.telve.query.filters;

import com.ozguryazilim.telve.entities.EntityBase;
import com.ozguryazilim.telve.lookup.LookupControllerBase;
import javax.persistence.metamodel.SingularAttribute;

/**
 * Dialog popup çıkararak kullanıcıya seçim şansı veren entity filter.
 * 
 * @author Hakan Uygun
 * @param <E> Sorgunun yapılacağı entity sınıfı
 * @param <T> Filtre değeri olarak kullanılacak entity sınıfı
 */
public class EntityDialogFilter<E extends EntityBase, T extends EntityBase> extends EntityOverlayFilter<E, T>{

    public EntityDialogFilter(SingularAttribute<? super E, T> attribute, Class<? extends LookupControllerBase<T, ?>> lookupClazz, String label) {
        super(attribute, lookupClazz, label);
    }

    @Override
    public String getTemplate() {
        return "entityDialogFilter";
    }
    
}
