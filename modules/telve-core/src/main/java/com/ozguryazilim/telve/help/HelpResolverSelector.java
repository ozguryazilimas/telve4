package com.ozguryazilim.telve.help;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sistemde HelpResolver interface'ini implemente etmiş sınıflar içerisinden doğru belişen listesini bulup döndürür.
 * 
 * @author oyas
 */
@ApplicationScoped
public class HelpResolverSelector {
    private static final Logger LOG = LoggerFactory.getLogger(HelpResolverSelector.class);
    
    @Inject
    @Any
    private Instance<HelpResolver> instance;
    
    /**
     * Bütün resolver'ların application context'e olmasını bekliyoruz. Dolayısı
     * ile her seferinde lookup yapmaya gerek yok.
     */
    private List<HelpResolver> resolvers = new ArrayList<>();

    @PostConstruct
    public void init() {
        //Sistemdeki providerları bir cachleyelim
        for (HelpResolver provider : instance) {
            resolvers.add(provider);
        }
        
        resolvers.sort(new Comparator<HelpResolver>() {
            @Override
            public int compare(HelpResolver t, HelpResolver t1) {
                return t.getOrder().compareTo(t1.getOrder());
            }
        });
        
        LOG.debug("Resolvers : {}", resolvers);
    }
    
    /**
     * Handle edebilecek ilk resolver'ı döndürür.
     * @return 
     */
    public HelpResolver getResolver(){
        for( HelpResolver resolver : resolvers){
            if( resolver.canHandle() ){
                return resolver;
            }
        }
        
        return null;
    }
}
