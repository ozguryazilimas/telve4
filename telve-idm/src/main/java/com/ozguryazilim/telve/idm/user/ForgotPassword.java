package com.ozguryazilim.telve.idm.user;

import com.google.common.hash.Hashing;
import com.ozguryazilim.telve.channel.email.EmailChannel;
import com.ozguryazilim.telve.config.TelveConfigResolver;
import com.ozguryazilim.telve.idm.entities.User;
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

@RequestScoped
@Named
public class ForgotPassword {

    @Inject
    private UserRepository repository;

    @Inject
    private EmailChannel emailChannel;

    @Inject
    private TelveConfigResolver resolver;

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String sendEmailWithToken() {
        try {
            User user = repository.findByEmail(email);

            Map<String, Object> params = new HashMap<>();
            params.put("messageClass", "PASSWORDRENEWALTOKEN");
            params.put("token", generateToken());
            params.put("entity", user);
            params.put("telveConfigResolver", resolver);

            emailChannel.sendMessage(email, "", "", params);

            FacesMessages.info(Messages.getMessageFromData(Messages.getCurrentLocale(),
                    "forgotPassword.info.SentRenewalEmail" + "$%&" + email));
            return "/login.xhtml?faces-redirect=true";
        } catch (NoResultException nre) {
            FacesMessages.error(Messages.getMessage("forgotPassword.error.InvalidEmail"));
            return null;
        }
    }

    private String generateToken() {
        return Hashing
                .sha256()
                .hashString(UUID.randomUUID().toString() + System.currentTimeMillis(),
                        StandardCharsets.UTF_8)
                .toString();
    }
}
