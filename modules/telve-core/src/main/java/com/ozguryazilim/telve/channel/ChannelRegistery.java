package com.ozguryazilim.telve.channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sistemde tanımlı iletişim kanallrının listesini tutar.
 * 
 * @author Hakan Uygun
 */
public class ChannelRegistery {
   
    private static List<String> channels = new ArrayList<>();
    private static Map<String,String> aliases = new HashMap<>();
    
    /**
     * Geriye sistemde tanımlı channel providerların CDI bean isimlerini döndürür.
     * 
     * Gelen stringler şu şekilde kullanılabilir : 
     * 
     * Channel channel = (Channel) BeanProvider.getContextualReference(channelName);
     * 
     * @return 
     */
    public static List<String> getChannels(){
        return channels;
    }
    
    
    /**
     * Verilen isimli kanalı sisteme tanıtır.
     * 
     * İsimler  @see Channel interface'ini implemente eden CDI bean isimleri olmalıdır. 
     * 
     * İsimlerin nesne karşılığı kontrolü yapıl mamaktadır. Doğru olma sorumluluğu yazılımcıdadır.
     * 
     * @param name 
     * @param alias 
     */
    public static void register( String name, String alias ){
        channels.add(name);
        aliases.put(alias, name);
    }
    
    /**
     * Alias ismi ile channel bulur.
     * 
     * Configürasyonlarda kullanıcıya daha yakın bir isim olsun diye email,web gibi kısaltmalar kullanılacak...
     * @param alias
     * @return 
     */
    public static String getChannelByAlias( String alias ){
        return aliases.get(alias);
    }
}
