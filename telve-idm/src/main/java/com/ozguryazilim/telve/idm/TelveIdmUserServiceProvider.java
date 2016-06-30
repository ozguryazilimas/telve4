/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm;

import com.ozguryazilim.telve.auth.UserServiceProvider;
import com.ozguryazilim.telve.idm.entities.User;
import com.ozguryazilim.telve.idm.entities.UserAttribute;
import com.ozguryazilim.telve.idm.entities.UserGroup;
import com.ozguryazilim.telve.idm.entities.UserRole;
import com.ozguryazilim.telve.idm.user.UserGroupRepository;
import com.ozguryazilim.telve.idm.user.UserRepository;
import com.ozguryazilim.telve.idm.user.UserRoleRepository;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * Telve UserServiceProvider implementasyonu.
 * 
 * @author Hakan Uygun
 */
@Dependent
public class TelveIdmUserServiceProvider implements UserServiceProvider, Serializable{

    @Inject
    private UserRepository userRepository;
    
    @Inject
    private UserRoleRepository userRoleRepository;
    
    @Inject
    private UserGroupRepository userGroupRepository;
    
    @Override
    public List<String> getLoginNames() {
        List<String> result = new ArrayList<>();
        List<User> ls = userRepository.findAllActives();
        
        for( User u : ls ){
            result.add(u.getLoginName());
        }
        
        return result;
    }

    @Override
    public List<String> getUsersByType(String type) {
        List<String> result = new ArrayList<>();
        List<User> ls = userRepository.findByUserTypeAndActive( type, true );
        
        for( User u : ls ){
            result.add(u.getLoginName());
        }
        
        return result;
    }

    @Override
    public List<String> getUsersByGroup(String group) {
        List<String> result = new ArrayList<>();
        //FIXME: Burada sorgu düzeltilecek. 
        List<User> ls = userRepository.findByUserTypeAndActive( group, true );
        
        for( User u : ls ){
            result.add(u.getLoginName());
        }
        
        return result;
    }

    @Override
    public String getUserName(String loginName) {
        User u = userRepository.findAnyByLoginName(loginName);
        
        if( u != null ){
            return u.getFirstName() + " " + u.getLastName();
        }
        
        return null;
    }

    @Override
    public List<String> getUserRoles(String loginName) {
        User u = userRepository.findAnyByLoginName(loginName);
        if( u == null ){
            return Collections.emptyList();
        }
        
        List<UserRole> ls = userRoleRepository.findByUser(u);
        List<String> result = new ArrayList<>();
        
        for( UserRole ur : ls ){
            result.add(ur.getRole().getName());
        }
        
        return result;
    }

    @Override
    public List<String> getUserGroups(String loginName) {
        User u = userRepository.findAnyByLoginName(loginName);
        if( u == null ){
            return Collections.emptyList();
        }
        
        List<UserGroup> ls = userGroupRepository.findByUser(u);
        List<String> result = new ArrayList<>();
        
        for( UserGroup ur : ls ){
            result.add(ur.getGroup().getName());
        }
        
        return result;
    }

    @Override
    public Map<String, String> getUserAttibutes(String loginName) {
        Map<String, String> result = new HashMap<>();
        
        User u = userRepository.findAnyByLoginName(loginName);
        if( u == null ){
            return result;
        }
        
        
        for( UserAttribute ua : u.getAttributes().values()){
            result.put(ua.getKey(), ua.getValue());
        }
        
        return result;
    }
    
}
