package com.ozguryazilim.telve.idm.ldapSync;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.auth.UserDataChangeEvent;
import com.ozguryazilim.telve.idm.entities.Group;
import com.ozguryazilim.telve.idm.entities.Role;
import com.ozguryazilim.telve.idm.entities.User;
import com.ozguryazilim.telve.idm.entities.UserGroup;
import com.ozguryazilim.telve.idm.entities.UserRole;
import com.ozguryazilim.telve.idm.group.GroupRepository;
import com.ozguryazilim.telve.idm.ldapSync.groupsync.LdapGroupSyncCommand;
import com.ozguryazilim.telve.idm.role.RoleRepository;
import com.ozguryazilim.telve.idm.user.UserGroupRepository;
import com.ozguryazilim.telve.idm.user.UserRepository;
import com.ozguryazilim.telve.idm.user.UserRoleRepository;
import com.ozguryazilim.telve.messagebus.command.AbstractCommandExecuter;
import com.ozguryazilim.telve.messagebus.command.CommandExecutor;
import com.ozguryazilim.telve.messagebus.command.CommandSender;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.PagedResultsControl;
import javax.naming.ldap.PagedResultsResponseControl;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.apache.shiro.config.Ini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@CommandExecutor(command = LdapSyncCommand.class)
public class LdapSyncCommandExecutor extends AbstractCommandExecuter<LdapSyncCommand> {

    private static final Logger LOG = LoggerFactory.getLogger(LdapSyncCommandExecutor.class);

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

    @Inject
    private Event<IdmLdapSyncEvent> event;

    @Inject
    private Event<UserDataChangeEvent> userDataChangeEventEvent;

    @Inject
    private CommandSender commandSender;
    
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

            String scope = realm.get(telveRealm + "userScope");
            String pageSizeStr = realm.get(telveRealm + "pageSize");
            int pageSize = 1000;
            try {
                pageSize = Integer.parseInt(pageSizeStr);
            } catch (NumberFormatException e) {
                LOG.error("pageSize realm value must be an integer value, so pageSize has been set to 1000 as the default.", e);
            }

            syncUsers(realm, ldapContext, scope, pageSize);

            // eger true donerse gruplari senkronize ediyoruz
            if (command.getSyncGroupsAndAssignUsers() != null && command.getSyncGroupsAndAssignUsers()) {
                syncGroups(realm, ldapContext, scope, pageSize, command);
            }

