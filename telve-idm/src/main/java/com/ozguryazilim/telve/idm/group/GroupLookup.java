/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.group;

import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.idm.config.IdmPages;
import com.ozguryazilim.telve.idm.entities.Group;
import com.ozguryazilim.telve.idm.entities.Group_;
import com.ozguryazilim.telve.lookup.Lookup;
import com.ozguryazilim.telve.lookup.LookupTreeControllerBase;
import javax.inject.Inject;

/**
 *
 * @author oyas
 */
@Lookup(dialogPage = IdmPages.GroupLookup.class)
public class GroupLookup extends LookupTreeControllerBase<Group, Group>{

    @Inject
    private GroupRepository repository;
    
    @Override
    protected RepositoryBase<Group, Group> getRepository() {
        return repository;
    }

    @Override
    public String getCaptionFieldName() {
        return Group_.name.getName();
    }
    
}
