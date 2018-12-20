package com.ozguryazilim.telve.nav;

import java.io.Serializable;
import java.util.Objects;

/**
 * Navigasyonlar UI build etmek için kullanılacak verileri tutan ViewModel sınıfı.
 * @author Hakan Uygun 
 */
public class NavigationLinkModel implements Serializable, Comparable<NavigationLinkModel>{
    
    private String viewId;
    private String icon;
    private String label;
    private Boolean fontIcon = false;
    private Boolean bundleKey = false;
    private Integer order;

    public NavigationLinkModel(String viewId, String label, String icon, Integer order ) {
        this.viewId = viewId;
        this.icon = icon;
        this.label = label;
        this.order = order;
        this.fontIcon = !icon.startsWith("/");
        this.bundleKey = icon.startsWith("feature.") || icon.startsWith("nav.icon.");
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

    public Boolean getBundleKey() {
        return bundleKey;
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.label);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NavigationLinkModel other = (NavigationLinkModel) obj;
        if (!Objects.equals(this.label, other.label)) {
            return false;
        }
        return true;
    }

    
    
    
    
}
