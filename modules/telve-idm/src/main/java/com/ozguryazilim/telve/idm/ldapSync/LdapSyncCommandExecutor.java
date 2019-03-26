package com.ozguryazilim.telve.idm.ldapSync;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.idm.entities.Group;
import com.ozguryazilim.telve.idm.entities.Role;
import com.ozguryazilim.telve.idm.entities.User;
import com.ozguryazilim.telve.idm.entities.UserGroup;
import com.ozguryazilim.telve.idm.entities.UserRole;
import com.ozguryazilim.telve.idm.group.GroupRepository;
import com.ozguryazilim.telve.idm.role.RoleRepository;
import com.ozguryazilim.telve.idm.user.UserGroupRepository;
import com.ozguryazilim.telve.idm.user.UserRepository;
import com.ozguryazilim.telve.idm.user.UserRoleRepository;
import com.ozguryazilim.telve.messagebus.command.AbstractCommandExecuter;
import com.ozguryazilim.telve.messagebus.command.CommandExecutor;
import com.ozguryazilim.telve.utils.TreeUtils;
import java.util.Hashtable;
import java.util.List;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.apache.shiro.config.Ini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Transactional
@CommandExecutor(command = LdapSyncCommand.class)
public class LdapSyncCommandExecutor extends AbstractCommandExecuter<LdapSyncCommand> {

    private static final Logger LOG = LoggerFactory.getLogger(LdapSyncCommandExecutor.class);
    private static final String OBJECT_CLASS = "(objectClass=*)";

    // tekrari azaltmak icin telveRealm degeri
    private String telveRealm = "telveRealm.";

    @Inject
    private UserRepository userRepository;

    @Inject
    private RoleRepository roleRepository;

    @Inject
    private UserRoleRepository userRoleRepository;

    @Inject
    private GroupRepository groupRepository;

    @Inject
    private UserGroupRepository userGroupRepository;

    @Override
    public void execute(LdapSyncCommand command) {

        try {
            // classpath uzerinden ini dosyasini okuyoruz
            Ini iniFile = Ini.fromResourcePath("classpath:shiro.ini");
            Ini.Section realm = iniFile.get("main");

            // realm uzerinden sik tekrarlanan bir kisim degerleri cekelim

            // Ldap baglantisi icin gerekli olan degerler
            Hashtable<String, String> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, realm.get(telveRealm + "contextFactory.url"));
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, realm.get(telveRealm + "contextFactory.systemUsername"));
            env.put(Context.SECURITY_CREDENTIALS, realm.get(telveRealm + "contextFactory.systemPassword"));

            // Ldap contextini olusturuyoruz
            LdapContext ldapContext = new InitialLdapContext(env, null);

            syncUsers(realm, ldapContext);

            // eger true donerse gruplari senkronize ediyoruz
            if (command.getSyncGroupsAndAssignUsers() != null && command.getSyncGroupsAndAssignUsers()) {
                syncGroups(realm, ldapContext, command);
            }

