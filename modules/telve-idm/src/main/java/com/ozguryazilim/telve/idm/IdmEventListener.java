/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm;

import com.ozguryazilim.telve.idm.entities.Role;
import com.ozguryazilim.telve.idm.entities.UserRole;
import com.ozguryazilim.telve.idm.role.RoleRepository;
import com.ozguryazilim.telve.idm.user.UserRepository;
import com.ozguryazilim.telve.idm.user.UserRoleRepository;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.SimplePrincipalCollection;

/**
 *
 * @author oyas
 */
@Dependent
public class IdmEventListener {

    @Inject
    private UserRepository userRepository;

    @Inject
    private RoleRepository roleRepository;

    @Inject
    private UserRoleRepository userRoleRepository;

    public void onIdmEvent(@Observes IdmEvent event) {
        //clearCache(principals);
        switch (event.getFrom()) {
            case IdmEvent.FROM_USER:
                clearUserCaches(event.getSubject());
                break;
            case IdmEvent.FROM_ROLE:
                clearRoleCaches(event.getSubject());
                break;
            case IdmEvent.FROM_GROUP:
                clearGroupCaches(event.getSubject());
                break;
        }
    }

    private void clearUserCaches(String subject) {

        org.apache.shiro.mgt.SecurityManager sm = SecurityUtils.getSecurityManager();

        if (sm instanceof RealmSecurityManager) {
            for (Realm r : ((RealmSecurityManager) sm).getRealms()) {
                if (r instanceof TelveIdmRealm) {
                    SimplePrincipalCollection spc = new SimplePrincipalCollection(subject, r.getName());
                    ((TelveIdmRealm) r).clearAuthCache(spc);
                }
            }
        }
    }

    private void clearRoleCaches(String subject) {
        Role r = roleRepository.findAnyByName(subject);
        List<UserRole> userRoles = userRoleRepository.findByRole(r);

        SimplePrincipalCollection spc = new SimplePrincipalCollection();

        org.apache.shiro.mgt.SecurityManager sm = SecurityUtils.getSecurityManager();
        if (sm instanceof RealmSecurityManager) {
            for (Realm realm : ((RealmSecurityManager) sm).getRealms()) {
                if (realm instanceof TelveIdmRealm) {
                    for (UserRole ur : userRoles) {
                        spc.add(ur.getUser().getLoginName(), realm.getName());
                    }
                    ((TelveIdmRealm) realm).clearAuthCache(spc);
                }
            }
        }
    }

    private void clearGroupCaches(String subject) {
        //Grupla ilgili bir cache tutmuyoruz Realm seviyesinde.
    }
}
