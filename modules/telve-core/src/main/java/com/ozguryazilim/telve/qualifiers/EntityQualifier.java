package com.ozguryazilim.telve.qualifiers;

import com.ozguryazilim.telve.entities.EntityBase;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import jakarta.inject.Qualifier;

/**
 * Veri modelleri ile ilgili yerlerde kullanılabilecek Qualifier
 * @author Hakan Uygun
 */
@Qualifier
@Retention(RUNTIME)
@Target({TYPE, METHOD, FIELD, PARAMETER})
public @interface EntityQualifier {
    
    Class<? extends EntityBase> entity(); 
    
}
