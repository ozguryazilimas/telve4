/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.config;

import com.ozguryazilim.telve.entities.Option;
import com.ozguryazilim.telve.view.Pages;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * Sistem yöneticileri tarafından tüm optionların görülebileceği bir option paneli.
 * 
 * @author Hakan Uygun
 */
@OptionPane(optionPage = Pages.Admin.AllOptionsPane.class)
public class AllOptionsPane extends AbstractOptionPane {

    private List<Option> options;
            
    
    @Inject
    private EntityManager entityManager;
    
    @PostConstruct
    public void init(){
        populateOptions();
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
    
    
    protected void populateOptions(){
        options = entityManager.createQuery("select c from Option c").getResultList();
    }
    
    public void refresh(){
        populateOptions();
    }
}
