/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.apache.shiro.subject.Subject;


/**
 * OptionPane'leri kullanarak kullanıcıların sistem ayarlarını tanımlaması için UI kontrol sınıfı.
 * 
 * @author Hakan Uygun
 */
@WindowScoped
@Named
public class OptionPaneController implements Serializable{
    
    private final List<String> optionPanes = new ArrayList<>();
    private final Map<String, String> optionPaneViews = new HashMap<>();
    
    @Inject @Any
    private Subject identity;
    
    @Inject
    private ViewConfigResolver viewConfigResolver;

    private String selectedPane;
    
    @PostConstruct
    public void init(){
        for(  Entry<String,OptionPane> e : OptionPaneRegistery.getOptionPanes().entrySet()){
            
            //Permission tanımlı değişse sınıf üzerinden bakıyoruz.
            String p = e.getValue().permission();
            if( p.isEmpty() ){
                p = e.getKey();
            }
            
            if( identity.isPermitted(p + ":select")){
                optionPanes.add(e.getKey());
                
                String viewId = getOptiponPageViewId(e.getValue().optionPage());
                
                optionPaneViews.put(e.getKey(), viewId);
                
            }
        }
        //İsim sırasına sokalım
        Collections.sort(optionPanes);
        //En az bir adet Pane olacağını varsayıyoruz :)
        selectedPane = optionPanes.get(0);
    }

    /**
     * OptionPane için sınıf annotationı üzerinden aldığı Page ID'sini döndürür.
     *
     * @return
     */
    protected String getOptiponPageViewId( Class<? extends ViewConfig> view) {
        return viewConfigResolver.getViewConfigDescriptor(view).getViewId();
    }
    
    /**
     * Gösterilecek olan option pane'lerin listesi
     * @return 
     */
    public List<String> getOptionPanes() {
        return optionPanes;
    }

    /**
     * Verilen isimli pane için view ID'sini döndürür.
     * @param name
     * @return 
     */
    public String getOptionPaneViewId( String name ){
        return optionPaneViews.get(name);
    }

    /**
     * Seçilmiş olan pane için view ID'sini döndürür.
     * @return 
     */
    public String getSelectedPaneViewId(){
        return optionPaneViews.get(selectedPane);
    }
    
    public String getSelectedPane() {
        return selectedPane;
    }

    public void setSelectedPane(String selectedPane) {
        this.selectedPane = selectedPane;
    }

    
}
