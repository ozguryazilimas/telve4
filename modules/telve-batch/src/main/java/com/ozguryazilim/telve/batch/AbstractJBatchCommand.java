package com.ozguryazilim.telve.batch;

import com.ozguryazilim.telve.messagebus.command.AbstractStorableCommand;
import java.util.Properties;

/**
 * JBtach ( jsr352 ) Job'larını çalıştıracak komutlar için taban sınıf.
 * 
 * @author Hakan Uygun
 */
public abstract class AbstractJBatchCommand extends AbstractStorableCommand{
    
    /**
     * Geriye JBatch Job ismini döndürür.
     * @return 
     */
    public abstract String getJobName();
            
    /**
     * JBatch Job'una basılacak parametreler için properties oluşturulur.
     * @param props 
     */
    protected abstract void buildProperties( Properties props );
    
    /**
     * Geriye JBatch Job'una gönderileç parametreleri döndürür.
     * @return 
     */
    public Properties getProperties(){
        Properties props = new Properties();
        
        buildProperties(props);
        
        return props;
    }
    
}
