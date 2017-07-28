/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command.ui;

import com.google.common.base.CaseFormat;
import com.ozguryazilim.telve.messagebus.command.StorableCommand;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sisteme tanımlı komut editorlerini saklar.
 * 
 * @author Hakan Uygun
 */
public class CommandEditorRegistery {
    
    private static final Logger LOG = LoggerFactory.getLogger(CommandEditorRegistery.class);
 
    private static final Map<String, CommandEditor> editors = new HashMap<>();
    /** 
     * Komut tipi ile editor adı eşlenip saklanıyor.
     */
    private static final Map<String, String> typeEditors = new HashMap<>();
    
    private static final HashMap<String, ArrayList<String>> categoryEditors = new HashMap<String, ArrayList<String>>();
    
        
    public static void register( String name, CommandEditor a ,String category){
    	name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, name);
        editors.put(name, a);
        typeEditors.put(a.command().getName(), name);
        addToList(category, name);
        LOG.info("Command Editor Registered : {}", name);
    }
    
    
    public static void addToList(String mapKey, String myItem) {
    	ArrayList<String> itemsList = categoryEditors.get(mapKey);

        // if list does not exist create it
        if(itemsList == null) {
             itemsList = new ArrayList<String>();
             itemsList.add(myItem);
             categoryEditors.put(mapKey, itemsList);
        } else {
            // add if item is not already in list
            if(!itemsList.contains(myItem)) itemsList.add(myItem);
        }
    }
    
    /**
     * Verilen komut için kayıtlı editör CDI instance'nını döndürür.
     * @param <C>
     * @param command
     * @return 
     */
    public static <C extends StorableCommand> CommandEditorBase<C> getEditor( C command ){
        return getEditorByCommand(command.getClass().getName());
    }
    
    /**
     * Verilen editor ismi ile kayıtlı editör CDI instance'ını döndürür.
     * @param name
     * @return 
     */
    public static CommandEditorBase getEditorByName( String name ){
        return (CommandEditorBase) BeanProvider.getContextualReference(name);
    }
    
    /**
     * Verilen komut ismi ile kayıtlı editör CDI instance'ını döndürür.
     * @param name
     * @return 
     */
    public static CommandEditorBase getEditorByCommand( String name ){
        String s = typeEditors.get(name);
        if( s != null ){
            return getEditorByName(s);
        }
        return null;
    }
    
    /**
     * Geriye tanımlı editorlerin EL isim listesini döndürür.
     * @return 
     */
    public static List<String> getEditorNames(){
        return new ArrayList( editors.keySet() );
    }
    /**
     * Geriye tanımlı editorlerin kategori isim listesini döndürür.
     * @return 
     */
    public static List<String> getEditorCategories(){
        return new ArrayList( categoryEditors.keySet() );
    }
    
    public static List<String> getEditorNamesByCategory(String category){
    	return categoryEditors.get(category);
    }
}
