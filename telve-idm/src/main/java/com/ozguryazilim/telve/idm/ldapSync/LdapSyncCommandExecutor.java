package com.ozguryazilim.telve.idm.ldapSync;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.idm.entities.Role;
import com.ozguryazilim.telve.idm.entities.User;
import com.ozguryazilim.telve.idm.entities.UserRole;
import com.ozguryazilim.telve.idm.role.RoleRepository;
import com.ozguryazilim.telve.idm.user.UserRepository;
import com.ozguryazilim.telve.idm.user.UserRoleRepository;
import com.ozguryazilim.telve.messagebus.command.AbstractCommandExecuter;
import com.ozguryazilim.telve.messagebus.command.CommandExecutor;
import org.apache.shiro.config.Ini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;
import java.util.List;

@CommandExecutor(command = LdapSyncCommand.class)
public class LdapSyncCommandExecutor extends AbstractCommandExecuter<LdapSyncCommand> {

    private static final Logger LOG = LoggerFactory.getLogger(LdapSyncCommandExecutor.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private RoleRepository roleRepository;

    @Inject
    private UserRoleRepository userRoleRepository;

    @Override
    public void execute(LdapSyncCommand command) {

        try {
            // tekrari azaltmak icin telveRealm degeri
            String telveRealm = "telveRealm.";

            // classpath uzerinden ini dosyasini okuyoruz
            Ini iniFile = Ini.fromResourcePath("classpath:shiro.ini");
            Ini.Section realm = iniFile.get("main");

            // realm uzerinden sik tekrarlanan bir kisim degerleri cekelim
            String loginNameAttr = realm.get(telveRealm + "loginNameAttr");
            String firstNameAttr = realm.get(telveRealm + "firstNameAttr");
            String lastNameAttr = realm.get(telveRealm + "lastNameAttr");
            String emailAttr = realm.get(telveRealm + "emailAttr");

            // Ldap baglantisi icin gerekli olan degerler
            Hashtable<String, String> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, realm.get(telveRealm + "contextFactory.url"));
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, realm.get(telveRealm + "contextFactory.systemUsername"));
            env.put(Context.SECURITY_CREDENTIALS, realm.get(telveRealm + "contextFactory.systemPassword"));

            // Ldap contextini olusturuyoruz
            LdapContext ldapContext = new InitialLdapContext(env, null);

            SearchControls searchControls = new SearchControls();
            searchControls.setReturningAttributes(new String[]{loginNameAttr, firstNameAttr, lastNameAttr, emailAttr});
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            // Ldap uzerinden kullanicilari ariyoruz
            NamingEnumeration<SearchResult> ldapUserResults = ldapContext.search(realm.get(telveRealm +
                    "userSearchBase"), "(objectClass=*)", searchControls);

            // Veritabanindaki kayitli ve otomatik uretilmis kullanicilari cekelim
            // en son elimizde kalanlari pasif duruma cekelim
            List<User> existingActiveUsers = userRepository.findAnyByAutoCreatedAndActive(Boolean.TRUE, Boolean.TRUE);

            // varsayilan rol tanimlanmis mi bakalim, gerekiyorsa olusturalim
            String defaultRole = !Strings.isNullOrEmpty(realm.get(telveRealm + "defaultRole")) ? realm.get(telveRealm
                    + "defaultRole") : null;
            Role role = null;
            if (defaultRole != null) {
                role = roleRepository.findAnyByName(defaultRole);
            }

            while (ldapUserResults.hasMoreElements()) {
                Attributes attributes = ldapUserResults.next().getAttributes();

                String loginName = attributes.get(loginNameAttr) != null ? attributes.get(loginNameAttr).get()
                        .toString() : null;
                String firstName = attributes.get(firstNameAttr) != null ? attributes.get(firstNameAttr).get()
                        .toString() : null;
                String lastName = attributes.get(lastNameAttr) != null ? attributes.get(lastNameAttr).get().toString
                        () : null;
                String email = attributes.get(emailAttr) != null ? attributes.get(emailAttr).get().toString() : null;

                // loginName yoksa kullaniciyi kaydetmeyelim/guncellemeyelim
                if (loginName != null) {
                    // Kullanici veritabaninda kayitli mi kontrol edelim
                    User user = userRepository.findAnyByLoginName(loginName);

                    // Eger yoksa yeni kayit olusturalim
                    if (user == null) {
                        User newUser = new User();

                        newUser.setLoginName(loginName);
                        newUser.setEmail(email);
                        newUser.setFirstName(firstName);
                        newUser.setLastName(lastName);
                        newUser.setAutoCreated(Boolean.TRUE);

                        userRepository.save(newUser);

                        // varsayilan rol'u kontrol edelim, varsa atayalim
                        if (role != null) {
                            UserRole userRole = new UserRole();
                            userRole.setUser(newUser);
                            userRole.setRole(role);

                            userRoleRepository.save(userRole);
                        }

                        // eger kullanici varsa bilgilerini guncelleyelim
                        // autoCreated degilse telve tarafinda kullanicisi elle acilmis demektir, bunu otomatik olarak
                        // ldap'a baglamak mantikli olmaz
                    } else if (user.getAutoCreated()) {
                        user.setFirstName(firstName);
                        user.setLastName(lastName);
                        user.setEmail(email);
                        user.setActive(Boolean.TRUE);

                        userRepository.save(user);

                        // varsayilan rol'e ekli mi? ekli degilse ekleyelim
                        if (role != null) {
                            UserRole userRole = userRoleRepository.findAnyByUserAndRole(user, role);

                            if (userRole == null) {
                                UserRole newUserRole = new UserRole();
                                newUserRole.setUser(user);
                                newUserRole.setRole(role);

                                userRoleRepository.save(newUserRole);
                            }
                        }
                        // islemler bitti, kullaniciyi varsa listeden cikaralim
                        existingActiveUsers.remove(user);

                        // kullaniciyi guncellemiyorsak loglayalim
                    } else {
                        LOG.info("User " + user.getLoginName() + " wasn't updated during LdapSyncCommand");
                    }
                }
            }

            // kalan kullanicilar telve tarafinda ve aktif ancak Ldap tarafinda bulunmuyor
            // bunlarin durumunu pasife cekelim
            for (User user : existingActiveUsers) {
                user.setActive(Boolean.FALSE);
                userRepository.save(user);
            }

        } catch (Exception e) {
            LOG.error("There was an error during LdapSyncCommand", e);
            e.printStackTrace();
        }
    }
}
