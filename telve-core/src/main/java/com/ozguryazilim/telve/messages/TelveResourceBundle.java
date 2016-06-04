/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messages;

import com.ozguryazilim.telve.api.module.TelveModuleRegistery;
import com.ozguryazilim.telve.config.LocaleSelector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import javax.enterprise.context.ContextNotActiveException;
import org.apache.deltaspike.core.api.config.ConfigResolver;

/**
 * Seam 2 SeamResourceBundle'dan alındı.
 *
 * @author Hakan Uygun
 */
public class TelveResourceBundle extends java.util.ResourceBundle {

    
    private static final String TELVE_RESOUCE_ORDINAL_KEY = "telve_ordinal";
    private static final Integer TELVE_RESOUCE_DEFAULT_ORDINAL = 100;
    
    private Map<String, Map<Locale, List<ResourceBundle>>> bundleCache = new ConcurrentHashMap<>();

    private Map<Locale, List<ResourceBundle>> getCachedBundle() {
        String init = "Telve";

        if (!bundleCache.containsKey(init)) {
            bundleCache.put(init, new ConcurrentHashMap<Locale, List<ResourceBundle>>());
        }
        return bundleCache.get(init);
    }

    /**
     * Get an instance for the current Locale
     *
     * @see Locale
     *
     * @return a SeamResourceBundle
     */
    public static java.util.ResourceBundle getBundle() {
        //FIXME: Current Locale alınmalı
        return java.util.ResourceBundle.getBundle(TelveResourceBundle.class.getName(),
                Locale.US);
        //org.jboss.seam.core.Locale.instance()); //note: it does not really matter what we pass here
    }

    public static java.util.ResourceBundle getBundleNamed(String bundleName) {
        //FIXME: Current Locale alınmalı
        return java.util.ResourceBundle.getBundle(bundleName,
                Locale.US);
        //org.jboss.seam.core.Locale.instance());
    }

    private List<java.util.ResourceBundle> getBundlesForCurrentLocale() {
        //FIXME: Current Locale alınmalı
        //Locale instance = org.jboss.seam.core.Locale.instance();
        Locale instance = getLocale();
        List<ResourceBundle> bundles = getCachedBundle().get(instance);
        if (bundles == null) {
            bundles = loadBundlesForCurrentLocale();
            getCachedBundle().put(instance, bundles);
        }
        return bundles;

    }

    /**
     * Tanımlı moduller için bundle yukler.
     * 
     * Convention:  modulename-messages
     * 
     * @return 
     */
    private List<ResourceBundle> loadBundlesForCurrentLocale() {
        List<ResourceBundle> bundles = new ArrayList<>();

        for (String bundleName : TelveModuleRegistery.getMessageBundleNames()) {
            ResourceBundle bundle = loadBundle(bundleName);
            if (bundle != null) {
                bundles.add(bundle);
            }
        }

        ResourceBundle bundle = loadBundle("telve-messages");
        if (bundle != null) {
            bundles.add(bundle);
        }
        
        //Uygulamaya özel dil bundle'ı
        ResourceBundle bundle2 = loadBundle("application-messages");
        if (bundle2 != null) {
            bundles.add(bundle2);
        }

        sortBundles(bundles);
        return Collections.unmodifiableList(bundles);
    }
    
    /**
     * Telve Ordinal değerine göre resource'ları sıraya koyar.
     * 
     * Bu sayede farklı dosyaların bir birlerini override edebilmesi granti altına alınır.
     * 
     * @param bundles
     * @return 
     */
    private void sortBundles(List<ResourceBundle> bundles){
        
        Collections.sort(bundles, new Comparator<ResourceBundle>() {
            @Override
            public int compare(ResourceBundle s, ResourceBundle t) {

                String sos = null;
                String tos = null;
                
                try{
                    sos = s.getString(TELVE_RESOUCE_ORDINAL_KEY);
                } catch (MissingResourceException mre) {
                  //Key tanımlı değil  
                }
                
                try{
                    tos = t.getString(TELVE_RESOUCE_ORDINAL_KEY);
                } catch (MissingResourceException mre) {
                  //Key tanımlı değil  
                }
                
                
                Integer so  = TELVE_RESOUCE_DEFAULT_ORDINAL;
                Integer to  = TELVE_RESOUCE_DEFAULT_ORDINAL;
                
                if( sos != null ){
                    try{
                        so = Integer.parseInt(sos);
                        //Integer to = sos != null ?  Integer.getInteger(sos) : TELVE_RESOUCE_DEFAULT_ORDINAL;
                    } catch( Exception ex ){
                        //Ya ordinal tanımlı değil ya da integer değil
                    }
                }
                
                if( tos != null ){
                    try{
                        to = Integer.parseInt(tos);
                    } catch( Exception ex ){
                        //Ya ordinal tanımlı değil ya da integer değil
                    }
                }
                
                return so.compareTo(to);
            }
        });
        
    }

    @Override
    public Enumeration<String> getKeys() {

        List<ResourceBundle> bundles = getBundlesForCurrentLocale();
        Enumeration<String>[] enumerations = new Enumeration[bundles.size()];

        int i = 0;
        for (ResourceBundle bundle : bundles) {
            enumerations[i++] = bundle.getKeys();
        }

        return new EnumerationEnumeration<>(enumerations);
    }

    @Override
    protected Object handleGetObject(String key) {

        for (java.util.ResourceBundle littleBundle : getBundlesForCurrentLocale()) {
            try {
                //return interpolate( littleBundle.getObject(key) );
                return littleBundle.getObject(key);
            } catch (MissingResourceException mre) {
            }
        }

        return null; // superclass is responsible for throwing MRE
    }

    //private Object interpolate(Object message)
    //{
    //   return message!=null && message instanceof String ?
    //            Interpolator.instance().interpolate( (String) message ) :
    //            message;
    //}
    /**
     * Load a resource bundle by name (may be overridden by subclasses who want
     * to use non-standard resource bundle types).
     *
     * @param bundleName the name of the resource bundle
     * @return an instance of java.util.ResourceBundle
     */
    public java.util.ResourceBundle loadBundle(String bundleName) {
        try {
            java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle(
                    bundleName,
                    getLocale(),
                    Thread.currentThread().getContextClassLoader()
            );

            return bundle;
        } catch (MissingResourceException mre) {
            //log.debug("resource bundle missing: " + bundleName);
            return null;
        }
    }

    @Override
    public Locale getLocale() {
        Locale l = null;
        try{
            l = LocaleSelector.instance().getLocale();
        } catch ( ContextNotActiveException e ){
            //Session'a sahip olmayan bir yerden çağrılırsa eğer uygulama'nın değerini al.
            //Mesela camel şeysilerinden.
            l = new Locale(ConfigResolver.getPropertyValue("application.locale", "tr"));
        }
        return l != null ? l : Locale.getDefault();
    }
}
