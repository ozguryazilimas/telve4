/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command;

import java.util.Date;

/**
 * Kullanıcıdan / Servisler'den alınan verilerle saklanabilecek komut türlerini tanımlar.
 * 
 * Özellikle Günsonu vb. tanımlanacak işler için kullanılır. Bu tür komutlar için CommandEditor'ler hazırlanabilir.
 * 
 * @author Hakan Uygun
 */
public interface StorableCommand extends Command{
    
    /**
     * Kullanıcı tarafından komut adı değiştirilebilir.
     * 
     * Bu sayede aynı komutlar sistemde farklı komut olarak değerlendirilip dolaşabilir.
     * 
     * @param name 
     */
    void setName( String name );
    
    /**
     * Komut için varsa ek açıklama döndürür.
     * 
     * @return 
     */
    String getInfo();
    
    /**
     * Komut için ek açıklama setler
     * @param info 
     */
    void setInfo( String info );
    
    /**
     * Komutun oluşturulma tarihini döndürür.
     * @return 
     */
    Date getCreateDate();
    
    /**
     * Komutun oluşturulma tarihini setler
     * @param date 
     */
    void setCreateDate( Date date );
    
    /**
     * Komutun kim tarafından oluşturulduğunu döndürür.
     * @return 
     */
    String getCreateBy();
    
    /**
     * Komutun kim tarafından oluşturulduğunu setler.
     * @param createBy 
     */
    void setCreateBy( String createBy );
}
