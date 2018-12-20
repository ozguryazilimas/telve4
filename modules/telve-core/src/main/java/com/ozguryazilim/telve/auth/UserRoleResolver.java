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
     * İsmi verilen kullanıcı için Role isimleri listeye eklenmiş olarak dönmeli
     * 
     * Roller standart yetki rolleridir. 
     * 
     * @param loginName
     * @return 
     */
    List<String> getRoles( String loginName );
    
    /**
     * İsmi verilen kullanıcı için UnifiedRole için prefix'leri verilmiş rol listesi. 
     * 
     * Unified roller ise normal yetki rollerinin dışında farklı amaçlar için kullanıcı gruplamayı sağlayacak tanımlar olabilir.
     * 
     * Örneğin :
     * 
     * OP:/1/2/3
     * OI:3
     * 
     * @param loginName
     * @return 
     */
    List<String> getUnifiedRoles( String loginName );
    
}
