/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.quick;

import com.google.common.base.Strings;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kullanıcı için yetki kontrolleri yapılmış QuickRecord bileşenlerini yönetir.
 * 
 * @author Hakan Uygun
 */
@SessionScoped
@Named
public class QuickRecordManager implements Serializable{
    private static final Logger LOG = LoggerFactory.getLogger(QuickRecordManager.class);
    
    private List<String> quickRecords = new ArrayList<>();
    
    @PostConstruct
    public void init(){
        quickRecords.clear();
        buildNav();
    }
    
    protected void buildNav(){
        Subject identity = SecurityUtils.getSubject();
        
        for( Map.Entry<String, QuickRecord> e : QuickRecordRegistery.getQuickRecordMap().entrySet()){
            
            //Eğer menüde görünmeyecek ise başka şeye bakmaya gerek yok.
            if( !e.getValue().showonMenu() ){
                continue;
            }
            
            String perm = e.getValue().permission();
            if( Strings.isNullOrEmpty(perm)){
                perm = e.getKey();
            }
            
            if (!identity.isPermitted( perm + ":exec")) {
                continue;
            }
            
            //Yetki çıkarma kontrolü
            if( "true".equals(ConfigResolver.getPropertyValue("permission.exclude." + perm, "false"))){
                continue;
            }
            
            quickRecords.add(e.getKey());
        }
        
        //TODO: Sıralama yapılacak
    }
    
    /**
     * Geriye eğer quickRecord bileşeni varsa true döner.
     * 
     * MainNavBar'da icon gösterelim mi?
     * 
     * @return 
     */
    public Boolean hasQuickRecords(){
        return !quickRecords.isEmpty();
    }
    
    /**
     * Geriye bean instance'ını döndürür.
     * 
     * @param name
     * @return 
     */
    public QuickRecordBase getQuickRecord( String name ){
        //QuickRecord'lar named bileşenler. Dolayısı ile Registery'e bakmıyoruz.
        return (QuickRecordBase) BeanProvider.getContextualReference(name, true);
    }
    
    public String getMenuPage(){
        //FIXME: Burası config/thema destekli hale gelecek.
        return "/layout/quickRecordMenuPanel.xhtml";
    }

    public List<String> getQuickRecords() {
        return quickRecords;
    }
    
    
    
}
