package com.ozguryazilim.telve.messagebus.command;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Stereotype;
import javax.inject.Named;

/**
 * Televe MessageBus CommandExecutor işaretçisi.
 * 
 * @author Hakan Uygun
 */
@Stereotype
@Named
@ApplicationScoped
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface CommandExecutor {
    
    /**
     * Executor'un dinleyeceği komut sınıfı.
     * @return 
     */
    Class<? extends Command> command();
    
    
    /**
     * Komut akışını dağıtmak için kullanılacak ara route endpoint.
     * 
     * Eğer verilmezse seda:beanAdı kullanılır.
     * 
     * @return 
     */
    String dispacher() default "";
    
    /**
     * Eğer verilmezse bean:sınıfAdı olacak.
     * 
     * Kuyruk v.s. için seda:beanAdı v.b. kullanılabilir.
     * 
     * @return 
     */
    String endpoint() default "";
}
