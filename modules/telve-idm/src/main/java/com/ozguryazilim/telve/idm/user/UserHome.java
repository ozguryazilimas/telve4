package com.ozguryazilim.telve.idm.user;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.audit.AuditLogCommand;
import com.ozguryazilim.telve.audit.ChangeLogStore;
import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.auth.UserDataChangeEvent;
import com.ozguryazilim.telve.auth.UserModel;
import com.ozguryazilim.telve.auth.UserModelRegistery;
import com.ozguryazilim.telve.channel.email.EmailChannel;
import com.ozguryazilim.telve.config.TelveConfigResolver;
import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.forms.FormBase;
import com.ozguryazilim.telve.forms.FormEdit;
import com.ozguryazilim.telve.idm.IdmEvent;
import com.ozguryazilim.telve.idm.entities.User;
import com.ozguryazilim.telve.messages.FacesMessages;
import com.ozguryazilim.telve.messages.Messages;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kullanıcı tanımlama ekranı.
 * 
 * @author Hakan Uygun
 */
@FormEdit( feature = UserFeature.class )
public class UserHome extends FormBase<User, Long>{
    
    private static final Logger LOG = LoggerFactory.getLogger(UserHome.class);

    public static final String ACT_GENERATEPASSWORD = "PASSWORD";
    
    @Inject
    private ViewConfigResolver viewConfigResolver;
    
    @Inject
    private UserRepository repository;
    
    @Inject 
    private Event<IdmEvent>  event;
    
    @Inject
    private Event<UserDataChangeEvent> userEvent;
    
    @Inject
    private Identity identity;

    @Inject
    private EmailChannel emailChannel;

    @Inject
    private TelveConfigResolver telveConfigResolver;

    private String password;

    private Boolean createPasswordAndSend;
    
    private List<String> fragments;
    
    private ChangeLogStore changeLogStore = new ChangeLogStore();

    @Override
    public boolean onAfterLoad() {
        changeLogStore.clear();
        
        changeLogStore.addOldValue("general.label.FirstName", getEntity().getFirstName());
        changeLogStore.addOldValue("general.label.LastName", getEntity().getLastName());
        changeLogStore.addOldValue("user.label.UserType", getEntity().getUserType());
        changeLogStore.addOldValue("general.label.Email", getEntity().getEmail());
        changeLogStore.addOldValue("user.label.Manager", getEntity().getManager());
        changeLogStore.addOldValue("user.label.DomainGroup", getEntity().getDomainGroup() != null ? getEntity().getDomainGroup().getName() : null );
        
        return true;
    }

    
    
    @Override
    public boolean onBeforeSave() {

        if (createPasswordAndSend != null && createPasswordAndSend.equals(true)) {
            password = generatePassword();
            sendEmailWithLoginInformation();
            getAuditLogger().actionLog(getEntity().getClass().getSimpleName(), getEntity().getId(), getBizKeyValue(), AuditLogCommand.CAT_AUTH, ACT_GENERATEPASSWORD, identity.getLoginName(), "");
        }
        
        if( !Strings.isNullOrEmpty(password)){
            DefaultPasswordService passwordService = new DefaultPasswordService();
            getEntity().setPasswordEncodedHash(passwordService.encryptPassword(password));
            //yönetici tarafından parola değişikliği yapıldı.O zaman kullanıcı tekrar parola değiştirmek zorunda
            getEntity().setChangePassword(Boolean.TRUE);
            changeLogStore.addNewValue("user.caption.Password", "Changed");
        }
        
        
        User ofUser = repository.hasLoginName( getEntity().getLoginName(), getEntity().getId() == null ? 0 : getEntity().getId());
        if ( ofUser != null ) {
            FacesMessages.error(Messages.getMessage("user.message.SameUser"),Messages.getMessage("user.message.SameUser.Details"));
            return false;
        }
        
        
        changeLogStore.addNewValue("general.label.FirstName", getEntity().getFirstName());
        changeLogStore.addNewValue("general.label.LastName", getEntity().getLastName());
        changeLogStore.addNewValue("user.label.UserType", getEntity().getUserType());
        changeLogStore.addNewValue("general.label.Email", getEntity().getEmail());
        changeLogStore.addNewValue("user.label.Manager", getEntity().getManager());
        changeLogStore.addNewValue("user.label.DomainGroup", getEntity().getDomainGroup() != null ? getEntity().getDomainGroup().getName() : null );
        
        if ( !isValidTckn( getEntity().getTckn())) {
            return false;
        }
        
        return true;
    }

    @Override
    public boolean onAfterSave() {
        event.fire(new IdmEvent(IdmEvent.FROM_USER, IdmEvent.CREATE, getEntity().getLoginName()));
        userEvent.fire(new UserDataChangeEvent(getEntity().getLoginName()));
        createPasswordAndSend = false;
        return super.onAfterSave(); 
    }
    
    @Override
    public boolean onBeforeDelete() {
        event.fire(new IdmEvent(IdmEvent.FROM_USER, IdmEvent.DELETE, getEntity().getLoginName()));
        userEvent.fire(new UserDataChangeEvent(getEntity().getLoginName()));
        return super.onAfterSave(); 
    }
    
    

    /**
     * Geriye ek model UI fragmentlerinin listesi döner.
     *
     * @return
     */
    public List<String> getUIFragments() {

        if (fragments == null) {
            populateFragments();
        }

        LOG.info("UI Fragments : {}", fragments);

        return fragments;
    }

