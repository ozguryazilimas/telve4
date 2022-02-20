package com.ozguryazilim.telve.idm.user;

import com.ozguryazilim.telve.entities.ViewModel;
import com.ozguryazilim.telve.idm.entities.Role;
import com.ozguryazilim.telve.idm.entities.User;
import java.io.Serializable;

/**
 *
 * @author oyas
 */
public class UserRoleViewModel implements ViewModel, Serializable{
    
    private Long id;
    private User user;
    private Role role;
    private Boolean autoCreated = Boolean.FALSE;

    public UserRoleViewModel(Long id, User user, Role role) {
        this.id = id;
        this.user = user;
        this.role = role;
    }

    public UserRoleViewModel(Long id, Long userId, String loginName, Long roleId, String roleName ) {
        this.id = id;
        this.user = new User();
        this.user.setId(userId);
        this.user.setLoginName(loginName);
        this.role = new Role();
        this.role.setId(roleId);
        this.role.setName(roleName);
    }

    public UserRoleViewModel(Long id, Long userId, String loginName, Long roleId, String roleName, Boolean autoCreated) {
        this.id = id;
        this.user = new User();
        this.user.setId(userId);
        this.user.setLoginName(loginName);
        this.role = new Role();
        this.role.setId(roleId);
        this.role.setName(roleName);
        this.autoCreated = autoCreated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getAutoCreated() {
        return autoCreated;
    }

    public void setAutoCreated(Boolean autoCreated) {
        this.autoCreated = autoCreated;
    }
}
