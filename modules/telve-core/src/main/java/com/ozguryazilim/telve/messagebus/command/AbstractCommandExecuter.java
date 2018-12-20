package com.ozguryazilim.telve.messagebus.command;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import org.apache.deltaspike.cdise.api.ContextControl;
import org.apache.deltaspike.core.api.provider.BeanProvider;

/**
 * Telve MessageBus komutlarının bu interface'i implemente etmesi gerek.
 * 
 * @author Hakan Uygun
 * @param <C> Karşılayıp çalışacağı komut sınıfı
 */
public abstract class AbstractCommandExecuter<C extends Command>{
    
    @PostConstruct
    public void initRequestScope(){
        ContextControl ctxCtrl = BeanProvider.getContextualReference(ContextControl.class);
        // this will implicitly bind a new RequestContext to your current thread
        ctxCtrl.startContext(SessionScoped.class);
        ctxCtrl.startContext(RequestScoped.class);
    }
    
    @PreDestroy
    public void doneRequestScope(){
        ContextControl ctxCtrl = BeanProvider.getContextualReference(ContextControl.class);
        // this will implicitly bind a new RequestContext to your current thread
        ctxCtrl.stopContext(RequestScoped.class);
        ctxCtrl.stopContext(SessionScoped.class);
    }
    
    /**
     * Çalıştırılacak komut bilgisi parametre olarak gelir.
     * @param command 
     */
    public abstract void execute(C command);
    
}
