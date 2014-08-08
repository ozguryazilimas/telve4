/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve;

import com.ozguryazilim.telve.ribbon.RibbonUtils;
import com.ozguryazilim.telve.ribbon.model.RibbonTab;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.inject.Named;
import org.picketlink.Identity;

/**
 *
 * @author haky
 */
@Named
@SessionScoped
public class Deneme implements Serializable{
    
    private Integer hitCounter = 0;
    
    @Inject @Any
    private Identity identity;
    
    /**
     * Kullanıcı için oluştutulmuş tab listesi
     */
    private List<RibbonTab> tabs = new ArrayList<RibbonTab>();
    
    public Integer getHitCounter(){
        hitCounter++;
        return hitCounter;
    }
    
    public List<RibbonTab> getTabs() {
        if( tabs.isEmpty() ){
            System.out.println("RibbonTab'lar toplanacak");
            initTabs();
        }
        return tabs;
    }
    
    /**
     * Kullanıcı haklarına bakarak tab listesini oluşturur.
     */
    protected void initTabs() {
        
        RibbonUtils ru = new RibbonUtils();
        tabs = ru.findUserTabs(identity );
        ru.optimizeRibbon(tabs);
    }
}
