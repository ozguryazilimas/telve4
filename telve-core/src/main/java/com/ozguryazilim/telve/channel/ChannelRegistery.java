/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.channel;

import java.util.ArrayList;
import java.util.List;

/**
 * Sistemde tanımlı iletişim kanallrının listesini tutar.
 * 
 * @author Hakan Uygun
 */
public class ChannelRegistery {
   
    private static List<String> channels = new ArrayList<>();
    
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
     */
    public static void register( String name ){
        channels.add(name);
    }
}
