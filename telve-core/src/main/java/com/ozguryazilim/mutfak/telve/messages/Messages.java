/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.mutfak.telve.messages;

import java.util.AbstractMap;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 * Seam 2 biçminde Messages üretir.
 * 
 * Kod Seam 2 den kopyalandı
 * 
 * @author Hakan Uygun
 */
@RequestScoped
public class Messages {

    protected Map<String, String> createMap() {
        final java.util.ResourceBundle bundle = TelveResourceBundle.getBundle();

        if (bundle == null) {
            return null;
        }

        return new AbstractMap<String, String>() {
            @Override
            public String get(Object key) {
                if (key instanceof String) {
                    String resourceKey = (String) key;

                    String resource;
                    try {
                        resource = bundle.getString(resourceKey);
                    } catch (MissingResourceException mre) {
                        return resourceKey;
                    }

                    return (resource == null) ? resourceKey : resource;

                } else {
                    return null;
                }
            }

            @Override
            public Set<Map.Entry<String, String>> entrySet() {
                Set<Map.Entry<String, String>> entrySet = new HashSet<Map.Entry<String, String>>();

                Enumeration<String> keys = bundle.getKeys();

                while (keys.hasMoreElements()) {
                    final String key = keys.nextElement();

                    entrySet.add(new Map.Entry<String, String>() {

                        public String getKey() {
                            return key;
                        }

                        public String getValue() {
                            return get(key);
                        }

                        public String setValue(String arg0) {
                            throw new UnsupportedOperationException("not implemented");
                        }
                    });
                }

                return entrySet;
            }

        };

    }

    /**
     * Create the Map and cache it in the EVENT scope. No need to cache it in
     * the SESSION scope, since it is inexpensive to create.
     *     
     * @return a Map that interpolates messages in the Seam ResourceBundle
     */
    @Produces @Named("messages")
    public Map<String, String> getMessages() {
        return createMap();
    }

}
