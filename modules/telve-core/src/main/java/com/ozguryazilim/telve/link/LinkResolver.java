package com.ozguryazilim.telve.link;

import java.io.Serializable;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sistemde tanımlı domainler için link çözümleyici.
 * 
 * 
 * TODO: Nasıl kullanılacağı belgelenmeli.
 * 
 * @author Hakan Uygun
 */
@Named
@Dependent
public class LinkResolver implements Serializable{
    
    private static final Logger LOG = LoggerFactory.getLogger(LinkResolver.class);
    
    public Link resolve( String domain, String caption,  Long pk ){
        AbstractLinkResolver res = LinkRegistery.getResolver(domain);
        if( res == null ){
            LOG.error("LinkResolver not found for {}", domain);
            return null;
        }
        return res.resolve(caption, pk);
    }
    
}
