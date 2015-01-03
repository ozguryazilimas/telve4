/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.channel;

import java.util.Map;

/**
 * Channel implementasyonları için arayüz.
 * 
 * @author Hakan Uygun
 */
public interface Channel {
   
    /**
     * Basit mesajlar için
     * @param to
     * @param subject
     * @param message 
     */
    void sendMessage( String to, String subject, String message ); 
   
    /**
     * Biraz daha karmaşık mesajlar için
     * @param to
     * @param subject
     * @param message
     * @param params 
     */
    void sendMessage( String to, String subject, String message, Map<String, Object> params ); 
}
