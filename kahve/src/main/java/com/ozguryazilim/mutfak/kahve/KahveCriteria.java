/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.mutfak.kahve;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Aynı Anahtar için farklı seviyelerde arama yapmak için.
 *
 *
 * @author Hakan Uygun
 */
public class KahveCriteria implements Serializable {

    private static final String USER_SCOPE = "USER";
    private static final String SYSTEM_SCOPE = "SYSTEM";

    private final List<String> levels;
    private String key;
    private KahveEntry defVal;
    private String defScope;

    protected KahveCriteria() {
        levels = new ArrayList<>();
    }

    public static KahveCriteria create() {
        return new KahveCriteria();
    }

    public KahveCriteria addScope(String scope) {
        levels.add(scope);
        return this;
    }

    public KahveCriteria addKey(String key) {
        this.key = key;
        return this;
    }

    public KahveCriteria addKey(KahveKey key) {
        this.key = key.getKey();
        addDefaultValue(new KahveEntry(key.getDefaultValue()));
        return this;
    }

    public KahveCriteria addDefaultScope(String scope) {
        this.defScope = scope;
        return this;
    }

    public KahveCriteria addDefaultValue(KahveEntry defVal) {
        return addDefaultValue(defVal, USER_SCOPE);
    }

    public KahveCriteria addDefaultValue(String defVal) {
        return addDefaultValue(new KahveEntry( defVal ), USER_SCOPE);
    }
    
    public KahveCriteria addDefaultValue(Integer defVal) {
        return addDefaultValue(new KahveEntry( defVal ), USER_SCOPE);
    }
    
    public KahveCriteria addDefaultValue(Long defVal) {
        return addDefaultValue(new KahveEntry( defVal ), USER_SCOPE);
    }
    
    public KahveCriteria addDefaultValue(BigDecimal defVal) {
        return addDefaultValue(new KahveEntry( defVal ), USER_SCOPE);
    }
    
    public KahveCriteria addDefaultValue(Boolean defVal) {
        return addDefaultValue(new KahveEntry( defVal ), USER_SCOPE);
    }
    
    public KahveCriteria addDefaultValue(Date defVal) {
        return addDefaultValue(new KahveEntry( defVal ), USER_SCOPE);
    }
    
    public KahveCriteria addDefaultValue(KahveEntry defVal, String scope) {
        this.defVal = defVal;
        this.defScope = scope;
        return this;
    }

    public List<String> getKeys() {
        return getKeys(null);
    }

    public List<String> getKeys(String identity) {
        List<String> ls = new ArrayList<>();

        
        if( identity != null && !"".equals(identity)){
            ls.add( getScopeKey(USER_SCOPE, identity));
        } 
        
        for (String s : levels) {
            ls.add( getScopeKey(s, identity));
        }
        
        ls.add( getScopeKey(SYSTEM_SCOPE, identity));

        return ls;
    }

    private String getScopeKey(String scope, String identity) {
        switch (scope) {
            case USER_SCOPE:
                return identity + "::" + key;
            case SYSTEM_SCOPE:
                return key;
            default:
                return scope + "::" + key;
        }
    }

    public boolean hasDefaultValue() {
        return defVal != null;
    }

    public KahveEntry getDefaultValue() {
        return defVal;
    }

    public String getScopeKey( String identity ) {
        return getScopeKey(defScope, identity);
    }
    
    public String getScopeKey() {
        return getScopeKey(defScope, null);
    }

    @Override
    public String toString() {
        return "KahveScope{" + "levels=" + levels + ", key=" + key + ", defVal=" + defVal + ", defScope=" + defScope + '}';
    }

}
