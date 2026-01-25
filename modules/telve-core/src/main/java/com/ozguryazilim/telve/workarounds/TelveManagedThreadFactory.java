package com.ozguryazilim.telve.workarounds;

import jakarta.enterprise.concurrent.ManagedThreadFactory;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

import org.apache.camel.util.concurrent.ThreadHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed Thread isimlerini düzenler.
 * @author Hakan Uygun
 */
public class TelveManagedThreadFactory implements ManagedThreadFactory {

    private static final Logger LOG = LoggerFactory.getLogger(TelveManagedThreadFactory.class);

    private final ManagedThreadFactory delegate;
    private String name;
    private String pattern;

    public TelveManagedThreadFactory(ManagedThreadFactory managedThreadFactory, String pattern, String name) {
        this.delegate = managedThreadFactory;
        this.pattern = pattern;
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        String threadName = ThreadHelper.resolveThreadName(pattern, name);
        Thread answer = delegate.newThread(r);
        try {
            answer.setName(threadName);
        } catch (Exception e) {
            LOG.warn("Error seting thread name {}", e.toString());
        }

        LOG.trace("Created thread[{}] -> {}", threadName, answer);
        return answer;
    }

    @Override
    public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
        //FIXME: jakarta: bu method yeni geldi nasıl impl etmek gerekiyor bakmak lazım
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'newThread'");
    }


}
