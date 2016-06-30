/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.CachingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.util.Initializable;
import org.apache.shiro.web.env.DefaultWebEnvironment;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author oyas
 */
public class TelveWebEnvironment extends DefaultWebEnvironment implements Initializable{
    
    private static final Logger LOG = LoggerFactory.getLogger(TelveWebEnvironment.class);
    
    @Override
    public void init() {
        LOG.info("Telve Web Environment");
        WebSecurityManager securityManager = createWebSecurityManager();
        setWebSecurityManager(securityManager);

    }
    
    protected WebSecurityManager createWebSecurityManager() {
        /*WebIniSecurityManagerFactory factory;
        Ini ini = getIni();
        if (CollectionUtils.isEmpty(ini)) {
            factory = new WebIniSecurityManagerFactory();
        } else {
            factory = new WebIniSecurityManagerFactory(ini);
        }

        WebSecurityManager wsm = (WebSecurityManager)factory.getInstance();

        //SHIRO-306 - get beans after they've been created (the call was before the factory.getInstance() call,
        //which always returned null.
        Map<String, ?> beans = factory.getBeans();
        if (!CollectionUtils.isEmpty(beans)) {
            this.objects.putAll(beans);
        }*/
        
        SecurityManagerConfig config = BeanProvider.getContextualReference(SecurityManagerConfig.class, true);
        
        
        CacheManager cm = new MemoryConstrainedCacheManager();
        
        for( Realm r : config.getRealms() ){
            LOG.info("Actived Realm : {}", r.getName());
            if( r instanceof CachingRealm ){
                ((CachingRealm)r).setCacheManager(cm);
            }
        }
        
        DefaultWebSecurityManager wsm = new DefaultWebSecurityManager( config.getRealms());
        wsm.setCacheManager(cm);
        return wsm;
    }
    
}
