/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.qualifiers;

import com.ozguryazilim.telve.entities.EntityBase;
import javax.enterprise.util.AnnotationLiteral;

/**
 *
 * @author oyas
 */
public class EntityQualifierLiteral extends AnnotationLiteral<EntityQualifier> implements EntityQualifier{

    private Class<? extends EntityBase> entity;

    public EntityQualifierLiteral(Class<? extends EntityBase> entity) {
        this.entity = entity;
    }
    
    
    @Override
    public Class<? extends EntityBase> entity() {
        return entity;
    }
    
}
