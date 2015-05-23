/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.dynaform;

import com.ozguryazilim.telve.dynaform.model.DynaForm;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DynaForm verilerini tutar ve gerekli servisleri sağlar.
 * @author Hakan Uygun
 */
@Named
@ApplicationScoped
public class DynaFormManager implements Serializable{
    
    private static final Logger LOG = LoggerFactory.getLogger(DynaFormManager.class);
    
    //Startup'da scan edilen builderlar konacak.
    private static final List<Class<DynaFormBuilder>> BUILDERS = new ArrayList<>();
    
    
    private Map<String, List<DynaForm>> forms = new HashMap<>();
    private Map<String, DynaForm> formMap = new HashMap<>();
    
    
    public static void registerBuilder( Class<DynaFormBuilder> dfb ){
        LOG.info("Form Builder Registered {}", dfb.getName());
        BUILDERS.add(dfb);
    }
    
    
    @PostConstruct
    public void init(){
        for( Class<DynaFormBuilder> cls : BUILDERS ){
            
            try {
                deployForms( cls.newInstance().buildForms());
            } catch (InstantiationException | IllegalAccessException ex) {
                LOG.error("Form Deployment Error", ex);
            }
            
        }
    }
    
    /**
     * Verilen formu sisteme ekler.
     * @param form 
     */
    public void deployForm( DynaForm form ){
        List<DynaForm> ls = forms.get(form.getDomain());
        if( ls == null ){
            ls = new ArrayList<>();
            forms.put(form.getDomain(), ls);
        }
        
        //TODO: Önceden olup olmadığına bir baksak?
        ls.add(form);
        formMap.put(form.getId(), form);
        
        LOG.info("Form Deployed : {}:{}", form.getDomain(), form.getId());
    }
    
    /**
     * Verilen form listesini deploy eder
     * @param forms 
     */
    public void deployForms( List<DynaForm> forms ){
        
        for( DynaForm f : forms){
            deployForm(f);
        } 
        
    }
    
    /**
     * Geriye verilen grup için olan formları döndürür.
     * 
     * TODO: Permission kontrolü yapmak lazım.
     * s
     * @param group
     * @return 
     */
    public List<DynaForm> getForms( String domain ){
        return forms.get(domain);
    }
    
    /**
     * ID'si verilen formu döndürür.
     * @param id
     * @return 
     */
    public DynaForm getForm( String id ){
        return formMap.get(id);
    }
}
