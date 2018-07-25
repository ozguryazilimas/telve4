package com.ozguryazilim.telve.idm.passwordrenewal;

import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.forms.FormBase;
import com.ozguryazilim.telve.idm.entities.PasswordRenewalToken;
import com.ozguryazilim.telve.idm.entities.User;
import com.ozguryazilim.telve.idm.user.UserRepository;
import com.ozguryazilim.telve.messages.FacesMessages;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.joda.time.DateTime;

/**
 *
 * @author yusuf
 */
@RequestScoped
@Named
public class PasswordRenewalHome extends FormBase<PasswordRenewalToken, Long> {

    private String newPassword;
    private String checkPassword;
    private DateTime tokenBirth;
    private DateTime tokenDate;

    private String token;
    private User user;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordRenewalTokenRepository passwordRenewalTokenRepository;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();
        if (paramMap.get("token") != null) {
            setToken(paramMap.get("token"));
        }

    }

    @Transactional
    public void saveToken(User user, String token) {
        this.token = token;
        this.user = user;

        PasswordRenewalToken getUserToken = passwordRenewalTokenRepository.findAnyByUser(user);

        if (getUserToken != null) {
            getUserToken.setToken(token);
            getUserToken.setTokenDate(new Date());
            passwordRenewalTokenRepository.save(getUserToken);
        } else {
            PasswordRenewalToken prt = new PasswordRenewalToken();
            prt.setToken(token);
            prt.setTokenDate(new Date());
            prt.setUser(user);
            passwordRenewalTokenRepository.save(prt);
        }

    }

    @Transactional
    public String changePassword() {
        if (token != null) {
            PasswordRenewalToken prt = passwordRenewalTokenRepository.findAnyByToken(token);
            if (prt == null) {
                FacesMessages.error("passwordRenewalHome.error.accessTime");
                return "/login.xhtml?faces-redirect=true";
            } else {
                long diff = TimeUnit.MILLISECONDS.toMinutes(new Date().getTime() - prt.getTokenDate().getTime());
                if (diff > 15) {
                    FacesMessages.error("passwordRenewalHome.error.accessTime");
                    passwordRenewalTokenRepository.deleteById(prt.getId());
                } else {
                    user = prt.getUser();
                    DefaultPasswordService passwordService = new DefaultPasswordService();
                    user.setPasswordEncodedHash(passwordService.encryptPassword(newPassword));
                    userRepository.save(user);
                    passwordRenewalTokenRepository.deleteById(prt.getId());
                    FacesMessages.info("passwordRenewalHome.success.changePassword");
                    return "/login.xhtml?faces-redirect=true";
                }

            }
            return "/login.xhtml?faces-redirect=true";
        } else {
            return "/login.xhtml?faces-redirect=true";
        }

    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getCheckPassword() {
        return checkPassword;
    }

    public void setCheckPassword(String checkPassword) {
        this.checkPassword = checkPassword;
    }

    public DateTime getTokenBirth() {
        return tokenBirth;
    }

    public void setTokenBirth(DateTime tokenBirth) {
        this.tokenBirth = tokenBirth;
    }

    public DateTime getTokenDate() {
        return tokenDate;
    }

    public void setTokenDate(DateTime tokenDate) {
        this.tokenDate = tokenDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    protected RepositoryBase<PasswordRenewalToken, ?> getRepository() {
        return passwordRenewalTokenRepository;
    }

}
