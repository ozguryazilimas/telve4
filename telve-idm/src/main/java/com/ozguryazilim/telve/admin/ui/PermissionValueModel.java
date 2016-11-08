/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.admin.ui;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Kullanıcı tarafından girilen ya da veri tabanında olan verilerin UI için düzenlenmiş hali.
 * 
 * @author Hakan Uygun
 */
public class PermissionValueModel {
   
    //domain, permission için scope değeri tutar.
    private Map<String,Map<String,String>> values = new HashMap<>();
    
    /**
     * Mecut verileri siler
     */
    public void clear(){
        values.clear();
    }
    
    
    public Map<String,String> getDomainActions( String domain ){
        Map<String,String> result = values.get(domain);
        
        if( result == null ){
            result = new HashMap<>();
            values.put(domain, result);
        }
        
        return result;
    }
    
    public void setValue( String domain, String action, String value ){
        //Eğer gelen değer NONE ise siliyoruz.
        if( "NONE".equals(value)){
            getDomainActions(domain).remove(action);
        } else {
            //Aksi halde değeri yazıyoruz
            if( "*".equals(value)){
                value = "ALL";
            } if( value.startsWith("$")){
                value = value.substring(1);
            }
            getDomainActions(domain).put(action, value);
        }
    }
    
    public String getValue( String domain, String action ){
        
        String result = getDomainActions(domain).get(action);
        
        //Eğer değer yoksa NONE dönüyoruz.
        if( Strings.isNullOrEmpty(result)){
            return "NONE";
        }
        
        return result;
    }
    
    /**
     * Shiro wildcardPermission stringini parse edip keyıt eder.
     * 
     * @param permStr 
     */
    public void setPermissionValues( String permStr ){
       List<String> parts = Splitter.on(':').trimResults().omitEmptyStrings().splitToList(permStr);
       List<String> actions = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(parts.get(1)); 
       
        //parts : 0 domain isimleri. Shiro domain ismi içinde çeşit kabul etmesine rağmen biz tek bir domain tutuyoruz.
        //parts : 1 action/permission stringleri
        //parts : 2 eğer varsa scope değerleri yoksa * 
        actions.forEach((act) -> {
            setValue(parts.get(0), act, parts.size() > 2 ? parts.get(2) : "*");
        });
    }
    
    public void setPermissionValues( List<String> permStrs ){
        permStrs.forEach((s) -> {
            setPermissionValues(s);
        });
    }
    
    /**
     * Elindeki değerleri Shiro wildcard formatında döndürür.
     * 
     * domain:action,action,action:scope formatı destekleniyor.
     * 
     * @return 
     */
    public List<String> getPermissionValues(){
        List<String> result = new ArrayList<>();
        
        //Map<Domain<Scope,List<Action>>
        Map<String, Map<String,List<String>>> scopeMap = new HashMap<>();
        //Önce domain, scope, action şeklinde bir transpose ediyoruz.
        for( Map.Entry<String, Map<String,String>> e : values.entrySet()){
            
            //Önce domaini bulalım
            Map<String,List<String>> dm = scopeMap.get(e.getKey());
            if( dm == null ){
                dm = new HashMap<>();
                scopeMap.put(e.getKey(), dm);
            }
            
            //Şimdi scope'a bakacağız
            for( Map.Entry<String,String> a : e.getValue().entrySet()){
                
                //Scope'a bağlı actionlar için
                List<String> ls = dm.get(a.getValue());
                if( ls == null ){
                    ls = new ArrayList<>();
                    dm.put(a.getValue(), ls);
                }
                
                ls.add(a.getKey());
                
            }
            
            
        }
        
        //Şimdi transpose edilmiş liste üzerinde dönüp stringleri hazırlıyoruz.
        for( Map.Entry<String, Map<String,List<String>>> e : scopeMap.entrySet()){
        
            for( Map.Entry<String,List<String>> a : e.getValue().entrySet()){
                
                //Actionları bir araya topladık
                String acts = Joiner.on(',').join( a.getValue());
                //Şimdide sonucu yazıyoruz : domain:action,action:scope
                String scp;
                if( "ALL".equals(a.getKey()) ){
                    scp =  "*";
                } else {
                    scp = "$" + a.getKey();
                }
                result.add( e.getKey() + ":" + acts + ":" + scp );
            }
            
        }
        
        
        return result;
    }
}
