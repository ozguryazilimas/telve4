/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.link;

import java.io.Serializable;

/**
 * UI için link modeli
 * 
 * @author Hakan Uygun
 */
public class Link implements Serializable{
    
    private String domainCaption;
    private String caption;
    private String url;
    private String icon;
    private Long pk;
    private String viewId;

    public Link(String domainCaption, String caption, Long pk, String viewId) {
        this.domainCaption = domainCaption;
        this.caption = caption;
        this.pk = pk;
        this.viewId = viewId;
    }

    public Link(String domainCaption, String caption, String url, String icon, Long pk, String viewId) {
        this.domainCaption = domainCaption;
        this.caption = caption;
        this.url = url;
        this.icon = icon;
        this.pk = pk;
        this.viewId = viewId;
    }

    
    
    public String getDomainCaption() {
        return domainCaption;
    }

    public void setDomainCaption(String domainCaption) {
        this.domainCaption = domainCaption;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }
    
    
    
}
