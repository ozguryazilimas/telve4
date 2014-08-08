/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.mutfak.telve.auth;

import com.ozguryazilim.mutfak.telve.auth.model.ResourcePermission;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import org.picketlink.event.PartitionManagerCreateEvent;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.config.IdentityConfiguration;
import org.picketlink.idm.config.IdentityConfigurationBuilder;
import org.picketlink.idm.config.SecurityConfigurationException;
import org.picketlink.idm.jpa.model.sample.simple.AccountTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.AttributeTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.GroupTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.IdentityTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.PartitionTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.PasswordCredentialTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.RelationshipIdentityTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.RelationshipTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.RoleTypeEntity;
import org.picketlink.idm.model.Relationship;
import org.picketlink.idm.model.basic.Realm;
import org.picketlink.internal.EEJPAContextInitializer;

/**
 * PicketLink ayarlarını yapar.
 *
 * @author Hakan Uygun
 */
@ApplicationScoped
public class PicketLinkConfiguration {

    @Inject
    private EEJPAContextInitializer contextInitializer;
    
    private IdentityConfiguration identityConfig = null;

    @Produces
    public IdentityConfiguration createConfig() {
        if (identityConfig == null) {
            initConfig();
        }
        return identityConfig;
    }

    /**
     * This method uses the IdentityConfigurationBuilder to create an
     * IdentityConfiguration, which defines how PicketLink stores
     * identity-related data. In this particular example, a JPAIdentityStore is
     * configured to allow the identity data to be stored in a relational
     * database using JPA.AttributedTypeEntity.class,
     */
    @SuppressWarnings("unchecked")
    private void initConfig() {
        System.out.println("Telve içinden PL initConfig");
        IdentityConfigurationBuilder builder = new IdentityConfigurationBuilder();

        builder
                .named("default")
                .stores()
                .jpa()
                .mappedEntity(
                        AccountTypeEntity.class,
                        RoleTypeEntity.class,
                        GroupTypeEntity.class,
                        IdentityTypeEntity.class,
                        RelationshipTypeEntity.class,
                        RelationshipIdentityTypeEntity.class,
                        PartitionTypeEntity.class,
                        PasswordCredentialTypeEntity.class,
                        AttributeTypeEntity.class,
                        ResourcePermission.class)
                .supportGlobalRelationship(Relationship.class)
                .addContextInitializer(this.contextInitializer)
                // Specify that this identity store configuration supports all features
                .supportAllFeatures();
        identityConfig = builder.build();
    }

    public void configureDefaultPartition(@Observes PartitionManagerCreateEvent event) {
        System.out.println("PL Partition config ");
        PartitionManager partitionManager = event.getPartitionManager();
        createDefaultPartition(partitionManager);
    }

    private void createDefaultPartition(PartitionManager partitionManager) {
        System.out.println("Çalışıyor mu? : createDefaultPartition");
        Realm partition = partitionManager.getPartition(Realm.class, Realm.DEFAULT_REALM);
        if (partition == null) {
            try {
                partition = new Realm(Realm.DEFAULT_REALM);
                partitionManager.add(partition);
            } catch (Exception e) {
                throw new SecurityConfigurationException("Could not create default partition.", e);
            }
        }
    }
}