    /**
     * UI için gerekli fragment listesini hazırlar.
     */
    protected void populateFragments() {
        fragments = new ArrayList<>();

        for (UserModel m : UserModelRegistery.getUserModelMap().values()) {
            fragments.add(viewConfigResolver.getViewConfigDescriptor(m.fragment()).getViewId());
        }
    }

    
    /**
     * Geriye kullanıcı tiplerini döndürür.
     *
     * @return
     */
    public List<String> getUserTypes() {
        
        //Eğer kullanıcı SUPERADMIN değil ise başka bir kullanıcıyı SUPERADMIN yapamaz
        List<String> result = UserModelRegistery.getUserTypes();
        if( !UserModelRegistery.SUPER_ADMIN_TYPE.equals(identity.getUserInfo().getUserType())){
            result.remove(UserModelRegistery.SUPER_ADMIN_TYPE);
        }
        
        return UserModelRegistery.getUserTypes();
    }
    
    /**
     * Eğer kullanıcı SUPERADMIN değil ise kendi bilgilerinden kritik olan yerleri değiştiremez.
     * 
     * Örneğin UserType, DomainGroup v.b.
     * @return 
     */
    public Boolean canChangeCriticalData(){
        if( !identity.getLoginName().equals( getEntity().getLoginName())) return true;
        if( UserModelRegistery.SUPER_ADMIN_TYPE.equals(identity.getUserInfo().getUserType())) return true;
        return false;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getCreatePasswordAndSend() {
        return createPasswordAndSend;
    }

    public void setCreatePasswordAndSend(Boolean createPasswordAndSend) {
        this.createPasswordAndSend = createPasswordAndSend;
    }

    public Boolean getDomainGroupRequired(){
        return "true".equals(ConfigResolver.getPropertyValue("security.domainGroup.control", "false"));
    }
    
    @Override
    protected RepositoryBase<User, ?> getRepository() {
        return repository;
    }    

    public boolean canEditPassword(){
        return "true".equals(ConfigResolver.getPropertyValue("userHome.CanEditPassword", "true"));
    }
    
    @Override
    protected void auditLog(String action) {
        getAuditLogger().actionLog(getEntity().getClass().getSimpleName(), getEntity().getId(), getBizKeyValue(), AuditLogCommand.CAT_AUTH,  action, identity.getLoginName(), "", changeLogStore.getChangeValues());
    }

    private String generatePassword() {
        String upperCaseLetters = "ABCDEFGHJKMNPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghjkmnpqrstuvwxyz";
        String numbers = "23456789";
        String symbols = "@#$%";
        String possibleCharacters = upperCaseLetters + lowerCaseLetters + numbers + symbols;

        String initialPassword = RandomStringUtils
            .random(12, 0, possibleCharacters.toCharArray().length - 1, false, false,
                possibleCharacters.toCharArray(), new SecureRandom());
        String upperCaseLetter = RandomStringUtils
            .random(1, 0, upperCaseLetters.toCharArray().length - 1, false, false,
                upperCaseLetters.toCharArray(), new SecureRandom());
        String lowerCaseLetter = RandomStringUtils
            .random(1, 0, lowerCaseLetters.toCharArray().length - 1, false, false,
                lowerCaseLetters.toCharArray(), new SecureRandom());
        String number = RandomStringUtils
            .random(1, 0, numbers.toCharArray().length - 1, false, false,
                numbers.toCharArray(), new SecureRandom());
        String symbol = RandomStringUtils
            .random(1, 0, symbols.toCharArray().length - 1, false, false,
                symbols.toCharArray(), new SecureRandom());

        StringBuilder randomPassword = new StringBuilder();

        return randomPassword
            .append(initialPassword)
            .insert(new SecureRandom().nextInt(randomPassword.length()), upperCaseLetter)
            .insert(new SecureRandom().nextInt(randomPassword.length()), lowerCaseLetter)
            .insert(new SecureRandom().nextInt(randomPassword.length()), number)
            .insert(new SecureRandom().nextInt(randomPassword.length()), symbol)
            .toString();
    }

    private void sendEmailWithLoginInformation() {
        Map<String, Object> params = new HashMap<>();
        params.put("messageClass", "USERINFO");
        params.put("telveConfigResolver", telveConfigResolver);
        params.put("entity", getEntity());
        params.put("password", password);

        emailChannel.sendMessage(getEntity().getEmail(), "", "", params);
    }
    
    public boolean isValidTckn(String tckn) {
        if ( !"true".equals(ConfigResolver.getPropertyValue("user.rule.tcknRequired", "false"))) {
            return true;
        }
        try {
            String tmp = tckn;

            if (tmp.length() == 11) {
                int totalOdd = 0;

                int totalEven = 0;

                for (int i = 0; i < 9; i++) {
                    int val = Integer.valueOf(tmp.substring(i, i + 1));

                    if (i % 2 == 0) {
                        totalOdd += val;
                    } else {
                        totalEven += val;
                    }
                }

                int total = totalOdd + totalEven + Integer.valueOf(tmp.substring(9, 10));

                int lastDigit = total % 10;

                if (tmp.substring(10).equals(String.valueOf(lastDigit))) {
                    int check = (totalOdd * 7 - totalEven) % 10;

                    if (tmp.substring(9, 10).equals(String.valueOf(check))) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("TCKN Hatalı");
        }
        FacesMessages.error("TCKN Hatalı. Kayıt edilemez.");
        return false;
    }
    
}
