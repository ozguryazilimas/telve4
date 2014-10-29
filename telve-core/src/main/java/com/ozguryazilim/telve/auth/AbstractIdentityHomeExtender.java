/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

import java.io.Serializable;
import org.picketlink.idm.model.IdentityType;

/**
 * IdentityHome türü sınıfları extend etmek için taban sınıf.
 * 
 * Bu sınıf miras alınıp @UserModel olarak işaretlenmeli. 
 * İleride farklı işaretçiler gelebilir.
 * 
 * @author Hakan Uygun
 */
public abstract class AbstractIdentityHomeExtender implements Serializable{
    
    /**
     * Yeni Identity oluşturulmadan önce çağırılır.
     * işleme devam edilmesi istenmiyorsa false döner.
     * @return 
     */
    public boolean onBeforeNew( IdentityType indentity ){
        return true;
    }
    /**
     * Yeni Identity oluşturulduktan sonra çağırılır.
     * işleme devam edilmesi istenmiyorsa false döner.
     * @return 
     */
    public boolean onAfterNew( IdentityType indentity ){
        return true;
    }
    
    /**
     * Identity saklanmadan önce çağırılır.
     * işleme devam edilmesi istenmiyorsa false döner.
     * @return 
     */
    public boolean onBeforeSave( IdentityType indentity){
        return true; 
    }
    /**
     * Identity saklanmadıktan sonra çağırılır.
     * işleme devam edilmesi istenmiyorsa false döner.
     * @return 
     */
    public boolean onAfterSave(IdentityType indentity){
        return true;
    }
    
    /**
     * Identity silinmeden önce çağırılır.
     * işleme devam edilmesi istenmiyorsa false döner.
     * @return 
     */
    public boolean onBeforeDelete(IdentityType indentity){
        return true;
    }
    /**
     * Identity silindikten sonra çağırılır.
     * işleme devam edilmesi istenmiyorsa false döner.
     * @return 
     */
    public boolean onAfterDelete(IdentityType indentity){
        return true;
    }
    
    /**
     * Identity yüklenmeden önce çağırılır.
     * işleme devam edilmesi istenmiyorsa false döner.
     * @return 
     */
    public boolean onBeforeLoad(IdentityType indentity){
        return true;
    }
    /**
     * Identity yüklendikten sonra çağırılır.
     * işleme devam edilmesi istenmiyorsa false döner.
     * @return 
     */
    public boolean onAfterLoad(IdentityType indentity){
        return true;
    }
}
