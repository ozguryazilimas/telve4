/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.config;

import com.ozguryazilim.mutfak.kahve.Kahve;
import com.ozguryazilim.mutfak.kahve.KahveEntry;
import com.ozguryazilim.mutfak.kahve.annotations.UserAware;
import com.ozguryazilim.telve.view.Pages;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

/**
 * Kullanıcı için Tema ve Dil seçimlerini ayarlar.
 * 
 * @author Hakan Uygun
 */
@OptionPane(optionPage = Pages.Admin.GuiOptionPane.class)
public class GuiOptionPane extends AbstractOptionPane{
    
    private Map<String, String> themes;
    private String theme;
    private String locale;
    
    @Inject
    private ThemeSelector themeSelector;
    
    @Inject
    private LocaleSelector localeSelector;
    
    @Inject
    private SkinSelector skinSelector;
    
    @Inject @UserAware
    private Kahve kahve;
    
    @PostConstruct
    public void init(){
        themes = new HashMap<String, String>();  
        themes.put("Bootstrap", "bootstrap");  
        themes.put("Aristo", "aristo");  
        themes.put("Black-Tie", "black-tie");  
        themes.put("Blitzer", "blitzer");  
        themes.put("Bluesky", "bluesky");  
        themes.put("Casablanca", "casablanca");  
        themes.put("Cupertino", "cupertino");  
        themes.put("Dark-Hive", "dark-hive");  
        themes.put("Dot-Luv", "dot-luv");  
        themes.put("Eggplant", "eggplant");  
        themes.put("Excite-Bike", "excite-bike");  
        themes.put("Flick", "flick");  
        themes.put("Glass-X", "glass-x");  
        themes.put("Hot-Sneaks", "hot-sneaks");  
        themes.put("Humanity", "humanity");  
        themes.put("Le-Frog", "le-frog");  
        themes.put("Midnight", "midnight");  
        themes.put("Mint-Choc", "mint-choc");  
        themes.put("Overcast", "overcast");  
        themes.put("Pepper-Grinder", "pepper-grinder");  
        themes.put("Redmond", "redmond");  
        themes.put("Rocket", "rocket");  
        themes.put("Sam", "sam");  
        themes.put("Smoothness", "smoothness");  
        themes.put("South-Street", "south-street");  
        themes.put("Start", "start");  
        themes.put("Sunny", "sunny");  
        themes.put("Swanky-Purse", "swanky-purse");  
        themes.put("Trontastic", "trontastic");  
        themes.put("UI-Darkness", "ui-darkness");  
        themes.put("UI-Lightness", "ui-lightness");  
        themes.put("Vader", "vader");  
    }

    public Map<String, String> getThemes() {
        return themes;
    }

    public void setThemes(Map<String, String> themes) {
        this.themes = themes;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
    
    public void saveTheme() {  
        themeSelector.setTheme(theme);
        kahve.put("theme.name", new KahveEntry(theme));
    }

    public String getSkin(){
        return skinSelector.getSkin();
    }
    
    public void setSkin( String skin ){
        skinSelector.setSkin(skin);
    }
    
    public void setSidebar( Boolean sidebarMini  ){
        skinSelector.setSidebarMini(sidebarMini);
    }
    
    public Boolean getSidebar(){
        return skinSelector.getSidebarMini();
    }
    
    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
    
    public List<SelectItem> getSupportedLocales(){
        return localeSelector.getSupportedLocales();
    }
    
    public void saveLocale(){
        localeSelector.setLocaleString(locale);
        kahve.put("locale.name", new KahveEntry(locale));
        //FIXME: #15465
        //RequestContext.getCurrentInstance().getApplicationContext().getCacheProvider().clear();
    }
    
    
}
