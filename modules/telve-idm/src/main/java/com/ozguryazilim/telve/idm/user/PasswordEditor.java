package com.ozguryazilim.telve.idm.user;


import com.google.common.base.Strings;
import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.config.AbstractOptionPane;
import com.ozguryazilim.telve.config.OptionPane;
import com.ozguryazilim.telve.idm.config.IdmPages;
import com.ozguryazilim.telve.idm.entities.User;
import com.ozguryazilim.telve.messages.FacesMessages;

import javax.inject.Inject;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.apache.shiro.authc.credential.DefaultPasswordService;


/**
 * Parola Değiştirme Ekranı.
 * 
 * Bir OptionPane olarak görünür.
 * 
 * @author Hakan Uygun
 */
@OptionPane(optionPage = IdmPages.PasswordOptionPane.class)
public class PasswordEditor extends AbstractOptionPane{

    private String newPassword;
    private String oldPassword;

    @Inject
    private UserRepository userRepository;
    
    @Inject
    private Identity identity;

    
    @Override @Transactional
    public void save() {
        
        User user = userRepository.findAnyByLoginName(identity.getLoginName());
        
        if( user == null ){
            FacesMessages.error("passwordEditor.message.PasswordError");
            return;
        }
        
        // Eski parola var mı kontrol edelim.
        if (Strings.isNullOrEmpty(oldPassword)) {
            FacesMessages.error("passwordEditor.message.OldPasswordError");
            return;
        }
        
        DefaultPasswordService passwordService = new DefaultPasswordService();
        
        // Eski parolayı doğrulayalım.
        if (!passwordService.passwordsMatch(oldPassword, user.getPasswordEncodedHash())) {
        	FacesMessages.error("passwordEditor.message.OldPasswordError");
            return;
        }
        
        //Yeni parola var mı bir kontrol edelim.
        if( Strings.isNullOrEmpty(newPassword)){
            FacesMessages.error("passwordEditor.message.PasswordError");
            return;
        }

        user.setPasswordEncodedHash(passwordService.encryptPassword(newPassword));
        
        
        userRepository.save(user);
        
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
