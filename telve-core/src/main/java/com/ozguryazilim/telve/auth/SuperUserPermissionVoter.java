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
import org.picketlink.idm.model.IdentityType;
import org.picketlink.idm.permission.spi.PermissionVoter;

/**
 * Sistem Project state'ine bakarak geliştiriciler için her türlü yetkiyi verir.
 * 
 * @author Hakan Uygun
 */
@ApplicationScoped
public class SuperUserPermissionVoter implements PermissionVoter{

    @Inject
    private ProjectStage projectStage;
    
    @Override
    public VotingResult hasPermission(IdentityType it, Object o, String string) {
        if( projectStage == ProjectStage.Development){
            return VotingResult.ALLOW;
        }
        
        return VotingResult.NOT_APPLICABLE;
    }

    @Override
    public VotingResult hasPermission(IdentityType it, Class<?> type, Serializable srlzbl, String string) {
        if( projectStage == ProjectStage.Development){
            return VotingResult.ALLOW;
        }
        return VotingResult.NOT_APPLICABLE;
    }
    
}
