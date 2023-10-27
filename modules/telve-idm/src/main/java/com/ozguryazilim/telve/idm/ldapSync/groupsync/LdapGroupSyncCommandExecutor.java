package com.ozguryazilim.telve.idm.ldapSync.groupsync;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.auth.UserDataChangeEvent;
import com.ozguryazilim.telve.idm.entities.Group;
import com.ozguryazilim.telve.idm.entities.User;
import com.ozguryazilim.telve.idm.entities.UserGroup;
import com.ozguryazilim.telve.idm.group.GroupRepository;
import com.ozguryazilim.telve.idm.ldapSync.IdmLdapSyncEvent;
import com.ozguryazilim.telve.idm.user.UserGroupRepository;
import com.ozguryazilim.telve.idm.user.UserRepository;
import com.ozguryazilim.telve.messagebus.command.AbstractCommandExecuter;
import com.ozguryazilim.telve.messagebus.command.CommandExecutor;
import com.ozguryazilim.telve.utils.TreeUtils;
import javax.enterprise.event.Event;
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

import java.util.Hashtable;
import java.util.List;

@Transactional
@CommandExecutor(command = LdapGroupSyncCommand.class)
public class LdapGroupSyncCommandExecutor extends AbstractCommandExecuter<LdapGroupSyncCommand> {

    private static final Logger LOG = LoggerFactory.getLogger(LdapGroupSyncCommand.class);

    private String telveRealm = "telveRealm.";

    @Inject
    private GroupRepository groupRepository;

    @Inject
    private UserGroupRepository userGroupRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private Event<UserDataChangeEvent> userDataChangeEventEvent;

    @Inject
    private Event<IdmLdapSyncEvent> event;

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

    public String getLoginNameWithUserDn(Ini.Section realm, String scope, LdapContext ldapContext, String dn) {
        String loginNameAttr = realm.get(telveRealm + "loginNameAttr");
        String firstNameAttr = realm.get(telveRealm + "firstNameAttr");
        String lastNameAttr = realm.get(telveRealm + "lastNameAttr");
        String emailAttr = realm.get(telveRealm + "emailAttr");
        String userSyncFilter = realm.get(telveRealm + "userSyncFilter");

        SearchControls userSearchControls = new SearchControls();
        userSearchControls.setReturningAttributes(new String[]{loginNameAttr, firstNameAttr, lastNameAttr, emailAttr});
        setScope(scope, userSearchControls);

        try {
            NamingEnumeration<SearchResult> groupUserSearchResult = ldapContext.search(dn, userSyncFilter, userSearchControls);
            if (groupUserSearchResult != null && groupUserSearchResult.hasMoreElements()) {
                Attributes attributes = groupUserSearchResult.next().getAttributes();
                String member = attributes != null ? attributes.get(loginNameAttr).get().toString() : null;
                LOG.debug("LDAP member with DN found. DN: {}", dn);
                return member;
            } else {
                LOG.error("LDAP member with DN not found. DN: {}", dn);
            }
        } catch (NamingException ex) {
            LOG.error("Error while searching member DN: {}", dn, ex);
        }
        return null;
    }

