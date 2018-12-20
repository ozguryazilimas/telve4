package com.ozguryazilim.telve.query.filters;

import com.ozguryazilim.telve.entities.EntityBase;
import com.ozguryazilim.telve.lookup.LookupControllerBase;
import com.ozguryazilim.telve.lookup.LookupSelect;
import com.ozguryazilim.telve.lookup.LookupSelectListener;
import javax.enterprise.event.Observes;
import javax.persistence.metamodel.SingularAttribute;

/**
 * Overlay popup çıkararak kullanıcıya seçim şansı veren entity filter.
 *
 * @author Hakan Uygun
 * @param <E> Filtrenin uygulanacağı entity sınıfı
 * @param <T> Filtre için kullanılacak entity sınıfı
 */
public class EntityOverlayFilter<E extends EntityBase, T extends EntityBase> extends EntityFilter<E, T> {

    private String profile;
    private String listener;

    public EntityOverlayFilter(SingularAttribute<? super E, T> attribute, Class<? extends LookupControllerBase<T, ?>> lookupClazz, String label) {
        super(attribute, lookupClazz, label);

        if (lookupClazz != null) {
            //TODO: Burada riskli bir durum var. Bu nesne bulunduğu browse memory'den atıldığında ne olacak?
            getLookupBean().registerListener("event:" + attribute.getName(), new LookupSelectListener() {
                @Override
                public void onSelect(Object o) {
                    setValue((T) o);
                }
            });
        }

    }

    @Override
    public String getTemplate() {
        return "entityOverlayFilter";
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getListener() {
        return listener;
    }

    public void setListener(String listener) {
        this.listener = listener;
    }

    public void onSelect(@Observes @LookupSelect T value) {
        System.out.println("On Select tetiklendi");
        setValue(value);
    }

}
