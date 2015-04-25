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
import javax.inject.Inject;
import org.picketlink.Identity;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.credential.Credentials;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.BasicModel;
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
    private DefaultLoginCredentials credentials;
    
    @Inject
    private Identity identity;
    
    @Override
    public void save() {
        
        //Once eski parola doğru mu bir kontrol edelim.
        credentials.setPassword(oldPassword);
        identityManager.validateCredentials(credentials);
        if( credentials.getStatus() != Credentials.Status.VALID ){
            return;
        }
        //Yeni parola var mı bir kontrol edelim.
        if( Strings.isNullOrEmpty(newPassword)) return;
        
        //Parolayı değiştirelim...
        User user = BasicModel.getUser(identityManager, credentials.getUserId());
        identityManager.updateCredential( user, new Password(newPassword));
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
