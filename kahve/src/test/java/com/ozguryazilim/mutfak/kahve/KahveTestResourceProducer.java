/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.mutfak.kahve;

import com.ozguryazilim.mutfak.kahve.annotations.UserAware;
import java.sql.SQLException;
import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

/**
 *
 * @author haky
 */
@Dependent
public class KahveTestResourceProducer {
    
    @Resource(mappedName = "java:jboss/datasources/TestDS" )
    private DataSource dataSource;
    
    @Produces @UserAware
    public String getIdentity(){
        return "Hakan";
    }
    
    @Produces
    @com.ozguryazilim.mutfak.kahve.annotations.Kahve
    public DataSource createDataSource() throws SQLException {
        return dataSource;
    }
    
}
