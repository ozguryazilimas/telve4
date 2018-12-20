package com.ozguryazilim.telve.bpm.handlers;

import java.util.HashMap;
import java.util.Map;

/**
 * TaskResultCommand'ların listesini tutar.
 * 
 * @author Hakan Uygun
 */
public class TaskResultCommandRegistery {
   
    private static final Map<String,TaskResultCommand> TASK_RESULT_COMMANDS = new HashMap<>();
    
    /**
     * Yeni bir task command register eder.
     * @param result
     * @param icon
     * @param style 
     */
    public static void registerCommand( String result, String icon, String style ){
        TASK_RESULT_COMMANDS.put( result, new TaskResultCommand(result, icon, style));
    }
    
    /**
     * Yeni bir task command regiter eder.
     * @param command 
     */
    public static void registerCommand( TaskResultCommand command ){
        TASK_RESULT_COMMANDS.put( command.getResult(), command);
    }
    
    /**
     * Verilen result için command döndürür.
     * @param result
     * @return 
     */
    public static TaskResultCommand getCommand( String result ){
        return TASK_RESULT_COMMANDS.get(result);
    }

}
