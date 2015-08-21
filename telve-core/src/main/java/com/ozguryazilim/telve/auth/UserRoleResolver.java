/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

import java.util.List;

/**
 * Kullanıcıya ait UnifiedRole'e girebilecek uygulama taraflı verileri toparlamak için bileşen arayüzü.
 * 
 * Örneğin uygulama üzerinde tanımlı kullanıcı organizasyon bilgilerinin UnifiedRole'e girmesi istenirse şöyle bir kod yazılmalı :
 * 
 * @Named
 * @SessionScoped
 * class public UserOrganizationResolver imlements UserRoleResolver {
 *  ......
 * }
 * 
 * 
 * Eğer geriye döncek olan role ismi eşitlik değil benzerlik ile aranması gerekiyorsa Örneğin Organizasyon Path gibi Ön ek P ile başlamalı
 * 
 * Örnek :
 * 
 * O:1
 * PO:/1/2/3
 * PW:abc
 * PW:/a/b/c/abc
 * 
 * @author Hakan Uygun
 */
public interface UserRoleResolver {

    /**
     * Role isimleri listeye eklenmiş olarak dönmeli
     * @return 
     */
    List<String> getRoles();
    
    /**
     * UnifiedRole için prefix'leri verilmiş rol listesi. 
     * 
     * Örneğin :
     * 
     * OP:/1/2/3
     * OI:3
     * 
     * @return 
     */
    List<String> getUnifiedRoles();
    
}
