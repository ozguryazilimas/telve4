package com.ozguryazilim.telve.attachment;

import com.ozguryazilim.telve.entities.FeaturePointer;
import java.util.ArrayList;
import java.util.Collections;
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
 * Sistemde tanımlı AttachmentContextProvider'lar içinden uygun olanı seçer.
 *
 * Bunun için canHandle ve priority değerlerini kullanır.
 *
 *
 * @author Hakan Uygun
 */
@ApplicationScoped
public class AttacmentContextProviderSelector {

    private static final Logger LOG = LoggerFactory.getLogger(AttacmentContextProviderSelector.class);
    
    @Inject
    @Any
    private Instance<AttachmentContextProvider> instance;

    /**
     * Bütün provider'ların application context'e olmasını bekliyoruz. Dolayısı
     * ile her seferinde lookup yapmaya gerek yok.
     */
    private List<AttachmentContextProvider> providers = new ArrayList<>();

    @PostConstruct
    public void init() {
        //Sistemdeki providerları bir cachleyelim
        for (AttachmentContextProvider provider : instance) {
            providers.add(provider);
        }
    }

    public AttachmentContextProvider getAttachmentContextProvider(FeaturePointer featurePointer, Object payload) {
        List<AttachmentContextProvider> result = new ArrayList<>();

        LOG.debug("Providers {}", providers);
        
        //Feature + Payload'a göre bak
        if (featurePointer != null && payload != null ) {
            for (AttachmentContextProvider provider : providers) {
                if (provider.canHandle(featurePointer, payload)) {
                    result.add(provider);
                }
            }

            if (!result.isEmpty()) {
                Collections.sort(result, Comparator.<AttachmentContextProvider>comparingInt(p -> p.priority()));
                LOG.debug("Feature+Payload Selected Providers {}", result);
                return result.get(0);
            }
        }

        //Bulunamadı sadece Payload'a göre bak
        if (payload != null) {
            for (AttachmentContextProvider provider : providers) {
                if (provider.canHandle(payload)) {
                    result.add(provider);
                }
            }

            if (!result.isEmpty()) {
                Collections.sort(result, Comparator.<AttachmentContextProvider>comparingInt(p -> p.priority()));
                LOG.debug("Payload Selected Providers {}", result);
                return result.get(0);
            }
        }
        
        //Bulunamadı sadece Feature'a göre bak
        if (featurePointer != null) {
            for (AttachmentContextProvider provider : providers) {
                if (provider.canHandle(featurePointer)) {
                    result.add(provider);
                }
            }

            if (!result.isEmpty()) {
                Collections.sort(result, Comparator.<AttachmentContextProvider>comparingInt(p -> p.priority()));
                LOG.debug("Feature Selected Providers {}", result);
                return result.get(0);
            }
        }

        LOG.error("Cannot find suitable AttachmentContextProvider for feature {} and payload {}", featurePointer, payload);
        
        //Hala Bulunamadı Exception mı verelim?
        throw new RuntimeException("Cannot find suitable AttachmentContextProvider!" );
        
    }

}
