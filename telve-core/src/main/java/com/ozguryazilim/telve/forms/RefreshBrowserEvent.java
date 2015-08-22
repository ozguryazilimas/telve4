/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.forms;

/**
 * Browserlar için tazeleme event'i.
 * 
 * Edit formlarından fırlatılır.
 * 
 * @author Hakan Uygun
 */
public class RefreshBrowserEvent {
   
    private String domain;
    private Long pk;

    public RefreshBrowserEvent(String domain, Long pk ) {
        this.domain = domain;
        this.pk = pk;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    
    
}
