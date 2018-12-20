package com.ozguryazilim.telve.dynaform;

import com.ozguryazilim.telve.dynaform.model.DynaCalcFieldValue;
import com.ozguryazilim.telve.dynaform.model.DynaForm;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 *
 * @author haky
 */
public class DynaFormControllerTest {
    
    /**
     * Test of init method, of class DynaFormController.
     */
    @Test
    public void testInit() {
        System.out.println("init");
        TestFormBuilder b = new TestFormBuilder();
        DynaForm form = b.buildTestForm();
        Map<String, Object> valueMap = new HashMap<>();
        Map<String, DynaCalcFieldValue> calcValueMap = new HashMap<>();
        DynaFormController instance = new DynaFormController();
        instance.init(form, valueMap, calcValueMap);
        
        instance.getBinder("fld1").setValue("TEST DEĞERİ");
        
        System.out.println(valueMap);
        
        System.out.println(instance.getBinder("fld2").getValue());
        
        instance.getBinder("fld3").setValue("def");
        System.out.println(valueMap);
        
        System.out.println(instance.getBinder("fld4").getValue());
        instance.getBinder("fld4").setValue( 4l);
        System.out.println(valueMap);
        instance.getBinder("fld4").setValue( "5l");
        System.out.println(valueMap);
        
        //Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //System.out.println(gson.toJson(form));
        
        
        instance.getBinder("test:1:1").setValue( "DEGER1");
        System.out.println(instance.calculateValue( "default" ));
        System.out.println(instance.calculateValue( "test" ));
        
        instance.calculateFieldValues();
        System.out.println(calcValueMap);
        
        System.out.println(DynaFormUtils.calcValuesToJson(calcValueMap));
        
    }

    
    
}
