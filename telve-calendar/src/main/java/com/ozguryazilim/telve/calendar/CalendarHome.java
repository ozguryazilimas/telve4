/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.calendar;

import java.io.Serializable;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Calendar sistemi UI control sınıfı
 * @author Hakan Uygun
 */
@Named
@WindowScoped 
public class CalendarHome implements Serializable{
    private static final Logger LOG = LoggerFactory.getLogger(CalendarHome.class);
    
    @Inject
    private CalendarFilterModel filterModel;
    
    public void processEvent(){
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String eventSource = params.get("source");
        String eventId = params.get("id");
        
        try{
            CalendarEventSource cec = (CalendarEventSource) BeanProvider.getContextualReference(eventSource);
            cec.process(eventId);
        } catch ( Exception e ){
            LOG.warn("EventSource not found : {}", eventSource);
        }
        
    }
    
}
