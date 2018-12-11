/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.query.filters;

import com.ozguryazilim.telve.entities.EntityBase;
import com.ozguryazilim.telve.entities.TreeNodeEntityBase;
import com.ozguryazilim.telve.lookup.LookupControllerBase;
import com.ozguryazilim.telve.query.Operands;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

/**
 * Ağaç tipi Entitylerin filtresi.
 * 
 * Under operandı ile seçilen entity ve altının kontrolünü sağlar.
 * 
 * @author Hakan Uygun
 * @param <E> Üzerinde sorgu yapılacak Entity sınıfı
 * @param <T> Sorgu için kullanılacak olan değer Entity Sınıfı
 */
public class TreeEntityFilter<E extends EntityBase, T extends TreeNodeEntityBase> extends EntityOverlayFilter<E,T>{
    
    private SingularAttribute<? super T, String> path;
    
    public TreeEntityFilter(SingularAttribute<? super E, T> attribute, SingularAttribute<? super T, String> path, Class<? extends LookupControllerBase<T, ?>> lookupClazz, String label) {
        super(attribute, lookupClazz, label);
        this.path = path;
        setOperands(Operands.getTreeEntityOperands());
    }
    
    @Override
    public void decorateCriteriaQuery(List<Predicate> predicates, CriteriaBuilder builder, Root<E> from) {
        if (getValue() != null) {
            switch (getOperand()) {
                case Equal:
                    predicates.add(builder.equal(from.get(getAttribute()), getValue()));
                    break;
                case NotEqual:
                    predicates.add(builder.notEqual(from.get(getAttribute()), getValue()));
                    break;
                case None:
                    predicates.add(builder.isNull(from.get(getAttribute())));
                    break;
                case Under:
                    predicates.add(builder.like(from.get(getAttribute()).get(path), getValue().getPath() + "%"));
                    break;
                default:
                    break;
            } 
        } else if( getOperand() == FilterOperand.None ){
            predicates.add(builder.isNull(from.get(getAttribute())));
        }
    }
    
}
