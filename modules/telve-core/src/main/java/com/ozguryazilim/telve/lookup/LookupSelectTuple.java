/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.lookup;

import java.io.Serializable;

/**
 * Lookup Dialoglarından Geri dönüş bilgilerini tutan model sınıf.
 * 
 * @author Hakan Uygun
 */
public class LookupSelectTuple implements Serializable{

    private String expression;
    private Object value;
    private String event;

    public LookupSelectTuple(String expression, Object value) {
        this.expression = expression;
        this.value = value;
    }

    /**
     * Değerin ataması için kullanılacak EL döndürür.
     * @return 
     */
    public String getExpression() {
        return expression;
    }

    /**
     * Değerin atanması için kullanılacak EL'i setler.
     * @param expression 
     */
    public void setExpression(String expression) {
        this.expression = expression;
    }

    /**
     * Atanacak olan değeri döndürür.
     * @return 
     */
    public Object getValue() {
        return value;
    }

    /**
     * ATanack olan değeri setler.
     * @param value 
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Atamadan sonra çağrılacak olan event.
     * @return 
     */
    public String getEvent() {
        return event;
    }

    /**
     * Atamadan sonra çağrılacak olan event.
     * @param event 
     */
    public void setEvent(String event) {
        this.event = event;
    }
        
    
}
