package com.ozguryazilim.telve.idm;

import com.ozguryazilim.telve.auth.UserRoleResolver;
import com.ozguryazilim.telve.idm.entities.User;
import com.ozguryazilim.telve.idm.entities.UserGroup;
import com.ozguryazilim.telve.idm.entities.UserRole;
import com.ozguryazilim.telve.idm.user.UserGroupRepository;
import com.ozguryazilim.telve.idm.user.UserRepository;
import com.ozguryazilim.telve.idm.user.UserRoleRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

/**
 * Verilen kullanıcı için role resolver implementasyonu.
 * 
 * @author Hakan Uygun
 */
public class TelveIdmUserRoleResolver implements UserRoleResolver{

    @Inject
    private UserRepository userRepository;
    
    @Inject
    private UserRoleRepository userRoleRepository;
    
    @Inject
    private UserGroupRepository userGroupRepository;
    
    @Override
    public List<String> getRoles(String loginName) {
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

    /**
     * Geriye farklı bilgileri paketleyerek döndürür : 
     * 
     * U: UserID
     * L: Login Name
     * N: UserName ( FirstName + LastName )
     * T: UserType
     * R: Role
     * G: Group
     * PG: GroupPath
     * 
     * 
     * @param loginName
     * @return 
     */
    @Override
    public List<String> getUnifiedRoles(String loginName) {
        User u = userRepository.findAnyByLoginName(loginName);
        if( u == null ){
            return Collections.emptyList();
        }
        
        List<String> result = new ArrayList<>();
        //Kullannıcı bilgileri
        result.add( "U:" + u.getId());
        result.add( "L:" + u.getLoginName());
        result.add( "N:" + u.getFirstName() + " " + u.getLastName());
        result.add( "T:" + u.getUserType());
        
        //Kullanıcı Rolleri
        List<UserRole> url = userRoleRepository.findByUser(u);
        for( UserRole ur : url ){
            result.add( "R:" + ur.getRole().getName());
        }
        
        //Kullanıcı Grupları + Grup Pathler
        List<UserGroup> ugl = userGroupRepository.findByUser(u);
        for( UserGroup ug : ugl ){
            result.add( "G:" + ug.getGroup().getName());
            result.add( "PG:" + ug.getGroup().getPath());
        }
        
        return result;
    }
    
}
