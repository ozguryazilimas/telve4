package com.ozguryazilim.telve.messagebus.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sisteme tanıtılmış komutların listesini tutar.
 *
 * @author Hakan Uygun
 */
public class CommandRegistery {

    private static final Logger LOG = LoggerFactory.getLogger(CommandRegistery.class);

    /**
     * Komut adı ve endpoint ikilisini tutar.
     */
    private static final Map< String, String> commands = new HashMap<>();
    private static final Map< String, String> dispacherMap = new HashMap<>();
    
    private static final List<String> vetoList = new ArrayList<>();

    /**
     * Sisteme yeni bir komut meta datası ekler.
     *
     * @param name komut sınıfının EL adı
     * @param dispacher komut akışı için ara dağıtıcı
     * @param endpoint executerların dileyeceği consumer enpoint
     */
    public static void register(String name, String dispacher, String endpoint) {
        commands.put(name, endpoint);
        dispacherMap.put(name, dispacher);
        LOG.info("Registered Command : {}=>{}->{}", new Object[]{name, dispacher, endpoint});
    }

    /**
     * Geriye register edilen komutların camel endpoint listesini döndürür.
     *
     * @return
     */
    public static List<String> getEndpoints() {
        return new ArrayList(commands.values());
    }
    
    /**
     * Geriye register edilen komutların dispach endpoint listesini döndürür.
     *
     * @return
     */
    public static List<String> getDispachers() {
        return new ArrayList(dispacherMap.values());
    }

    /**
     * Geriye tanımlanmış komut listesini döndürür.
     *
     * @return
     */
    public static List<String> getCommands() {
        return new ArrayList(commands.keySet());
    }

    /**
     * Verilen komutun sistemde tanımlı olup olmadığını kontrol eder.
     *
     * @param command
     * @return
     */
    public static boolean isRegistered(String command) {

        boolean result = commands.containsKey(command);

        if (!result && !vetoList.contains(command)) {
            //Kendisi için yokmuş ama belki miras aldığı sınıf register edilmiştir.
            try {
                Class c = ClassUtils.getClass(command);
                
                Class r = isRegistered( c.getSuperclass() );
                if( r != null ){
                    //Evet üst sınıfta bulduk. Aynı aramayı bir daha yapmayalım diye register edelim...
                    register(command, getDispacher(r.getName()), getEndpoint(r.getName()));
                    result = true;
                } else {
                    //Vetolular arasına koyuyoruz ki ikide bir aramayalım...
                    vetoList.add(command);
                }
            } catch (ClassNotFoundException ex) {
                //Bişey yapmamıza gerek yok, bulunamadıysa bulunamadı :)
            }
        }
        return result;
    }
    
    /**
     * Verilen sınıf ve üstü için arama yapar bulursa bulduğu sınıfı döndürür.
     * @param c
     * @return 
     */
    public static Class isRegistered( Class c ){
        
        boolean b = commands.containsKey(c.getName());
        Class result = null;
        if( !b ){
            Class p = c.getSuperclass();
            if( p != null ){
                result = isRegistered( p );
            }
        } else {
            result = c;
        }
        
        return result;
    }

    /**
     * Geriye verilen komutun endpoint bilgisini döndürür.
     *
     * @param name
     * @return
     */
    public static String getEndpoint(String name) {
        //Önce altsınıflardan sa ve ara register yapılacaksa onlar yapısın diye
        if( isRegistered(name) ){
            return commands.get(name);
        }
        
        return null;
    }
    
    public static String getDispacher( String name ){
        return dispacherMap.get(name);
    }
}
