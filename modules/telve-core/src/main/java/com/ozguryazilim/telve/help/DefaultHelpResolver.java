package com.ozguryazilim.telve.help;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author oyas
 */
@ApplicationScoped
public class DefaultHelpResolver implements HelpResolver{

    private static final Logger LOG = LoggerFactory.getLogger(DefaultHelpResolver.class);
    
    @Inject
    private FacesContext facesContext;
    
    @Override
    public boolean canHandle() {
        //Her halukarda gelen requestleri kabul eder.
        return true;
    }

    /**
     * Mevcut viewRoot'u kullanarak sadece uzantı değiştirerek topicPath oluşturur.
     * 
     * @return 
     */
    @Override
    public String getHelpPath() {
        
        String topic = facesContext.getViewRoot().getViewId();
    	
    	topic = topic.replace("xhtml", "html");
        topic = topic.charAt(0) == '/' ? topic.substring(1) : topic;
        LOG.debug("Nereden Help istendi : {}", topic );
        
        Boolean featureBase = "true".equals(ConfigResolver.getPropertyValue("telve.help.featureBase", "false"));
        
        if( featureBase ){
            topic = topic.replace("Browse", "");
            topic = topic.replace("View", "");
        }
        
        //String topicPath = "/help/" + LocaleSelector.instance().getLocaleString() + topic;
        String topicPath = "/docs?topic=" + topic;
        
        LOG.debug("Help yolu : {}", topicPath );
        
        return topicPath;
    }

    @Override
    public Integer getOrder() {
        return 1000;
    }
    
}
