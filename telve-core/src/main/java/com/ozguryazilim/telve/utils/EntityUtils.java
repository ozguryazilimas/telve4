/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.utils;

import com.ozguryazilim.telve.annotations.BizKey;
import com.ozguryazilim.telve.entities.EntityBase;
import java.lang.reflect.Field;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entity'ler ile ilgili utility fonksiyonlar.
 * 
 * @author Hakan Uygun
 */
public class EntityUtils {
   
    private static final Logger LOG = LoggerFactory.getLogger(EntityUtils.class);
    
    /**
     * Entity üzerinde @BizKey annotation'ını bulunana field değerini döner.
     * 
     * İstenilir ise form için override edilebilir.
     * 
     * @return 
     */
    public static String getBizKeyValue( EntityBase entity){
        
        String result = "";

        Field[] fields = FieldUtils.getFieldsWithAnnotation(entity.getClass(), BizKey.class);
        
        for( Field f : fields ){
            if( f.isAnnotationPresent(BizKey.class) ){
                try {
                    f.setAccessible(true);
                    result += f.get(entity).toString();
                } catch (IllegalArgumentException ex) {
                    LOG.debug("BizKey not found", ex);
                } catch (IllegalAccessException ex) {
                    LOG.debug("BizKey not found", ex);
                }
            }
        }
        
        return result;
    }
}
