/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.unit;

import java.io.Serializable;
import java.util.Objects;

/**
 * Birim Tanımı
 * 
 * @author Hakan Uygun
 */
public class Unit implements Serializable{
    
    private final UnitName name;
    private final QuantitativeAmount base;

    public Unit(UnitName name, QuantitativeAmount base) {
        this.name = name;
        this.base = base;
    }

    public UnitName getName() {
        return name;
    }

    public QuantitativeAmount getBase() {
        return base;
    }
    
    public boolean isBaseUnit(){
        return this.name.equals(base.getUnitName());
    }

    @Override
    public String toString() {
        return "Unit{" + "name=" + name + ", base=" + base + '}';
    }

    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.name);
        hash = 11 * hash + Objects.hashCode(this.base);
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
        final Unit other = (Unit) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.base, other.base)) {
            return false;
        }
        return true;
    }
    
    
    
}
