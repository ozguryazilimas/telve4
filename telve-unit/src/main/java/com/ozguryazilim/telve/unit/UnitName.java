/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.unit;

import java.io.Serializable;
import java.util.Objects;

/**
 * Birim Sistemi için isim sınıfı
 * 
 * @author Hakan Uygun
 */
public class UnitName implements Serializable{
    
    private final String dimension;
    private final String name;

    public UnitName(String dimension, String name) {
        this.dimension = dimension;
        this.name = name;
    }

    public String getDimension() {
        return dimension;
    }

    public String getName() {
        return name;
    }

    public static UnitName of( String unitName ){
        String s[] = unitName.split(":");
        //FIXME: parseException fırlat
        return new UnitName( s[0], s[1]);
    }
    
    /**
     * İki Unit Name'in bir biriyle uyumlu olup olmadığını ( yani aynı dimension'a ait olup olmadıklarına ) bakar.
     * @param un
     * @return 
     */
    public boolean compatible( UnitName un ){
        return this.dimension.equals(un.dimension);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.dimension);
        hash = 41 * hash + Objects.hashCode(this.name);
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
        final UnitName other = (UnitName) obj;
        if (!Objects.equals(this.dimension, other.dimension)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return dimension + ":" + name;
    }

    
    
    
}