            // eger true donerse rolleri senkronize ediyoruz
            if (command.getSyncRolesAndAssignUsers() != null && command.getSyncRolesAndAssignUsers()) {
                syncRoles(realm, ldapContext, scope, pageSize);
            }
        } catch (Exception e) {
            LOG.error("There was an error during LdapSyncCommand", e);
        }
    }

    private void syncUsers(Ini.Section realm, LdapContext ldapContext, String scope, int pageSize) throws NamingException {
        LOG.info("LDAP USER SYNCHRONIZATION STARTING...");
        String loginNameAttr = realm.get(telveRealm + "loginNameAttr");
        String firstNameAttr = realm.get(telveRealm + "firstNameAttr");
        String lastNameAttr = realm.get(telveRealm + "lastNameAttr");
        String emailAttr = realm.get(telveRealm + "emailAttr");
        String userSearchBase = realm.get(telveRealm + "userSearchBase");
        String userSyncFilter = realm.get(telveRealm + "userSyncFilter");
        String defaultRole = !Strings.isNullOrEmpty(realm.get(telveRealm + "defaultRole")) ?
            realm.get(telveRealm + "defaultRole") : null;

        // Veritabanindaki kayitli ve otomatik uretilmis kullanicilari cekelim
        // en son elimizde kalanlari pasif duruma cekelim
        List<User> existingActiveUsers = userRepository.findAnyByAutoCreatedAndActive(Boolean.TRUE, Boolean.TRUE);

        // varsayilan rol tanimlanmis mi bakalim
        Role role = null;
        if (defaultRole != null) {
            role = roleRepository.findAnyByName(defaultRole);
        }

        // Ldap den veri çekilip çekilmediği bilgisini saklar. Eğer hiç veri çekilmemişse bir sorun olduğu bellidir.
        // Bu durumda hiçbir şey yapılmamalı. Kullanıcılar pasifleştirilmemeli.
        boolean isAnyLdapUser = false;

        // Başarılı user create sync'ları sayacak counter.
        int createUserCounter = 0;
        // Başarılı user update sync'ları sayacak counter.
        int updateUserCounter = 0;
        // Deaktif edilen user'ları sayacak counter.
        int deactiveUserCounter = 0;

        try {
            SearchControls userSearchControls = new SearchControls();
            userSearchControls.setReturningAttributes(new String[]{loginNameAttr, firstNameAttr, lastNameAttr, emailAttr});
            setScope(scope, userSearchControls);

            ldapContext.setRequestControls(new Control[]{new PagedResultsControl(pageSize, true)});

            byte[] cookie = null;

            do {
                // Ldap uzerinden kullanicilari ariyoruz
                NamingEnumeration<SearchResult> ldapUserResults = ldapContext
                    .search(userSearchBase, userSyncFilter, userSearchControls);

                //Ldap üzerinden veri çekilip çekilmediğinin bilgisini elde ediyoruz.
                isAnyLdapUser = ldapUserResults.hasMoreElements();

                if(!isAnyLdapUser){
                    LOG.warn("There isn't any user coming from LDAP. User Sync is completed without any change...");
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

                    LOG.debug("LDAP User Sync - Login Name: {}, First Name: {}, Last Name: {}, E-mail: {}.", loginName, firstName, lastName, email);

                    // loginName yoksa kullaniciyi kaydetmeyelim/guncellemeyelim
                    if (loginName != null) {
                        // Kullanici veritabaninda kayitli mi kontrol edelim
                        User user = userRepository.findAnyByLoginName(loginName);

                        // Eger yoksa yeni kayit olusturalim
                        if (user == null) {
                            LOG.debug("User not found in database. Will be added. Login Name: {}", loginName);
                            User newUser = new User();

                            newUser.setLoginName(loginName);
                            newUser.setEmail(email);
                            newUser.setFirstName(firstName);
                            newUser.setLastName(lastName);
                            newUser.setAutoCreated(Boolean.TRUE);
                            newUser.setChangePassword(Boolean.FALSE);
                            newUser.setManaged(Boolean.FALSE);

                            userRepository.save(newUser);
                            LOG.debug("User added and active now. Login Name: {}", loginName);
                            createUserCounter++;
                            // varsayilan rol'u kontrol edelim, varsa atayalim
                            if (role != null) {
                                UserRole userRole = new UserRole();
                                userRole.setUser(newUser);
                                userRole.setRole(role);

                                userRoleRepository.save(userRole);
                                LOG.debug("User role added. Login Name: {}, Role: {}", loginName, role.getName());
                            }

                            // eger kullanici varsa bilgilerini guncelleyelim
                            // autoCreated degilse telve tarafinda kullanicisi elle acilmis demektir, bunu otomatik olarak
                            // ldap'a baglamak mantikli olmaz
                        } else if (user.getAutoCreated()) {
                            LOG.debug("User already saved in database. Login Name: {}", loginName);
                            user.setFirstName(firstName);
                            user.setLastName(lastName);
                            user.setEmail(email);
                            user.setActive(Boolean.TRUE);

                            userRepository.save(user);
                            LOG.debug("User updated and active now. Login Name: {}", loginName);
                            updateUserCounter++;
                            // varsayilan rol'e ekli mi? ekli degilse ekleyelim
                            if (role != null) {
                                UserRole userRole = userRoleRepository.findAnyByUserAndRole(user, role);

                                if (userRole == null) {
                                    UserRole newUserRole = new UserRole();
                                    newUserRole.setUser(user);
                                    newUserRole.setRole(role);

                                    userRoleRepository.save(newUserRole);
                                    LOG.debug("User role updated. Login Name: {}, Role: {}", loginName, role.getName());
                                }
                            }
                            // islemler bitti, kullaniciyi varsa listeden cikaralim
                            existingActiveUsers.remove(user);
                            userDataChangeEventEvent.fire(new UserDataChangeEvent(user.getLoginName()));

                            // kullaniciyi guncellemiyorsak loglayalim
                        } else {
                            LOG.info("User {} wasn't updated during LdapSyncCommand", user.getLoginName());
                        }
                    } else {
                        LOG.warn("Username not found for LDAP user record. First Name: {}, Last Name: {}.", firstName, lastName);
                    }
                }

                cookie = getControlResponse(ldapContext.getResponseControls());

                ldapContext.setRequestControls(new Control[]{new PagedResultsControl(pageSize, cookie, Control.CRITICAL)});

            } while (cookie.length != 0);

        } catch (NamingException | IOException e) {
            LOG.error("There was an error during LdapSyncCommand - users", e);
            // Hata olustu, kullanicilari pasif yapmayalim.
            return;
        }
        LOG.debug("User deactivation process starting... ");
        // kalan kullanicilar telve tarafinda ve aktif ancak Ldap tarafinda bulunmuyor
        // bunlarin durumunu pasife cekelim
        // Eğer Ldap üzerinden hiç veri çekilmemiş ise bu işlemi gerçekleştirmeyelim
        if (isAnyLdapUser) {
            for (User user : existingActiveUsers) {
                user.setActive(Boolean.FALSE);
                userRepository.save(user);
                userDataChangeEventEvent.fire(new UserDataChangeEvent(user.getLoginName()));
                LOG.debug("User updated and passive now. Login Name: {}", user.getLoginName());
                deactiveUserCounter++;
            }
        }

        LOG.info("LDAP USER SYNCHRONIZATION COMPLETED. Created Users Count: {}, Updated Users Count: {}, Deactivated Users Count: {}",
                createUserCounter, updateUserCounter, deactiveUserCounter);
        
        event.fire(new IdmLdapSyncEvent(IdmLdapSyncEvent.USER));
    }

    private void syncGroups(Ini.Section realm, LdapContext ldapContext, String scope, int pageSize, LdapSyncCommand command)
        throws NamingException {
        LOG.info("LDAP GROUP SYNCHRONIZATION STARTING...");
        //otomatik olusturulmus gruplari bulalim
        List<Group> autoCreatedGroups = groupRepository.findAnyByAutoCreated(Boolean.TRUE);

        String groupNameAttr = realm.get(telveRealm + "groupNameAttr");
        String groupMembersAttr = realm.get(telveRealm + "groupMembersAttr");
        boolean queryGroupMembersWithAttr = Boolean.parseBoolean(realm.get(telveRealm + "queryGroupMembersWithAttrDN"));
        String groupSearchBase = realm.get(telveRealm + "groupSearchBase");
        String groupSyncFilter = realm.get(telveRealm + "groupSyncFilter");

        try {
            SearchControls groupSearchControls = new SearchControls();
            groupSearchControls.setReturningAttributes(new String[]{groupNameAttr, groupMembersAttr});
            setScope(scope, groupSearchControls);

            ldapContext.setRequestControls(new Control[]{new PagedResultsControl(pageSize, true)});

            byte[] cookie = null;

            List<String> ldapGroupCodes = new ArrayList<>();
            do {
                // ldap uzerinden sonuclari cekelim
                NamingEnumeration<SearchResult> ldapGroupResults = ldapContext
                    .search(groupSearchBase, groupSyncFilter, groupSearchControls);

                if(!ldapGroupResults.hasMoreElements()){
                    LOG.warn("There isn't any group coming from LDAP. Group Sync is completed without any change...");
                }
                // sonuclarin uzerinden gecelim
                while (ldapGroupResults.hasMoreElements()) {
                    // Collect LDAP group codes
                    SearchResult groupSearchResult = ldapGroupResults.next();
                    Attributes groupSearchResultAttributes = groupSearchResult.getAttributes();
                    if (groupSearchResultAttributes.get(groupNameAttr) != null) {
                        ldapGroupCodes.add(groupSearchResultAttributes.get(groupNameAttr).get().toString());
                    }

                    // Filter autoCreatedGroups
                    autoCreatedGroups = autoCreatedGroups.stream()
                            .filter(group -> ldapGroupCodes.contains(group.getCode()))
                            .collect(Collectors.toList());

                    LdapGroupSyncCommand ldapGroupSyncCommand = new LdapGroupSyncCommand(command, groupSearchResult);
                    commandSender.sendCommand(ldapGroupSyncCommand);
                }

                cookie = getControlResponse(ldapContext.getResponseControls());

                ldapContext.setRequestControls(new Control[]{new PagedResultsControl(pageSize, cookie, Control.CRITICAL)});

            } while (cookie.length != 0);

        } catch (NamingException | IOException e) {
            LOG.error("There was an error during LdapSyncCommand - groups", e);
            // Hata olustu, grouplari pasif yapmayalim.
            return;
        }
        LOG.debug("Group deactivation process starting...");
        // elimizde kalan kayitlari pasife cekelim
        for (Group remainingGroups : autoCreatedGroups) {
            remainingGroups.setActive(Boolean.FALSE);
            groupRepository.save(remainingGroups);
            userGroupRepository.findByGroup(remainingGroups).stream()
                    .map(UserGroup::getUser)
                    .forEach(user -> userDataChangeEventEvent.fire(new UserDataChangeEvent(user.getLoginName())));

            LOG.debug("Group updated and passive now. Group Name: {}", remainingGroups.getName());
        }
    }

    private void syncRoles(Ini.Section realm, LdapContext ldapContext, String scope, int pageSize) throws NamingException {
        LOG.info("LDAP ROLES SYNCHRONIZATION STARTING...");
        String roleNameAttr = realm.get(telveRealm + "roleNameAttr");
        String roleMembersAttr = realm.get(telveRealm + "roleMembersAttr");
        String roleSearchBase = realm.get(telveRealm + "roleSearchBase");
        String roleSyncFilter = realm.get(telveRealm + "roleSyncFilter");

        // Başarılı user role eklemelerini sayacak counter.
        int createUserRoleCounter = 0;
        // Başarılı user role update'leri sayacak counter.
        int updateUserRoleCounter = 0;
        // Silinen user role'ları sayacak counter.
        int removeUserRoleCounter = 0;

        try {
            SearchControls roleSeachControls = new SearchControls();
            roleSeachControls.setReturningAttributes(new String[]{roleNameAttr, roleMembersAttr});
            setScope(scope, roleSeachControls);

            ldapContext.setRequestControls(new Control[]{new PagedResultsControl(pageSize, true)});

            byte[] cookie = null;

            do {
                // ldap uzerinden sonuclari cekelim
                NamingEnumeration<SearchResult> ldapRoleResults = ldapContext
                    .search(roleSearchBase, roleSyncFilter, roleSeachControls);

                if(!ldapRoleResults.hasMoreElements()){
                    LOG.warn("There isn't any role coming from LDAP. Role Sync is completed without any change...");
                }

                // sonuclarin uzerinden gecelim
                while (ldapRoleResults.hasMoreElements()) {
                    Attributes ldapGroup = ldapRoleResults.next().getAttributes();

                    String roleName = ldapGroup.get(roleNameAttr) != null ? ldapGroup.get(roleNameAttr).get().toString() : null;

                    LOG.debug("LDAP Role Sync - Role Name: {}.", roleName);

                    if (roleName != null) {
                        // role ait kullanicilar
                        NamingEnumeration<?> members = ldapGroup.get(roleMembersAttr) != null
                            ? ldapGroup.get(roleMembersAttr).getAll() : null;

                        // rol telve tarafinda kayitli mi?
                        Role role = roleRepository.findAnyByName(roleName);

                        // eger rol var ise
                        if (role != null) {
                            LOG.debug("LDAP Role already exist in database - Role Name: {}.", roleName);
                            // rol kayitli ise userRole uyelerini cekelim,
                            // ldap tarafinda silinen biri varsa biz de silecegiz userRole'den en sonda
                            List<UserRole> roleMembers = userRoleRepository.findAnyByRole(role);
                            if(members == null || !members.hasMoreElements()){
                                LOG.warn("There isn't any role member coming from LDAP. Role Name: {}", roleName);
                            }
                            // uyeleri while loopu ile cekelim
                            while (members != null && members.hasMoreElements()) {
                                // ustunde islem yapilacak uye
                                String member = members.next().toString();

                                LOG.debug("Role Member Checking... Role Name: {}, Member Name: {}", roleName, member);

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
                                        newUserRole.setAutoCreated(true);
                                        userRoleRepository.save(newUserRole);
                                        LOG.debug("Role member not found. Member is added. Role Name: {}, Member Name: {}", roleName, member);
                                        createUserRoleCounter++;
                                    }
                                    // katiyliysa da guncelleyelim
                                    else {
                                        existingUserRole.setUser(existingUser);
                                        existingUserRole.setRole(role);
                                        existingUserRole.setAutoCreated(true);
                                        userRoleRepository.save(existingUserRole);
                                        // kullaniciyi listeden silelim
                                        roleMembers.remove(existingUserRole);
                                        LOG.debug("Role member already added. Member is updated. Role Name: {}, Member Name: {}", roleName, member);
                                        updateUserRoleCounter++;
                                    }

                                    userDataChangeEventEvent.fire(new UserDataChangeEvent(existingUser.getLoginName()));

                                } else {
                                    LOG.debug("Role member not found in database. User can't added to role. Role Name: {}, Member Name: {}", roleName, member);
                                }
                            }
                            LOG.debug("Role member removing process starting...");
                            // ardindan kalan userGroup'lari veritabanindan silelim
                            for (UserRole userRole : roleMembers) {
                                userRoleRepository.remove(userRole);
                                if(userRole != null && userRole.getRole() != null && userRole.getUser() != null){
                                    LOG.debug("User Role removed. Role Name: {}, Member Name: {}", userRole.getRole().getName(),
                                            userRole.getUser().getLoginName());

                                    userDataChangeEventEvent.fire(new UserDataChangeEvent(userRole.getUser().getLoginName()));
                                }
                                removeUserRoleCounter++;
                            }
                        } else {
                            LOG.warn("Role not found in database. Role Name: {}.", roleName);
                        }
                    } else {
                        LOG.warn("Role Name not found for LDAP Role record.");
                    }
                }
                cookie = getControlResponse(ldapContext.getResponseControls());

                ldapContext.setRequestControls(new Control[]{new PagedResultsControl(pageSize, cookie, Control.CRITICAL)});

            } while (cookie.length != 0);

        } catch (NamingException | IOException e) {
            LOG.error("There was an error during LdapSyncCommand - roles", e);
        }
        LOG.info("LDAP ROLE SYNCHRONIZATION COMPLETED. Created User Roles Count: {}, Updated User Roles Count: {}, Removed User Roles Count: {}",
                createUserRoleCounter, updateUserRoleCounter, removeUserRoleCounter);
        event.fire(new IdmLdapSyncEvent(IdmLdapSyncEvent.ROLE));
    }

    private void setScope(String scope, SearchControls controls) {
        int searchScope = SearchControls.ONELEVEL_SCOPE;
        if (!Strings.isNullOrEmpty(scope)) {
            if (scope.equals("sub")) {
                searchScope = SearchControls.SUBTREE_SCOPE;
            } else if (scope.equals("base")) {
                searchScope = SearchControls.OBJECT_SCOPE;
            }
        }
        controls.setSearchScope(searchScope);
    }

    private static byte[] getControlResponse(Control[] controls) throws NamingException {
        // Examine the paged results control response
        byte[] cookie = null;
        if (controls != null) {
            for (int i = 0; i < controls.length; i++) {
                if (controls[i] instanceof PagedResultsResponseControl) {
                    PagedResultsResponseControl prrc = (PagedResultsResponseControl)controls[i];
                    cookie = prrc.getCookie();
                }
            }
        }
        return (cookie == null) ? new byte[0] : cookie;
    }

}
