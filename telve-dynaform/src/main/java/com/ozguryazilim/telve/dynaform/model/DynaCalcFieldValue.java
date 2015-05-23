/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.dynaform.model;

import java.io.Serializable;

/**
 * DynaCalcField'lar için yapılan hesaplama ve result sonuçlarını tutar
 * @author Hakan Uygun
 */
public class DynaCalcFieldValue implements Serializable{
    
    private String id;
    private Long value;
    private String result;
    private String style;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public String toString() {
        return "DynaCalcFieldValue{" + "id=" + id + ", value=" + value + ", result=" + result + ", style=" + style + '}';
    }
    
    
    
}