    private void syncGroups(Ini.Section realm,
                            LdapContext ldapContext,
                            String scope,
                            int pageSize,
                            LdapGroupSyncCommand command) {
        LOG.info("LDAP GROUP SYNCHRONIZATION STARTING.");
        //otomatik olusturulmus gruplari bulalim

        String groupNameAttr = realm.get(telveRealm + "groupNameAttr");
        String groupMembersAttr = realm.get(telveRealm + "groupMembersAttr");
        boolean queryGroupMembersWithAttr = Boolean.parseBoolean(realm.get(telveRealm + "queryGroupMembersWithAttrDN"));
        try {

            // sonuclarin uzerinden gecelim
            Attributes ldapGroup = command.getGroupSearchResult().getAttributes();

            String groupCode = ldapGroup.get(groupNameAttr) != null ? ldapGroup.get(groupNameAttr).get().toString() : null;

            LOG.debug("LDAP Group Sync - LDAP Group Name: {}", groupCode);

            // eger grup adi mevcutsa islemleri yapalim
            if (groupCode != null) {

                if (groupCode.length() > 30)
                    groupCode = groupCode.substring(0, 30);

                // gruba ait kullanicilar
                NamingEnumeration<?> members = ldapGroup.get(groupMembersAttr) != null
                        ? ldapGroup.get(groupMembersAttr).getAll() : null;

                // grup telve tarafinda kayitli mi?
                Group group = groupRepository.findAnyByCode(groupCode);

                // eger veritabaninda kayitli degil ve olusturulmasi icin parametre verilmis ise olusturalim
                if (command.getCreateMissingGroups() != null && group == null && command.getCreateMissingGroups()) {
                    LOG.debug("Group not found in database. Will be added. LDAP Group Name: {}", groupCode);
                    Group newGroup = new Group();
                    newGroup.setActive(Boolean.TRUE);
                    newGroup.setCode(groupCode);
                    newGroup.setName(groupCode);
                    newGroup.setAutoCreated(Boolean.TRUE);
                    groupRepository.save(newGroup);
                    // path id'sini verip tekrar kaydedelim
                    newGroup.setPath(TreeUtils.getNodeIdPath(newGroup));
                    groupRepository.save(newGroup);
                    // grup degerini degistirelim
                    group = newGroup;
                    LOG.debug("Group added to database. LDAP Group Name: {}", groupCode);
                }

                // eger grup var ise
                if (group != null) {
                    LOG.debug("Group already saved in database. LDAP Group Name: {}", groupCode);
                    // grup kayitli ise userGroup uyelerini cekelim,
                    // ldap tarafinda silinen biri varsa biz de silecegiz userGroup'dan en sonda
                    List<UserGroup> groupMembers = userGroupRepository.findAnyByGroup(group);

                    if(members == null || !members.hasMoreElements()){
                        LOG.warn("There isn't any group member coming from LDAP. LDAP Group Name: {}", groupCode);
                    }

                    // uyeleri while loopu ile cekelim
                    while (members != null && members.hasMoreElements()) {
                        // ustunde islem yapilacak uye
                        String member = members.next().toString();

                        //Get group search result with dn
                        if (queryGroupMembersWithAttr) {
                            String loginNameFromDn = getLoginNameWithUserDn(realm, scope, ldapContext, member);
                            member = loginNameFromDn != null ? loginNameFromDn : member;
                        }

                        LOG.debug("Group Member Checking... LDAP Group Name: {}, Member Name: {}", groupCode, member);

                        // once kullaniciyi user tablosunda bulalim
                        User existingUser = userRepository.findAnyByLoginName(member);

                        // boyle bir kullanici var mi emin olalim
                        if (existingUser != null) {

                            // ardindan gruba coktan kayitli mi bir kontrol edelim
                            UserGroup existingUserGroup = userGroupRepository.findAnyByUserAndGroup(existingUser, group);

                            // eger kayitli degil ise kaydini yapalim
                            if (existingUserGroup == null) {
                                UserGroup newUserGroup = new UserGroup();
                                newUserGroup.setGroup(group);
                                newUserGroup.setUser(existingUser);
                                newUserGroup.setAutoCreated(true);
                                userGroupRepository.save(newUserGroup);
                                LOG.debug("Group member not found. Member is added. LDAP Group Name: {}, Member Name: {}", groupCode, member);
                            }
                            // katiyliysa da guncelleyelim
                            else {
                                existingUserGroup.setUser(existingUser);
                                existingUserGroup.setGroup(group);
                                existingUserGroup.setAutoCreated(true);
                                userGroupRepository.save(existingUserGroup);
                                // kullaniciyi listeden silelim
                                groupMembers.remove(existingUserGroup);
                                LOG.debug("Group member already added. Member is updated. LDAP Group Name: {}, Member Name: {}", groupCode, member);
                            }

                            userDataChangeEventEvent.fire(new UserDataChangeEvent(existingUser.getLoginName()));
                        } else {
                            LOG.warn("Group member not found in database. User can't be added to group. LDAP Group Name: {}, Member Name: {}", groupCode, member);
                        }
                    }
                    LOG.debug("Group member removing process starting. LDAP Group Name: {}", groupCode);
                    // ardindan kalan userGroup'lari veritabanindan silelim
                    for (UserGroup userGroup : groupMembers) {
                        userGroupRepository.remove(userGroup);
                        if(userGroup != null && userGroup.getGroup() != null && userGroup.getUser() != null){
                            LOG.debug("Group Member Removed. Group Name: {}, Member Name: {}", userGroup.getGroup().getName(),
                                    userGroup.getUser().getLoginName());

                            userDataChangeEventEvent.fire(new UserDataChangeEvent(userGroup.getUser().getLoginName()));
                        }
                    }
                }
            } else {
                LOG.warn("Group Name not found for LDAP group record.");
            }
        } catch (NamingException e) {
            LOG.error("There was an error during LdapSyncCommand - groups", e);
            // Hata olustu, grouplari pasif yapmayalim.
            return;
        }

        event.fire(new IdmLdapSyncEvent(IdmLdapSyncEvent.GROUP));
    }

    @Override
    public void execute(LdapGroupSyncCommand command) {
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
                LOG.error("Could not format realm pageSize value: " +
                        "pageSize realm value must be an Integer value, so pageSize has been set to 1000 as default. " +
                        "Value: {}", pageSizeStr);
            }

            syncGroups(realm, ldapContext, scope, pageSize, command);
        } catch (Exception e) {
            LOG.error("There was an error during LdapGroupSyncCommand", e);
        }
    }
}
