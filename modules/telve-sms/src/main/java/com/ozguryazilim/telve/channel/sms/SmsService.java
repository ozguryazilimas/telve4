package com.ozguryazilim.telve.channel.sms;

/**
 * SMS gönderme işlemini yapacak asıl servislerin bu Interface'i implemente etmeleri gerekiyor.
 * 
 * @author Hakan Uygun
 */
public interface SmsService {
   
    /**
     * Verilen telefon numarasına verilen mesajı SMS olarak gönderir.
     * @param to
     * @param body 
     */
    void send(String to, String body);
}
