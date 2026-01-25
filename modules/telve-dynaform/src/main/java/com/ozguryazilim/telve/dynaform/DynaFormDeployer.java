package com.ozguryazilim.telve.dynaform;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Stereotype;
import jakarta.inject.Named;

/**
 * DynaFom Deploy edecek olan sınıfları işeretler.
 * @author Hakan Uygun
 */
@Stereotype
@Dependent
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Named
@Documented
public @interface DynaFormDeployer {
    
}
