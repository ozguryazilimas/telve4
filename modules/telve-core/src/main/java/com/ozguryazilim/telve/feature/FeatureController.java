/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.feature;

import com.ozguryazilim.telve.entities.FeaturePointer;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Feature'lara ve genel geöer fonksiyonlarına erişim için kullanılır.
 *
 * @author Hakan Uygun
 */
@Named
@RequestScoped
public class FeatureController implements Serializable {

    //Kısa süreli cachleme. Çünkü üretilen link defalarca isteniyor ve kendisi Immutable
    private Map<FeaturePointer, FeatureLink> linkMap = new HashMap<>();

    
    @Inject @Named("messages")
    private Map<String,String> messages;
    
    /**
     * Verilen bir pointer'ı linke çevirir. Örnek kullanım :
     * <t:outputFeatureLink label="general.label.Source" value="#{featureController.getFeatureLink(contactHome.entity.sourcePointer)}" />
     *
     * @param pointer
     * @return eğer verilen pointer null ise geriye null döner
     */
    public FeatureLink getFeatureLink(FeaturePointer pointer) {
        if (pointer == null) {
            return null;
        }

        FeatureLink fl = linkMap.get(pointer);
        if (fl == null) {

            //FIXME: Burada icon işi ne olacak bir bakalım
            fl = new FeatureLink(pointer.getFeature(),
                    pointer.getPrimaryKey(),
                    FeatureRegistery.getCaption(pointer.getFeature()),
                    pointer.getBusinessKey(),
                    messages.get("feature.icon."+pointer.getFeature()));
                    //FeatureRegistery.getIcon(pointer.getFeature()));

            linkMap.put(pointer, fl);
        }

        return fl;

    }

}
