package com.ozguryazilim.telve.workarounds;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Default;
import jakarta.interceptor.Interceptor;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.transaction.UserTransaction;
import org.apache.deltaspike.jpa.impl.transaction.BeanManagedUserTransactionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WildFly ile java:comp/UserTransaction bulunamyor. Çevresinden dolaşıyoruz.
 *
 * https://issues.apache.org/jira/browse/DELTASPIKE-552
 *
 * @author Hakan Uygun
 */
@Dependent
@Alternative
@Priority(Interceptor.Priority.APPLICATION + 1)
@Default
public class TelveUserTransactionStrategy extends BeanManagedUserTransactionStrategy {
    
    private static final Logger LOG = LoggerFactory.getLogger(TelveUserTransactionStrategy.class);
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.deltaspike.jpa.impl.transaction.BeanManagedUserTransactionStrategy
     * #resolveUserTransaction()
     */

    @Override
    protected UserTransaction resolveUserTransaction() {
        
        //LOG.info(USER_TRANSACTION_JNDI_NAME);
        
        InitialContext context = null;
        try {
            context = new InitialContext();
            UserTransaction returnUserTransaction = (jakarta.transaction.UserTransaction) context
                    .lookup("java:comp/UserTransaction");
            return returnUserTransaction;
        } catch (NamingException ne) {
            LOG.debug(ne.getExplanation());
            try {
                jakarta.transaction.UserTransaction ut = (jakarta.transaction.UserTransaction) context
                        .lookup("UserTransaction");
                ut.getStatus();
                return ut;
            } catch (NamingException ne2) {
                LOG.debug(ne2.getExplanation());
                try {
                    // // Try the other JBoss location
                    return (UserTransaction) context.lookup("java:jboss/UserTransaction");
                } catch (NamingException ex) {
                    LOG.error("JBoss da olmadı", ex);
                }
            } catch (Exception e) {
                LOG.error(e.getLocalizedMessage());
                // throw ne;
            }
        }

        UserTransaction returnUserTransaction = super.resolveUserTransaction();
        return returnUserTransaction;
    }
    
    /*
    Alternatif bir yöntem olarak burada bulunsun. Camel thread'i içindeyken asıl registeryi bulamazsa jboss'a geri düşmek için
    @Override
    protected TransactionSynchronizationRegistry resolveTransactionRegistry(){
        return JndiUtils.lookup("java:jboss/TransactionSynchronizationRegistry", TransactionSynchronizationRegistry.class);
        //return super.resolveTransactionRegistry();
    }*/
}
