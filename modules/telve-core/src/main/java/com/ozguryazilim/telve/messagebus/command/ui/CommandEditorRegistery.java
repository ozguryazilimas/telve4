package com.ozguryazilim.telve.messagebus.command.ui;

import com.google.common.base.CaseFormat;
import com.ozguryazilim.telve.messagebus.command.StorableCommand;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.deltaspike.core.api.config.ConfigResolver;
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
    
    public static void register( String name, CommandEditor a ){
        name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, name);
        editors.put(name, a);
        typeEditors.put(a.command().getName(), name);
        LOG.info("Command Editor Registered : {}", name);
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
        //Config'den exclude edilmemişleri döndürür.
        return editors.keySet().stream()
                .filter( p-> !"true".equals(ConfigResolver.getPropertyValue( "permission.exclude." + p)))
                .collect(Collectors.toList());
        
    }
    
}
