package com.ozguryazilim.telve.idm;

import com.ozguryazilim.telve.auth.UserInfo;
import com.ozguryazilim.telve.auth.UserServiceProvider;
import com.ozguryazilim.telve.idm.entities.User;
import com.ozguryazilim.telve.idm.entities.UserAttribute;
import com.ozguryazilim.telve.idm.entities.UserGroup;
import com.ozguryazilim.telve.idm.entities.UserRole;
import com.ozguryazilim.telve.idm.user.UserGroupRepository;
import com.ozguryazilim.telve.idm.user.UserRepository;
import com.ozguryazilim.telve.idm.user.UserRoleRepository;
import com.ozguryazilim.telve.idm.user.UserViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.NoResultException;

/**
 * Telve UserServiceProvider implementasyonu.
 *
 * @author Hakan Uygun
 */
@Dependent
public class TelveIdmUserServiceProvider implements UserServiceProvider, Serializable {

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserRoleRepository userRoleRepository;

    @Inject
    private UserGroupRepository userGroupRepository;

    @Override
    public List<String> getLoginNames() {
        List<String> result = new ArrayList<>();
        List<UserViewModel> ls;

        ls = userRepository.lookupQuery(null);

        for (UserViewModel u : ls) {
            result.add(u.getLoginName());
        }

        return result;
    }

    @Override
    public List<String> getUsersByType(String type) {
        List<String> result = new ArrayList<>();
        List<UserViewModel> ls = userRepository.lookupQuery(null, type, null);

        for (UserViewModel u : ls) {
            result.add(u.getLoginName());
        }

        return result;
    }

    @Override
    public List<String> getUsersByGroup(String group) {
        List<String> result = new ArrayList<>();

        List<UserViewModel> ls = userRepository.lookupQuery(null, null, group);

        for (UserViewModel u : ls) {
            result.add(u.getLoginName());
        }

        return result;
    }

    @Override
    public List<String> getUsersByTypeAndGroup(String type, String group) {
        List<String> result = new ArrayList<>();

        List<UserViewModel> ls = userRepository.lookupQuery(null, type, group);

        for (UserViewModel u : ls) {
            result.add(u.getLoginName());
        }

        return result;
    }

    @Override
    public List<String> getUsersByTextAndTypeAndGroup(String searchText, String type, String group) {
        List<String> result = new ArrayList<>();

        List<UserViewModel> ls = userRepository.lookupQuery(searchText, type, group);

        for (UserViewModel u : ls) {
            result.add(u.getLoginName());
        }

        return result;
    }

    @Override
    public String getUserName(String loginName) {
        User u = userRepository.findAnyByLoginName(loginName);

        if (u != null) {
            return u.getFirstName() + " " + u.getLastName();
        }

        return null;
    }

    @Override
    public List<String> getUserRoles(String loginName) {
        User u = userRepository.findAnyByLoginName(loginName);
        if (u == null) {
            return Collections.emptyList();
        }

        List<UserRole> ls = userRoleRepository.findByUser(u);
        List<String> result = new ArrayList<>();

        for (UserRole ur : ls) {
            result.add(ur.getRole().getName());
        }

        return result;
    }

    @Override
    public List<String> getUserGroups(String loginName) {
        User u = userRepository.findAnyByLoginName(loginName);
        if (u == null) {
            return Collections.emptyList();
        }

        List<UserGroup> ls = userGroupRepository.findByUser(u);
        List<String> result = new ArrayList<>();

        for (UserGroup ur : ls) {
            result.add(ur.getGroup().getName());
        }

        return result;
    }

    @Override
    public List<String> getUserGroupsMembers(String loginName) {
        return userRepository.findAllGroupMembers(loginName);
    }

    @Override
    public Map<String, String> getUserAttibutes(String loginName) {
        Map<String, String> result = new HashMap<>();

        User u = userRepository.findAnyByLoginName(loginName);
        if (u == null) {
            return result;
        }

        for (UserAttribute ua : u.getAttributes().values()) {
            result.put(ua.getKey(), ua.getValue());
        }

        return result;
    }

    @Override
    public UserInfo getUserInfo(String loginName) {
        User u = userRepository.findAnyByLoginName(loginName);
        if (u == null) {
            return null;
        }
        return createUserInfo(u);
    }

    @Override
    public UserInfo getUserByEmail(String email) {
        User u;
        try {
            u = userRepository.findByEmail(email);
        } catch (NoResultException e) {
            return null;
        }
        if (u == null) {
            return null;
        }
        return createUserInfo(u);
    }

    private UserInfo createUserInfo(User u) {
        UserInfo ui = new UserInfo();

        ui.setChangePassword(u.getChangePassword());
        ui.setId(u.getId().toString());
        ui.setLoginName(u.getLoginName());
        ui.setFirstName(u.getFirstName());
        ui.setLastName(u.getLastName());
        ui.setEmail(u.getEmail());
        ui.setMobile(u.getMobile());
        ui.setUserType(u.getUserType());
        if (u.getDomainGroup() != null) {
            ui.setDomainGroupId(u.getDomainGroup().getId());
            ui.setDomainGroupName(u.getDomainGroup().getName());
            ui.setDomainGroupPath(u.getDomainGroup().getPath());
        }

        return ui;
    }
}
