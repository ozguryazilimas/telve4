package com.ozguryazilim.telve.ribbon;

import com.ozguryazilim.telve.ribbon.model.RibbonTab;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.inject.Named;
import org.picketlink.Identity;

/**
 * RibbonRegistery üzerinde aktif olan kullanıcın haklarına göre tab listesini
 * oluşturur
 *
 * Ribbon web üzerinde cachelemek lazım. Ama her seferinde üretilecek olan bir
 * tab var?!
 *
 * FIXME: Login sayfası değiştikten sonra session scope'a alınmalı
 *
 * @author Hakan Uygun
 *
 */
@Named
@SessionScoped
public class RibbonController implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer hitCounter = 0;
    
    /**
     * Kullanıcı için oluştutulmuş tab listesi
     */
    private List<RibbonTab> tabs = new ArrayList<RibbonTab>();

    @Inject @Any
    private Identity identity;
    
    /**
     * Init seam component
     */
    @PostConstruct
    public void init() {
        initTabs();
    }

    /**
     * Kullanıcı haklarına bakarak tab listesini oluşturur.
     */
    protected void initTabs() {

        RibbonUtils ru = new RibbonUtils();
        tabs = ru.findUserTabs(identity );
        ru.optimizeRibbon(tabs);
    }

    /**
     * Kullanıcının yetkisi olan ribbon tabların listesini döndürür.
     *
     * @return
     */
    public List<RibbonTab> getTabs() {
        if( tabs.isEmpty() ){
            System.out.println("RibbonTab'lar toplanacak");
            initTabs();
        }
        return tabs;
    }

    public Integer getHitCounter(){
        hitCounter++;
        return hitCounter;
    }
}
