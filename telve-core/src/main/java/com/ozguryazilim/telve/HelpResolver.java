/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Context Sensitive Help için resolver.
 * 
 * Bulunulan ekran bilgisini kullanarak help yolu döndürür.
 * 
 * @author Hakan Uygun
 */
@Named
@RequestScoped
public class HelpResolver {
    
    private static final Logger LOG = LoggerFactory.getLogger(HelpResolver.class);
    
    @Inject
    private FacesContext facesContext;
    
    public String getHelpPath(){
        
        
    	String topic = facesContext.getViewRoot().getViewId();
    	
    	topic = topic.replace("xhtml", "html");
        topic = topic.charAt(0) == '/' ? topic.substring(1) : topic;
        LOG.info("Nereden Help istendi : {}", topic );
        
        //String topicPath = "/help/" + LocaleSelector.instance().getLocaleString() + topic;
        String topicPath = "/infocenter/docs?topic=" + topic;
        
        LOG.info("Help yolu : {}", topicPath );
        
        return topicPath;
    }
    
}
