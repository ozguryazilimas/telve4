package com.ozguryazilim.mutfak.kahve;

import com.ozguryazilim.mutfak.kahve.annotations.UserAware;
import java.sql.SQLException;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
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
