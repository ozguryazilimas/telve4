/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

import java.util.List;
import java.util.Map;

/**
 * Altta kullanılan farklı Realm implementasyonlarının UserService için gereken fonksiyoneleteyi sağlayacağı provider yapısı.
 * 
 * Telve'nin yapısı altta kullanılan modelleme yapısından bağımsız olarak kullanıcı arayüzü ve gerekli yerlerde ihtiyaç duyduğu temel fonsiyonları döndürür.
 * 
 * @author Hakan Uygun
 */
public interface UserServiceProvider {
    
    /**
     * Geriye sistemde tanımlı kullanıcı login isimlerini döndürür.
     * 
     * @return 
     */
    List<String> getLoginNames();
    
    /**
     * Kullanıcı tipine göre kullanıcı ( loginName ) listesi döndürür.
     * 
     * Bahsi geçen tipler UserModelExtention ile verilebilmektedir.
     * 
     * @param type
     * @return 
     */
    List<String> getUsersByType( String type );
    
    /**
     * Verilen Grup üyesi kullanıcı listesini döndürür.
     * 
     * Grup yapısı ağaç ise o ağacın alt dallarında olan kullanıcıları da döndürür.
     * 
     * @param group
     * @return 
     */
    List<String> getUsersByGroup( String group );
    
    /**
     * LoginName'i kullanıcı adına çevirir. 
     * 
     * 
     * @param loginName
     * @return 
     */
    String getUserName( String loginName );


    /**
     * LoginName'i verilen kullanıcının temel bilgilerini döndürür.
     * @param loginName
     * @return 
     */
    UserInfo getUserInfo( String loginName );
    
    /**
     * Login name verilen kullanıcının role listesini döndürür.
     * @param loginName
     * @return 
     */
    List<String> getUserRoles( String loginName);
    
    
    /**
     * Login name verilen kullanıcının grup isim listesini döndürür.
     * 
     * @param loginName
     * @return 
     */
    List<String> getUserGroups( String loginName);
    
    
    /**
     * İsmi verilen kullanıcının ek attribute listesini döndürür.
     * 
     * @param loginName
     * @return 
     */
    Map<String,String> getUserAttibutes( String loginName );
}
