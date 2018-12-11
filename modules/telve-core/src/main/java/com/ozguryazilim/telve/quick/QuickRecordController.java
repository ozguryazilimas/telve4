/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.quick;

import com.google.common.base.Strings;
import java.io.Serializable;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.WindowScoped;

/**
 * UI'da hangi panel'in gösterileceğini yönetir.
 * 
 * @author Hakan Uygun
 */
@Named
@WindowScoped
public class QuickRecordController implements Serializable{
    
    @Inject
    private QuickRecordManager manager;
   
    //Current QuickRecord.name
    private String name;
    
    public String getDialogViewId(){
        if( Strings.isNullOrEmpty(name)){
            return manager.getMenuPage();
        }
        
        QuickRecordBase quickRecord = manager.getQuickRecord(name);
        //FIXME: Burada NPE kontrolü lazım
        return quickRecord.getPage();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
