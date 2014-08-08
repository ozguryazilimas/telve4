/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.mutfak.telve.auth;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.permission.spi.PermissionVoter;

/**
 * Yetki kontrol üreticisi
 *
 * @author Hakan Uygun
 */
@ApplicationScoped
public class SuperUserPermissionVoterProducer {

    @Produces
    @ApplicationScoped
    public PermissionVoter producePermissionVoter(PartitionManager partitionManager) {
        return new SuperUserPermissionVoter();

    }
}
