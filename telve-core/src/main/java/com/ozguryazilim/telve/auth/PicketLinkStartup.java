/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.auth;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.PermissionManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.User;

/**
 *
 * @author haky
 */
@Singleton
@Startup
public class PicketLinkStartup {
    @Inject 
    private PartitionManager partitionManager;

    @PostConstruct
    public void create() {
        // Create an IdentityManager
        IdentityManager identityManager = partitionManager.createIdentityManager();

        User user = new User("telve");
        identityManager.add(user);

        Password password = new Password("telve");
        identityManager.updateCredential(user, password);
        
        // Create a RelationshipManager
        //RelationshipManager relationshipManager = partitionManager.createRelationshipManager();
        
        // Create some relationships
        
        PermissionManager pm = partitionManager.createPermissionManager();
        pm.grantPermission(user, "BBB", "CREATE");
        
    }
}
