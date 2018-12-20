package com.ozguryazilim.telve.dynaform;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Stereotype;
import javax.inject.Named;

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
