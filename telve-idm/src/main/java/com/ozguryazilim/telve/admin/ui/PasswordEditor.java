/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.admin.ui;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.admin.AdminPages;
import com.ozguryazilim.telve.config.AbstractOptionPane;
import com.ozguryazilim.telve.config.OptionPane;
import com.ozguryazilim.telve.messages.FacesMessages;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.credential.Credentials;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.credential.UsernamePasswordCredentials;
import org.picketlink.idm.model.basic.User;

/**
 * Parola Değiştirme Ekranı.
 * 
 * Bir OptionPane olarak görünür.
 * 
 * @author Hakan Uygun
 */
@OptionPane(optionPage = AdminPages.PasswordOptionPane.class)
public class PasswordEditor extends AbstractOptionPane{

    private String newPassword;
    private String oldPassword;
    
    @Inject
    private IdentityManager identityManager;
    
    @Inject
    private Identity identity;
    
    @Override @Transactional
    public void save() {
        
        User user = (User) identity.getAccount();
    
        UsernamePasswordCredentials credential = new UsernamePasswordCredentials();
        Password oldPwd = new Password( oldPassword );
        
        //Once eski parola doğru mu bir kontrol edelim.
        credential.setUsername(user.getLoginName());
        credential.setPassword(oldPwd);
        identityManager.validateCredentials(credential);
        if( credential.getStatus() != Credentials.Status.VALID ){
            FacesMessages.error("passwordEditor.message.PasswordError");
            return;
        }
        //Yeni parola var mı bir kontrol edelim.
        if( Strings.isNullOrEmpty(newPassword)){
            FacesMessages.error("passwordEditor.message.PasswordError");
            return;
        }
        
        //Parolayı değiştirelim...
        identityManager.updateCredential( user, new Password(newPassword));
        
        FacesMessages.info("passwordEditor.message.Success");
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
    
    
}
