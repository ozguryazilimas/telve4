/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import liquibase.integration.cdi.CDILiquibase;

/**
 * PicketLink'in Liqubase'i beklemesi için
 * @author Hakan Uygun
 */
@Singleton
@Startup
public class LiquibaseBootstrap implements Serializable{
    
    @Inject
    private CDILiquibase liquibase;
    
    @PostConstruct
    public void onStartup(){
        liquibase.isInitialized();
    }
}
