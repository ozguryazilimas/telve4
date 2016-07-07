/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.group;

import com.ozguryazilim.telve.data.TreeRepositoryBase;
import com.ozguryazilim.telve.idm.entities.Group;
import javax.enterprise.context.Dependent;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;

/**
 *
 * @author oyas
 */
@Repository
@Dependent
public abstract class GroupRepository extends TreeRepositoryBase<Group> implements CriteriaSupport<Group>{
    
    /**
     * Geriye yeni bir Group oluşturup döndürür.
     *
     * @param parent
     * @return
     */
    public Group newGroup(Group  parent) {
        Group entity = new Group();
        entity.setParent(parent);
        return entity;
    }
    
}