            // eger true donerse rolleri senkronize ediyoruz
            if (command.getSyncRolesAndAssignUsers() != null && command.getSyncRolesAndAssignUsers()) {
                syncRoles(realm, ldapContext);
            }
        } catch (Exception e) {
            LOG.error("There was an error during LdapSyncCommand", e);
        }
    }

    private void syncUsers(Ini.Section realm, LdapContext ldapContext) throws NamingException {
        String loginNameAttr = realm.get(telveRealm + "loginNameAttr");
        String firstNameAttr = realm.get(telveRealm + "firstNameAttr");
        String lastNameAttr = realm.get(telveRealm + "lastNameAttr");
        String emailAttr = realm.get(telveRealm + "emailAttr");

        SearchControls userSearchControls = new SearchControls();
        userSearchControls.setReturningAttributes(new String[]{loginNameAttr, firstNameAttr, lastNameAttr, emailAttr});
        userSearchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        // Ldap uzerinden kullanicilari ariyoruz
        NamingEnumeration<SearchResult> ldapUserResults = ldapContext
            .search(realm.get(telveRealm + "userSearchBase"), OBJECT_CLASS, userSearchControls);

        // Veritabanindaki kayitli ve otomatik uretilmis kullanicilari cekelim
        // en son elimizde kalanlari pasif duruma cekelim
        List<User> existingActiveUsers = userRepository.findAnyByAutoCreatedAndActive(Boolean.TRUE, Boolean.TRUE);

        // varsayilan rol tanimlanmis mi bakalim
        String defaultRole =
            !Strings.isNullOrEmpty(realm.get(telveRealm + "defaultRole"))
                ? realm.get(telveRealm + "defaultRole") : null;
        Role role = null;
        if (defaultRole != null) {
            role = roleRepository.findAnyByName(defaultRole);
        }

        while (ldapUserResults.hasMoreElements()) {
            Attributes attributes = ldapUserResults.next().getAttributes();

            String loginName =
                attributes.get(loginNameAttr) != null ? attributes.get(loginNameAttr).get().toString() : null;
            String firstName =
                attributes.get(firstNameAttr) != null ? attributes.get(firstNameAttr).get().toString() : null;
            String lastName =
                attributes.get(lastNameAttr) != null ? attributes.get(lastNameAttr).get().toString() : null;
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
                    newUser.setChangePassword(false);

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
                    LOG.info("User {} wasn't updated during LdapSyncCommand", user.getLoginName());
                }
            }
        }

        // kalan kullanicilar telve tarafinda ve aktif ancak Ldap tarafinda bulunmuyor
        // bunlarin durumunu pasife cekelim
        for (User user : existingActiveUsers) {
            user.setActive(Boolean.FALSE);
            userRepository.save(user);
        }
    }

    private void syncGroups(Ini.Section realm, LdapContext ldapContext, LdapSyncCommand command)
        throws NamingException {
        //otomatik olusturulmus gruplari bulalim
        List<Group> autoCreatedGroups = groupRepository.findAnyByAutoCreated(Boolean.TRUE);

        String groupNameAttr = realm.get(telveRealm + "groupNameAttr");
        String groupMembersAttr = realm.get(telveRealm + "groupMembersAttr");

        SearchControls groupSearchControls = new SearchControls();
        groupSearchControls.setReturningAttributes(new String[]{groupNameAttr, groupMembersAttr});
        groupSearchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        // ldap uzerinden sonuclari cekelim
        NamingEnumeration<SearchResult> ldapGroupResults = ldapContext
            .search(realm.get(telveRealm + "groupSearchBase"), OBJECT_CLASS, groupSearchControls);

        // sonuclarin uzerinden gecelim
        while (ldapGroupResults.hasMoreElements()) {
            Attributes ldapGroup = ldapGroupResults.next().getAttributes();

            String groupName =
                ldapGroup.get(groupNameAttr) != null ? ldapGroup.get(groupNameAttr).get().toString() : null;

            // eger grup adi mevcutsa islemleri yapalim
            if (groupName != null) {
                // gruba ait kullanicilar
                NamingEnumeration<?> members = ldapGroup.get(groupMembersAttr).getAll();

                // grup telve tarafinda kayitli mi?
                Group group = groupRepository.findAnyByName(groupName);

                // eger veritabaninda kayitli degil ve olusturulmasi icin parametre verilmis ise olusturalim
                if (command.getCreateMissingGroups() != null && group == null && command.getCreateMissingGroups()) {
                    Group newGroup = new Group();
                    newGroup.setActive(Boolean.TRUE);
                    newGroup.setCode(groupName);
                    newGroup.setName(groupName);
                    newGroup.setAutoCreated(Boolean.TRUE);
                    groupRepository.save(newGroup);
                    // path id'sini verip tekrar kaydedelim
                    newGroup.setPath(TreeUtils.getNodeIdPath(newGroup));
                    groupRepository.save(newGroup);
                    // grup degerini degistirelim
                    group = newGroup;
                }

                // eger grup var ise
                if (group != null) {

                    // grup kayitli ise userGroup uyelerini cekelim,
                    // ldap tarafinda silinen biri varsa biz de silecegiz userGroup'dan en sonda
                    List<UserGroup> groupMembers = userGroupRepository.findAnyByGroup(group);

                    // uyeleri while loopu ile cekelim
                    while (members.hasMoreElements()) {
                        // ustunde islem yapilacak uye
                        String member = members.next().toString();

                        // once kullaniciyi user tablosunda bulalim
                        User existingUser = userRepository.findAnyByLoginName(member);

                        // boyle bir kullanici var mi emin olalim
                        if (existingUser != null) {

                            // ardindan gruba coktan kayitli mi bir kontrol edelim
                            UserGroup existingUserGroup = userGroupRepository
                                .findAnyByUserAndGroup(existingUser, group);

                            // eger kayitli degil ise kaydini yapalim
                            if (existingUserGroup == null) {
                                UserGroup newUserGroup = new UserGroup();
                                newUserGroup.setGroup(group);
                                newUserGroup.setUser(existingUser);
                                userGroupRepository.save(newUserGroup);
                            }
                            // katiyliysa da guncelleyelim
                            else {
                                existingUserGroup.setUser(existingUser);
                                existingUserGroup.setGroup(group);
                                userGroupRepository.save(existingUserGroup);
                                // kullaniciyi listeden silelim
                                groupMembers.remove(existingUserGroup);
                            }
                        }
                    }

                    // ardindan kalan userGroup'lari veritabanindan silelim
                    for (UserGroup userGroup : groupMembers) {
                        userGroupRepository.remove(userGroup);
                    }

                    // islemler bitti, listeden cikaralim
                    autoCreatedGroups.remove(group);
                }
            }
        }

        // elimizde kalan kayitlari pasife cekelim
        for (Group remainingGroups : autoCreatedGroups) {
            remainingGroups.setActive(Boolean.FALSE);
            groupRepository.save(remainingGroups);
        }
    }

    private void syncRoles(Ini.Section realm, LdapContext ldapContext) throws NamingException {
        String roleNameAttr = realm.get(telveRealm + "roleNameAttr");
        String roleMembersAttr = realm.get(telveRealm + "roleMembersAttr");

        SearchControls roleSeachControls = new SearchControls();
        roleSeachControls.setReturningAttributes(new String[]{roleNameAttr, roleMembersAttr});
        roleSeachControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        // ldap uzerinden sonuclari cekelim
        NamingEnumeration<SearchResult> ldapRoleResults = ldapContext
            .search(realm.get(telveRealm + "roleSearchBase"), OBJECT_CLASS, roleSeachControls);

        // sonuclarin uzerinden gecelim
        while (ldapRoleResults.hasMoreElements()) {
            Attributes ldapGroup = ldapRoleResults.next().getAttributes();

            String roleName = ldapGroup.get(roleNameAttr) != null ? ldapGroup.get(roleNameAttr).get().toString() : null;

            if (roleName != null) {
                // role ait kullanicilar
                NamingEnumeration<?> members = ldapGroup.get(roleMembersAttr).getAll();

                // rol telve tarafinda kayitli mi?
                Role role = roleRepository.findAnyByName(roleName);

                // eger rol var ise
                if (role != null) {

                    // rol kayitli ise userRole uyelerini cekelim,
                    // ldap tarafinda silinen biri varsa biz de silecegiz userRole'den en sonda
                    List<UserRole> roleMembers = userRoleRepository.findAnyByRole(role);

                    // uyeleri while loopu ile cekelim
                    while (members.hasMoreElements()) {
                        // ustunde islem yapilacak uye
                        String member = members.next().toString();

                        // once kullaniciyi user tablosunda bulalim
                        User existingUser = userRepository.findAnyByLoginName(member);

                        // boyle bir kullanici var mi emin olalim
                        if (existingUser != null) {

                            // ardindan role coktan kayitli mi bir kontrol edelim
                            UserRole existingUserRole = userRoleRepository.findAnyByUserAndRole(existingUser, role);

                            // eger kayitli degil ise kaydini yapalim
                            if (existingUserRole == null) {
                                UserRole newUserRole = new UserRole();
                                newUserRole.setRole(role);
                                newUserRole.setUser(existingUser);
                                userRoleRepository.save(newUserRole);
                            }
                            // katiyliysa da guncelleyelim
                            else {
                                existingUserRole.setUser(existingUser);
                                existingUserRole.setRole(role);
                                userRoleRepository.save(existingUserRole);
                                // kullaniciyi listeden silelim
                                roleMembers.remove(existingUserRole);
                            }

                        }
                    }
                    // ardindan kalan userGroup'lari veritabanindan silelim
                    for (UserRole userRole : roleMembers) {
                        userRoleRepository.remove(userRole);
                    }
                }
            }
        }
    }
}
