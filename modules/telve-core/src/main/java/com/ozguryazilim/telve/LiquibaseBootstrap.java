package com.ozguryazilim.telve;

import java.io.Serializable;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import liquibase.integration.jakarta.cdi.CDILiquibase;

/**
 * PicketLink'in Liqubase'i beklemesi için
 * @author Hakan Uygun
 */
@Singleton
@Startup
public class LiquibaseBootstrap implements Serializable{
  
    /* FIXME: jakarta: liquibase yeni sürümleri artık böyle şeyler desteklemiyor :( 
        Yeni bir yöntem düşünmek gerekecek */
    @Inject
    private CDILiquibase liquibase;
    
    @PostConstruct
    public void onStartup(){
        liquibase.isInitialized();
    }
         
}
