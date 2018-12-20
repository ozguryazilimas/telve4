package com.ozguryazilim.telve.workarounds;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javax.enterprise.concurrent.ManagedThreadFactory;
import org.apache.camel.impl.DefaultThreadPoolFactory;
import org.apache.camel.util.concurrent.CamelThreadFactory;
import org.apache.camel.util.concurrent.ThreadHelper;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Camel'ın Java EE 7 Concurent API üzerinden thread pooling yapmasını sağlar.
 *
 * Transaction yönetimi artık threadler JEE 7 Concurent API üzerinden
 * alınmadığında çalışmıyor.
 *
 * @author Hakan Uygun
 */
public class TelveCamelThreadPoolFactory extends DefaultThreadPoolFactory{

    private static final Logger LOG = LoggerFactory.getLogger(TelveCamelThreadPoolFactory.class);

    private final ManagedThreadFactory managedThreadFactory;

    public TelveCamelThreadPoolFactory(ManagedThreadFactory threadFactory) {
        this.managedThreadFactory = threadFactory;
    }

    @Override
    public ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
        //every threadFactory is replaced by subsituteThreadFactory(threadFactory)
        return Executors.newCachedThreadPool(subsituteThreadFactory(threadFactory));
    }

    /**
     * Gets the naming off the passed in factory and creates a wrapper around
     * the managed one to rename the thread. If permission is denied logging MDC
     * could be using instead.
     *
     * @param passedInFactory
     * @return
     */
    protected ThreadFactory subsituteThreadFactory(ThreadFactory passedInFactory) {
        try {
            String pattern = ThreadHelper.DEFAULT_PATTERN;
            String name = "";
            if (passedInFactory instanceof CamelThreadFactory) {
                pattern = (String) FieldUtils.readField(passedInFactory, "pattern", true);
                name = (String) FieldUtils.readField(passedInFactory, "name", true);
                // boolean daemon;
            } 
            /*else if (passedInFactory instanceof CustomizableThreadFactory) {
                pattern = "#name#-#counter#";
                name = (String) FieldUtils.readField(passedInFactory, "threadNamePrefix", true);
            }*/
            LOG.debug("Creating naming factory pattern {} name {}", pattern, name);
            return new TelveManagedThreadFactory(managedThreadFactory, pattern, name);
        } catch (IllegalAccessException e) {
            LOG.warn("Could not determine fatory name {}", e.toString());
            return managedThreadFactory;
        }
    }
}
