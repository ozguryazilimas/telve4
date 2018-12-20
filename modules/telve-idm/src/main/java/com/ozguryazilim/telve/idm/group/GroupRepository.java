package com.ozguryazilim.telve.idm.group;

import com.ozguryazilim.telve.data.TreeRepositoryBase;
import com.ozguryazilim.telve.idm.entities.Group;
import javax.enterprise.context.Dependent;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;

import java.util.List;

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

    public abstract Group findAnyByName(String name);

    public abstract List<Group> findAnyByAutoCreated(Boolean autoCreated);
}
