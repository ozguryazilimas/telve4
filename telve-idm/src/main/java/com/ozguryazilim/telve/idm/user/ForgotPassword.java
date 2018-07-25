package com.ozguryazilim.telve.idm.user;

import com.google.common.hash.Hashing;
import com.ozguryazilim.telve.channel.email.EmailChannel;
import com.ozguryazilim.telve.config.TelveConfigResolver;
import com.ozguryazilim.telve.idm.entities.User;
import com.ozguryazilim.telve.idm.passwordrenewal.PasswordRenewalHome;
import com.ozguryazilim.telve.messages.FacesMessages;
import com.ozguryazilim.telve.messages.Messages;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

@RequestScoped
@Named
public class ForgotPassword {

    @Inject
    private UserRepository repository;

    @Inject
    private EmailChannel emailChannel;

    @Inject
    private TelveConfigResolver resolver;

    @Inject
    PasswordRenewalHome passwordRenewalHome;

    private String email = "";
    private String token;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String sendEmailWithToken() {

        try {
            User user = repository.findByEmail(email);

            if (user.getAutoCreated().equals(false)) {
                generateToken();
                Map<String, Object> params = new HashMap<>();
                params.put("messageClass", "PASSWORDRENEWALTOKEN");
                params.put("token", token);
                params.put("entity", user);
                params.put("telveConfigResolver", resolver);

                emailChannel.sendMessage(email, "", "", params);

                FacesMessages.info(Messages.getMessageFromData(Messages.getCurrentLocale(),
                        "forgotPassword.info.SentRenewalEmail" + "$%&" + email));

                passwordRenewalHome.saveToken(user, token);
            } else {
                FacesMessages.error("forgotPassword.error.mailChangePermission");
            }

            return "/login.xhtml?faces-redirect=true";
        } catch (NoResultException nre) {
            FacesMessages.error(Messages.getMessage("forgotPassword.error.InvalidEmail"));
            return null;
        } catch (NonUniqueResultException e) {
            FacesMessages.error("forgotPassword.error.multipleEmail");
            return null;
        }
    }

    private void generateToken() {
        token = Hashing
                .sha256()
                .hashString(UUID.randomUUID().toString() + System.currentTimeMillis(),
                        StandardCharsets.UTF_8)
                .toString();
    }
}
