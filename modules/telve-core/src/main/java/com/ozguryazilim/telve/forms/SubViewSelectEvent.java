package com.ozguryazilim.telve.forms;

import java.io.Serializable;

/**
 * SubView seçimi duyurmak için CDI Event
 * @author Hakan Uygun
 */
public class SubViewSelectEvent implements Serializable{
    
    private String subViewId;

    public SubViewSelectEvent(String subViewId) {
        this.subViewId = subViewId;
    }
    
    public String getSubViewId() {
        return subViewId;
    }

    public void setSubViewId(String subViewId) {
        this.subViewId = subViewId;
    }
    
    
}
