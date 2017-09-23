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
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;

/**
 *
 * @author oyas
 */
public abstract class AbstractOptionPaneController implements Serializable{

    private final List<String> optionPanes = new ArrayList<>();
    private final Map<String, String> optionPaneViews = new HashMap<>();
    
    @Inject
    private ViewConfigResolver viewConfigResolver;

    private String selectedPane;
    
    @PostConstruct
    public void init(){
        buildPaneList();
        //İsim sırasına sokalım
        Collections.sort(optionPanes);
        
        if( !optionPanes.isEmpty() ){
            selectedPane = optionPanes.get(0);
        }
    }

    /**
     * Gösterilecek olan Pane listesi hazırlanır.
     */
    protected abstract void buildPaneList();
    
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

    public Map<String, String> getOptionPaneViews() {
        return optionPaneViews;
    }

    
}
