package com.ozguryazilim.telve.idm.user;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.auth.PasswordChangeEvent;
import com.ozguryazilim.telve.auth.UserDataChangeEvent;
import com.ozguryazilim.telve.idm.entities.User;
import com.ozguryazilim.telve.messages.FacesMessages;
import java.io.Serializable;
import javax.enterprise.event.Event;
import javax.enterprise.context.SessionScoped;

import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.apache.shiro.authc.credential.DefaultPasswordService;

/**
 * Kullanıcı ilk giriş yaptığında veya Yönetici tarafından parolası
 * değiştirildiğinde Parolasını değiştirmesi için gerekli sınıf
 *
 * @author serdar
 *
 */
@Named
@SessionScoped
public class ChangePassword implements Serializable {

    private String newPassword;

    @Inject
    private UserRepository userRepository;

    @Inject
    private Identity identity;

    @Inject
    private Event<UserDataChangeEvent> userEvent;

    @Inject
    private Event<PasswordChangeEvent> passwordChangeEvent;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Transactional
    public void save() {

        User user = userRepository.findAnyByLoginName(identity.getLoginName());
        if (user == null) {
            FacesMessages.error("passwordEditor.message.PasswordError");
            return;
        }

        if (Strings.isNullOrEmpty(newPassword)) {
            FacesMessages.error("passwordEditor.message.PasswordError");
            return;
        }

        DefaultPasswordService passwordService = new DefaultPasswordService();
        user.setPasswordEncodedHash(passwordService.encryptPassword(newPassword));
        user.setChangePassword(Boolean.FALSE);
        userRepository.save(user);
        userEvent.fire(new UserDataChangeEvent(identity.getLoginName()));
        passwordChangeEvent.fire(new PasswordChangeEvent());
    }
}
