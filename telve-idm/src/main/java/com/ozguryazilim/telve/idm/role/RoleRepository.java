/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.role;

import com.ozguryazilim.telve.data.ParamRepositoryBase;
import com.ozguryazilim.telve.idm.entities.Role;
import com.ozguryazilim.telve.idm.entities.Role_;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityGraph;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;

/**
 * Role Repository
 * 
 * @author Hakan Uygun
 */
@Repository
@Dependent
public abstract class RoleRepository extends ParamRepositoryBase<Role, Role> implements CriteriaSupport<Role>{
    
    @Override
    protected Class<Role> getViewModelClass() {
        return Role.class;
    }

    @Override
    protected SingularAttribute<? super Role, Long> getIdAttribute() {
        return Role_.id;
    }

    //TODO: Buradaki olanağı biraz araştırmak lazım. EntityGraph işi aslında bayağı bir şeyden bizi kurtarabilir
    public Role findRoleWithPermission( Long id ){
        EntityGraph<Role> gr = entityManager().createEntityGraph(Role.class);
        gr.addSubgraph(Role_.permissions);
        
        Map<String, Object> hints = new HashMap<>();
        
        hints.put("javax.persistence.loadgraph", gr);
        
        return entityManager().find(Role.class, id, hints);
    }
    
    
}
