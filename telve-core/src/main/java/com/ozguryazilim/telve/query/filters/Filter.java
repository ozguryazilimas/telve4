/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.query.filters;

import java.util.List;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.deltaspike.data.api.criteria.Criteria;

/**
 * BaseRepository filteredQuery için filtre tanım modeli.
 * 
 * @author Hakan Uygun
 * @param <E> Entity sınıfı
 * @param <X> Attribute Veri sınıfı
 */
public abstract class Filter<E, X> {
   
    /**
     * Model üzerinde hangi alanı ilgilendirdiği
     */
    private SingularAttribute<? super E, X> attribute;
    
    /**
     * Ekranda hangi metni gösterecek
     */
    private String labelKey;
    /**
     * Ekranda kullanıcıya hint
     */
    private String hintKey;

    /**
     * Desteklenen operand listesi
     */
    private List<FilterOperand> operands;
    
    /**
     * Seçilen operand
     */
    private FilterOperand operand;
    
    /**
     * Seçilen değer
     */
    private X value;
    
    /**
     * Aralık tipi filtreler için ikinci değer
     */
    private X value2;

    public Filter( SingularAttribute<? super E, X> attribute, String label ){
        this.attribute = attribute;
        this.labelKey = label;
    }
    
    public SingularAttribute<? super E, X> getAttribute() {
        return attribute;
    }

    public void setAttribute(SingularAttribute<? super E, X> attribute) {
        this.attribute = attribute;
    }

    public String getLabelKey() {
        return labelKey;
    }

    public void setLabelKey(String labelKey) {
        this.labelKey = labelKey;
    }

    public String getHintKey() {
        return hintKey;
    }

    public void setHintKey(String hintKey) {
        this.hintKey = hintKey;
    }

    public List<FilterOperand> getOperands() {
        return operands;
    }

    public void setOperands(List<FilterOperand> operands) {
        this.operands = operands;
    }

    public FilterOperand getOperand() {
        return operand;
    }

    public void setOperand(FilterOperand operand) {
        this.operand = operand;
    }

    public X getValue() {
        return value;
    }

    public void setValue(X value) {
        this.value = value;
    }

    public X getValue2() {
        return value2;
    }

    public void setValue2(X value2) {
        this.value2 = value2;
    }
    
    public abstract void decorateCriteria( Criteria<E, ?> criteria );
    
    public abstract String getTemplate();
}
