/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.suggestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Hakan Uygun
 */
@ApplicationScoped
@Named
public class SuggestionGroupRegistery implements Serializable{
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(SuggestionGroupRegistery.class);

    /**
     * Key alanında group isimi, boolean olarak da ek key verisine ihtyaç duyup
     * duymadığı durur.
     */
    private Map<String, Boolean> groupRegistery = new HashMap<String, Boolean>();

    /**
     *
     * Sisteme GenerelCode için grup ismi register eder.
     *
     * @param group Kullanılacak Grup conts değeri
     * @param needKey ek bilgi alanına ihtiyaç duyup duymadığı
     */
    public void addGroup(String group, Boolean needKey) {
        LOG.info("Register Edilen GenerelCode grubu : #0, #1", group, needKey);
        groupRegistery.put(group, needKey);
    }

    /**
     * Geriye grup isimlerinin listesini döndürür.
     *
     * @return
     */
    public List<String> getGroupNames() {
        return new ArrayList<String>(groupRegistery.keySet());
    }

    /**
     * Geriye ismi verilen grup için ek key alanına ihtiyaç olup olmadığını
     * döndürür.
     *
     * @param groupName
     * @return
     */
    public Boolean getGroupNeedKey(String groupName) {
        Boolean result = groupRegistery.get(groupName);
        return result == null ? false : result;
    }

    public Map<String, Boolean> getGroupRegistery() {
        return groupRegistery;
    }

    public void setGroupRegistery(Map<String, Boolean> groupRegistery) {
        this.groupRegistery = groupRegistery;
    }

    /**
     * CDI Bean instance'i döndürür.
     * @return 
     */
    public static SuggestionGroupRegistery intance() {
        return BeanProvider.getContextualReference(SuggestionGroupRegistery.class, true);
    }
}
