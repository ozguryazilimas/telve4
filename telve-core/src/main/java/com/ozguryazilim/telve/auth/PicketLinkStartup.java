/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.Role;
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

        List<User> ls  = identityManager.createIdentityQuery(User.class).setParameter(User.LOGIN_NAME, "telve").getResultList();

        if (ls.isEmpty()) {
            User user = new User("telve");
            identityManager.add(user);

            Password password = new Password("telve");
            identityManager.updateCredential(user, password);
            
            // Create some relationships
            Role r = new Role("admin");
            identityManager.add(r);
            
            BasicModel.grantRole( partitionManager.createRelationshipManager(), user, r);
        }
        // Create a RelationshipManager
        //RelationshipManager relationshipManager = partitionManager.createRelationshipManager();

        

    }
}
