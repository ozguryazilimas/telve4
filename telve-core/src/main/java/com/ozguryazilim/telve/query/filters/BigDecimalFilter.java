/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.query.filters;

import com.google.common.base.Splitter;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.metamodel.SingularAttribute;

/**
 *
 * @author oyas
 */
public class BigDecimalFilter<E> extends NumberFilter<E, BigDecimal>{
    
    public BigDecimalFilter(SingularAttribute<? super E, BigDecimal> attribute, String label) {
        super(attribute, label, BigDecimal.ZERO);
    }
    
    @Override
    public void deserialize(String s) {
        List<String> ls = Splitter.on("::").trimResults().splitToList(s);
        setOperand(FilterOperand.valueOf(ls.get(0)));
        setValue(new BigDecimal(ls.get(1)));
        
    }
}
