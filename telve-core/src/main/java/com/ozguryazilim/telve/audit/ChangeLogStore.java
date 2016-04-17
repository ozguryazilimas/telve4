/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.audit;

import com.ozguryazilim.telve.entities.AuditLogDetail;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * AuditLog için değişim kontrollerı.
 * 
 * @author Hakan Uygun
 */
public class ChangeLogStore implements Serializable{
   
    private Map<String,ValueTuple> values = new HashMap<>();
    
    public void clear(){
        values.clear();
    }
    
    public void addOldValue( String key, String value ){
        values.put(key, new ValueTuple(value));
    }
    
    /**
     * Değerin sonuna yeni gelen değeri virgülle ekler.
     * 
     * @param key
     * @param value 
     */
    public void appendOldValue( String key, String value ){
        ValueTuple vt = values.get(key);
        if( vt == null ){
            values.put(key, new ValueTuple(value));
        } else {
            vt.setOldValue( vt.getOldValue() + "," + value );
        }
    }
    
    public void addNewValue( String key, String value ){
        ValueTuple vt = values.get(key);
        if( vt != null ){
            vt.setNewValue(value);
        } else {
            //Yoksa yeni kayıt ekle
            vt = new ValueTuple("");
            vt.setNewValue(value);
            values.put(key, vt );
        }
    }
    
    public void appendNewValue( String key, String value ){
        ValueTuple vt = values.get(key);
        if( vt != null ){
            if( vt.getNewValue() != null ){
                vt.setNewValue(vt.getNewValue() + "," + value);
            } else {
                vt.setNewValue(value);
            }
        } else {
            //Yoksa yeni kayıt ekle
            vt = new ValueTuple("");
            vt.setNewValue(value);
            values.put(key, vt );
        }
    }
    

    /**
     * Geriye değişen verilerden oluşan listesi döndürür.
     * @return 
     */
    public List<AuditLogDetail> getChangeValues(){
        List<AuditLogDetail> ls = new ArrayList<>();
        
        
        for( Entry<String,ValueTuple> vt : values.entrySet()){
            if( vt.getValue() == null ) continue;
            if( ( vt.getValue().getOldValue() == null && vt.getValue().getNewValue() != null ) 
                    || ( vt.getValue().getOldValue() != null && !vt.getValue().getOldValue().equals(vt.getValue().getNewValue()))){
                AuditLogDetail au = new AuditLogDetail();
                au.setAttribute(vt.getKey());
                au.setOldValue(vt.getValue().getOldValue());
                au.setNewValue(vt.getValue().getNewValue());
                
                ls.add(au);
            }
            
        }
        
        return ls;
    }
    
    public class ValueTuple{
        private String oldValue;
        private String newValue;

        public ValueTuple(String oldValue) {
            this.oldValue = oldValue;
        }

        public String getOldValue() {
            return oldValue;
        }

        public void setOldValue(String oldValue) {
            this.oldValue = oldValue;
        }

        public String getNewValue() {
            return newValue;
        }

        public void setNewValue(String newValue) {
            this.newValue = newValue;
        }
        
        
    }
}
