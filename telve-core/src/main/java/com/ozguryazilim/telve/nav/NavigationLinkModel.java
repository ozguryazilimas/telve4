/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.nav;

import java.io.Serializable;

/**
 * Navigasyonlar UI build etmek için kullanılacak verileri tutan ViewModel sınıfı.
 * @author Hakan Uygun 
 */
public class NavigationLinkModel implements Serializable, Comparable<NavigationLinkModel>{
    
    private String viewId;
    private String icon;
    private String label;
    private Boolean fontIcon = false;
    private Integer order;

    public NavigationLinkModel(String viewId, String label, String icon, Integer order ) {
        this.viewId = viewId;
        this.icon = icon;
        this.label = label;
        this.order = order;
        this.fontIcon = icon.startsWith("/");
    }

    public NavigationLinkModel(String viewId, String label, String icon,  Boolean fontIcon, Integer order) {
        this.viewId = viewId;
        this.icon = icon;
        this.label = label;
        this.fontIcon = fontIcon;
        this.order = order;
    }
    
    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getFontIcon() {
        return fontIcon;
    }

    public void setFontIcon(Boolean fontIcon) {
        this.fontIcon = fontIcon;
    }

    @Override
    public String toString() {
        return "NavigationLinkModel{" + "viewId=" + viewId + ", icon=" + icon + ", label=" + label + '}';
    }

    @Override
    public int compareTo(NavigationLinkModel o) {
        int r = order.compareTo(o.order);
        
        //Eğer sıra nosu aynı ise key'e göre sırala ki ikidebir yerleri değişmesin.
        if( r == 0 ){
            r = label.compareTo(o.label);
        }
        
        return r;
    }
    
}
