package com.ozguryazilim.telve.query.filters;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.deltaspike.data.api.criteria.Criteria;

/**
 * BaseRepository filteredQuery için filtre tanım modeli.
 * 
 * @author Hakan Uygun
 * @param <E> Entity sınıfı
 * @param <X> Attribute Veri sınıfı
 */
public abstract class Filter<E, X, Y> {
   
    /**
     * Model üzerinde hangi alanı ilgilendirdiği
     */
    private SingularAttribute<? super E, X> attribute;
    
    /**
     * Model üzerinde ilgilenilen alt alan
     */
    private SingularAttribute<? super E, X> subattr;
    
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
    private Y value;
    
    /**
     * Aralık tipi filtreler için ikinci değer
     */
    private Y value2;

    public Filter( SingularAttribute<? super E, X> attribute, String label ){
        this.attribute = attribute;
        this.labelKey = label;
    }
    
    public Filter( SingularAttribute<? super E, X> attribute,SingularAttribute<? super E, X> subattr, String label ){
        this.attribute = attribute;
        this.labelKey = label;
        this.subattr=subattr;
    }

    public Filter(String label) {
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

    public Y getValue() {
        return value;
    }

    public void setValue(Y value) {
        this.value = value;
    }

    public Y getValue2() {
        return value2;
    }

    public void setValue2(Y value2) {
        this.value2 = value2;
    }

    public SingularAttribute<? super E, X> getSubattr() {
        return subattr;
    }

    public void setSubattr(SingularAttribute<? super E, X> subattr) {
        this.subattr = subattr;
    }
    
    public abstract void decorateCriteria( Criteria<E, ?> criteria );
    
    public abstract void decorateCriteriaQuery( List<Predicate> predicates, CriteriaBuilder builder, Root<E> from );
    
    public abstract String getTemplate();
    
    public abstract String serialize();
    
    public abstract void deserialize( String s );
    
}
