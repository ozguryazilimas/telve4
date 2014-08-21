/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.dashboard;

import com.ozguryazilim.telve.config.TelveConfigRepository;
import com.ozguryazilim.telve.entities.Option;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.ConfigResolver;

/**
 * Uygulama Haberleri Veren bir dahslet.
 * 
 * Haberleri nereden çekeceğini config'den okur.
 * 
 * @author Hakan Uygun
 */
@Dashlet(capability = {DashletCapability.canHide, DashletCapability.canEdit, DashletCapability.canMinimize, DashletCapability.canRefresh})
public class AppNewsDashlet extends AbstractDashlet{
    
    
    private Integer feedCount = 3;
    
    @Inject
    private TelveConfigRepository configRepository;
    
    /**
     * Haberleri çekeceği URL'i  döndürür.
     * 
     * Varsayılan olarak Özgür Yazılım A.Ş.'den haber döndürür.
     * @return 
     */
    public String getNewsURL(){
        return ConfigResolver.getProjectStageAwarePropertyValue("application.news.url", "http://www.ozguryazilim.com.tr/category/haberler/feed");
    }

    /**
     * Dashlet Başlığını döndürür.
     * 
     * Config'den alır varsayılan değer olarak Özgür Yazılım A.Ş. döndürür.
     * 
     * @return 
     */
    @Override
    public String getCaption(){
        return ConfigResolver.getProjectStageAwarePropertyValue("application.news.caption", "Özgür Yazılım A.Ş.");
    }

    /**
     * Gösterilecek feed sayısını döndürür.
     * @return 
     */
    public Integer getFeedCount() {
        return feedCount;
    }

    /**
     * Gösterilecek feed sayısını setler.
     * @param feedCount 
     */
    public void setFeedCount(Integer feedCount) {
        this.feedCount = feedCount;
    }
    
    @Override
    public void load(){
        Option o = configRepository.getUserAwareOption("appNewsDashlet.feedCount");
        if( o != null ){
            feedCount = o.getAsInteger();
        }
    }
    
    @Override
    public void save(){
        Option o = new Option("appNewsDashlet.feedCount");
        o.setAsInteger(feedCount);
        configRepository.saveUserAvareOption(o);
    }
}
