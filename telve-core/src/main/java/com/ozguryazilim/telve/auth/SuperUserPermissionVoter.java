/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.auth;

import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.projectstage.ProjectStage;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.IdentityType;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.model.basic.User;
import org.picketlink.idm.permission.spi.PermissionVoter;

/**
 * superuser rolüne sahip kullanıcıların her türlü yetkisi vardır.
 * 
 * @author Hakan Uygun
 */
@ApplicationScoped
public class SuperUserPermissionVoter implements PermissionVoter{

    @Inject
    private ProjectStage projectStage;
    
    @Inject
    private RelationshipManager relationshipManager;
    
    @Inject
    private IdentityManager identityManager;
    
    @Override
    public VotingResult hasPermission(IdentityType it, Object o, String string) {
        
        if( identityManager == null ){
            return VotingResult.NOT_APPLICABLE;
        }
        
        if( relationshipManager == null ){
            return VotingResult.NOT_APPLICABLE;
        }
        
        User u = (User) it;
        Role r = BasicModel.getRole(identityManager, "superuser");
        if( BasicModel.hasRole(relationshipManager, it, r) ){
            return VotingResult.ALLOW;
        }
        
        
        return VotingResult.NOT_APPLICABLE;
    }

    @Override
    public VotingResult hasPermission(IdentityType it, Class<?> type, Serializable srlzbl, String string) {
        
        User u = (User) it;
        Role r = BasicModel.getRole(identityManager, "superuser");
        if( BasicModel.hasRole(relationshipManager, it, r) ){
            return VotingResult.ALLOW;
        }
        
        return VotingResult.NOT_APPLICABLE;
    }
    
}
