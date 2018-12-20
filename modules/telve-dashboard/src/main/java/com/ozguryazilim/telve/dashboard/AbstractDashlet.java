package com.ozguryazilim.telve.dashboard;

import com.google.common.base.CaseFormat;
import java.io.Serializable;
import javax.annotation.PostConstruct;

/**
 * Dashlet'ler için taban sınıf.
 * 
 * Mevcut durumda yapılacak bir şey yok gibi. İlerde laım olursa diye...
 * 
 * @author Hakan Uygun
 */
public abstract class AbstractDashlet implements Serializable{
 
    
    @PostConstruct
    public void init(){
        load();
    }
    
    /**
     * Dashlet için caption bilgisi döndürür.
     * @return 
     */
    public String getCaption(){
        return "dashlet.caption." + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, this.getClass().getSimpleName());
    }
    
    /**
     * Dashletin tazelenmesi için GUI'den çağrılır.
     * 
     * Alt sınıfların bu özelliği kullanmak için bu methodu override etmesi gerekir.
     */
    public void refresh(){
        
    }
    
    public void load(){
        
    }
    
    public void save(){
        
    }
}
