package com.ozguryazilim.telve.view;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.apache.deltaspike.core.api.config.view.metadata.ViewMetaData;

/**
 * DeltaSpike ViewConfig'leri için title tanımlama annotationı.
 * 
 * Bu annotation ile tanımlanan title bilgisi PageTitleResolver ile alınabilir.
 * 
 * @author Hakan Uygun
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ViewMetaData
public @interface PageTitle {
    /**
     * View için verilecek olan title
     * @return 
     */
    String value();
}
