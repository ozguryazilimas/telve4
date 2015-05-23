/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.dynaform;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.ozguryazilim.telve.dynaform.model.DynaCalcFieldValue;
import com.ozguryazilim.telve.dynaform.model.DynaContainer;
import com.ozguryazilim.telve.dynaform.model.DynaField;
import com.ozguryazilim.telve.dynaform.model.DynaForm;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * DynaForm sistemi için yardımcı fonksiyonlar.
 * @author Hakan Uygun
 */
public class DynaFormUtils {
    
    public static String fieldValuestoJson( Map<String, Object> valueMap){
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String,Object>>(){}.getType();
        return gson.toJson(valueMap, type);
    }
    
    public static Map<String, Object>  fieldValuesfromJson( String json, DynaForm form ){
        return fieldValuesfromJson( json, resolveFieldMap(form));
    }
    
    public static Map<String, Object>  fieldValuesfromJson( String json, Map<String,DynaField> fieldMap ){
        
        
        Map<String, Object> result = new HashMap<>();
        
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonObject jo = parser.parse(json).getAsJsonObject();
        
        for( Map.Entry<String,JsonElement> e : jo.entrySet()){
            System.out.println(e);
            
            DynaField f = fieldMap.get(e.getKey());
            if( f != null ){
                result.put(e.getKey(), gson.fromJson(e.getValue(), f.getValueClass()));
            }
            
        }
        
        return result;
    }
    
    
    public static Map<String,DynaField> resolveFieldMap( DynaForm form ){
        
        Map<String,DynaField> result = new HashMap<>();
        
        for( DynaContainer c : form.getContainers()){
            for( DynaField f : c.getFields()){
                result.put(f.getId(), f );
            }
        }
        
        return result;
    }
    
    
    public static String calcValuesToJson( Map<String, DynaCalcFieldValue> valueMap){
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String,DynaCalcFieldValue>>(){}.getType();
        return gson.toJson(valueMap, type);
    }
    
    public static Map<String, DynaCalcFieldValue> calcValuesFromJson(  String json ){
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String,DynaCalcFieldValue>>(){}.getType();
        return gson.fromJson(json, type);
    }
}
