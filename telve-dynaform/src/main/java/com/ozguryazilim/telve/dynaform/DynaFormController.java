/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.dynaform;

import com.ozguryazilim.telve.dynaform.binder.DynaCalcFieldBinder;
import com.ozguryazilim.telve.dynaform.binder.DynaFieldBinder;
import com.ozguryazilim.telve.dynaform.calc.CalcResultRule;
import com.ozguryazilim.telve.dynaform.calc.CalcRule;
import com.ozguryazilim.telve.dynaform.model.DynaCalcField;
import com.ozguryazilim.telve.dynaform.model.DynaCalcFieldValue;
import com.ozguryazilim.telve.dynaform.model.DynaCalcType;
import com.ozguryazilim.telve.dynaform.model.DynaContainer;
import com.ozguryazilim.telve.dynaform.model.DynaField;
import com.ozguryazilim.telve.dynaform.model.DynaForm;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.enterprise.context.Dependent;

/**
 * DynaForm modeli ile UI verilerini eşleyen teme UI control sınıfı.
 * 
 * @author Hakan Uygun
 */
@Dependent
public class DynaFormController implements Serializable{
    
    private DynaForm form;
    private Map<String,Object> valueMap = new HashMap<>();
    private Map<String,DynaFieldBinder> binderMap = new HashMap<>();
    private Map<String,DynaCalcFieldBinder> calcBinderMap = new HashMap<>();
    private Map<String,DynaCalcFieldValue> calcValueMap = new HashMap<>();
            
    
    /**
     * Controller verilerini init eder.
     * 
     * COntroller kullanılmadan önce mutlaka çağrılmalıdır.
     * 
     * @param form
     * @param valueMap 
     * @param calcValueMap 
     */
    public void init( DynaForm form, Map<String,Object> valueMap, Map<String,DynaCalcFieldValue> calcValueMap){
        this.form = form;
        
        if( valueMap != null ){
            this.valueMap = valueMap;
        }
        
        if( calcValueMap != null ){
            this.calcValueMap = calcValueMap;
        }
        
        
        for( DynaContainer c : form.getContainers()){
            for( DynaField f : c.getFields()){
                binderMap.put(f.getId(), new DynaFieldBinder<>( f, valueMap));
            }
        }
        
        for( DynaCalcField cf : form.getCalcFields()){
            calcBinderMap.put(cf.getId(), new DynaCalcFieldBinder( cf, calcValueMap));
        }
    }
    
    public Object getValue( String id ){
        return valueMap.get(id);
    }
    
    public void setValue( String id, Object value ){
        valueMap.put(id, value);
    }

    public DynaForm getForm() {
        return form;
    }

    public void setForm(DynaForm form) {
        this.form = form;
    }

    public Map<String, Object> getValueMap() {
        return valueMap;
    }

    public void setValueMap(Map<String, Object> valueMap) {
        this.valueMap = valueMap;
    }

    public Map<String, DynaFieldBinder> getBinderMap() {
        return binderMap;
    }

    public void setBinderMap(Map<String, DynaFieldBinder> binderMap) {
        this.binderMap = binderMap;
    }

    public Map<String, DynaCalcFieldBinder> getCalcBinderMap() {
        return calcBinderMap;
    }

    public void setCalcBinderMap(Map<String, DynaCalcFieldBinder> calcBinderMap) {
        this.calcBinderMap = calcBinderMap;
    }
    
    public DynaFieldBinder getBinder( String id ){
        return binderMap.get(id);
    }
    
    public DynaCalcFieldBinder getCalcBinder( String id ){
        return calcBinderMap.get(id);
    }
    
    public Double calculateValue( String group){
        
        Double res = 0d;
        
        for( DynaFieldBinder b : binderMap.values()){
            if( !b.getField().getRules().isEmpty() ){
                for (Iterator it = b.getField().getRules().iterator(); it.hasNext();) {
                    CalcRule c = (CalcRule) it.next();
                    res = res + c.calculate(b, group);
                }
            }
        }
        
        
        return res;
    }
    
    
    public void calculateFieldValues(){
        
        //Önce tüm değerleri bir hesaplayalım
        for( DynaCalcField cf : form.getCalcFields()){
            calculateValue( cf );
        }
        
        //Şimdi de sonuç hesaplayalım
        for( DynaCalcField cf : form.getCalcFields()){
            calculateResult( cf );
        }
        
    }
    
    
    /**
     * Verilen DynaCalcField için FieldValueların üzerinden geçerek hesaplama yapar.
     * 
     * Sonuçlar calcFieldValueMap içerisne konur.
     * 
     * @param cf 
     */
    protected void calculateValue( DynaCalcField cf ){
        
        Long result = 0l;
        Long count = 0l;
        
        for( DynaFieldBinder b : binderMap.values()){
            if( !b.getField().getRules().isEmpty() ){
                for (Iterator it = b.getField().getRules().iterator(); it.hasNext();) {
                    CalcRule c = (CalcRule) it.next();
                    Long fcv = c.calculate(b, cf.getGroup());
                    
                    switch ( cf.getMethod() ){
                        case AVG : count++;
                        case SUM : result = result + fcv; break;
                        case MAX : result = result > fcv ? result : fcv; break;
                        case MIN : result = result < fcv ? result : fcv; break;
                    }
                    
                }
            }
        }
        
        //Eğer AVG hesaplanıyorsa bir bölme yapalım
        if( cf.getMethod() == DynaCalcType.AVG ){
            result = result / count;
        }
        
        DynaCalcFieldValue dcfv = new DynaCalcFieldValue();
        
        dcfv.setId(cf.getId());
        dcfv.setValue(result);
        
        calcValueMap.put(dcfv.getId(), dcfv);
        
    }
    
    /**
     * Verilen DynaCalcField için sonuç hesaplar.
     * @param cf 
     */
    protected void calculateResult( DynaCalcField cf ){
        
        for( CalcResultRule rr : cf.getResultRules()){
            rr.calcResult(calcValueMap);
        }
        
    }
}
