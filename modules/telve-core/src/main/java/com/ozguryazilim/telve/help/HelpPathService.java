package com.ozguryazilim.telve.help;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.ConfigResolver;
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
@ApplicationScoped
public class HelpPathService {
    
    private static final Logger LOG = LoggerFactory.getLogger(HelpPathService.class);
    
    @Inject
    private HelpResolverSelector helpResolverSelector;
    
    public String getHelpPath(){
        
        HelpResolver resolver = helpResolverSelector.getResolver();
        
        if( resolver == null ){
            LOG.warn("Resolver Bulunamadı");
            return "";
        }
        
    	String topic = resolver.getHelpPath();
        String helpDomain = ConfigResolver.getProjectStageAwarePropertyValue("telve.help.domain", "/infocenter");
    	
        //String topicPath = "/help/" + LocaleSelector.instance().getLocaleString() + topic;
        String topicPath = helpDomain + topic;
        
        LOG.debug("Help yolu : {}", topicPath );
        
        return topicPath;
    }
    
}
