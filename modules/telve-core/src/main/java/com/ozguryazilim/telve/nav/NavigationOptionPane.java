/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.nav;

import com.google.common.base.Splitter;
import com.ozguryazilim.mutfak.kahve.Kahve;
import com.ozguryazilim.mutfak.kahve.KahveEntry;
import com.ozguryazilim.mutfak.kahve.annotations.UserAware;
import com.ozguryazilim.telve.config.AbstractOptionPane;
import com.ozguryazilim.telve.config.OptionPane;
import com.ozguryazilim.telve.view.Pages;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.shiro.subject.Subject;

/**
 * Kullanıcı Favori Menüsü oluşturmak için kontroller.
 * 
 * Navigasyon menüsünden seçilmişleri Ana parça olarak sideMenü'ye dizilebilmesi için 
 * kullanıcının seçtiklerini Kahve'de saklar.
 * 
 * Tüm kullanıcılar yetkiye ihtiyaç olmadan erişebilir.
 * @author oyas
 */
@OptionPane( permission = "PUBLIC", optionPage = Pages.Admin.NavigationOptionPane.class)
public class NavigationOptionPane extends AbstractOptionPane{
    
    @Inject @UserAware
    private Kahve kahve;
    
    @Inject
    private Subject identity;
    
    @Inject
    private NagivationController nagivationController;
    
    private List<String> menus = new ArrayList<>();

    @PostConstruct
    public void init(){
        KahveEntry ke = kahve.get("favmenu.count", 0);
        int count = ke.getAsInteger();
        for( int i = 0; i < count; i++ ){
            ke = kahve.get("favmenu.item." + i, "");
            menus.add(ke.getAsString());
        }
    }
    
    public List<String> getMenus() {
        return menus;
    }

    public void setMenus(List<String> menus) {
        this.menus = menus;
    }
    
    /**
     * Virgüller ile ayrılmış listeyi kullanarak menu listesini günceller.
     * @param menu 
     */
    public void updateFavMenu( String menu ){
        List<String> tmp = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(menu);
        
        //Önce eskiden olan şimdi seçilmeyenleri temizle
        menus = menus.stream()
                  .filter(s -> tmp.contains(s))
                  .collect(Collectors.toList());

        //Şimdi de yeni gelenleri alta ekleyelim.
        tmp.stream()
                .filter( t -> !t.startsWith("j")) //Section altında her şey çecilirse section'da geliyor. ve j1_1 gibi birşey oluyor.
                .filter(t -> !menus.contains(t) )
                .forEach(t -> {
                    menus.add(t);
            });
        
    }

    @Override
    public void save() {
        
        kahve.put("favmenu.count", menus.size());
        int i = 0;
        for( String menu : menus ){
            kahve.put("favmenu.item." + i, menu);
            i++;
        }
        
        nagivationController.init();
        
        super.save(); 
    }
    
    
    
}
