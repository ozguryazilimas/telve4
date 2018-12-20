package com.ozguryazilim.telve.idm.user;

import com.ozguryazilim.telve.entities.ViewModel;
import com.ozguryazilim.telve.idm.entities.Group;
import java.io.Serializable;
import java.util.Objects;

/**
 * UserViewModel 
 * 
 * @author Hakan Uygun
 */
public class UserViewModel implements ViewModel, Serializable{
   
    private Long id;
    private String loginName;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String manager;
    private Boolean active;
    private String userType;
    private String info;
    private Group domainGroup;

    public UserViewModel(Long id, String loginName, String firstName, String lastName, String email, String mobile, Boolean active, String userType, String info, Long groupId, String groupName, String manager ) {
        this.id = id;
        this.loginName = loginName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
        this.active = active;
        this.userType = userType;
        this.info = info;
        this.manager = manager;
        
        this.domainGroup = new Group();
        this.domainGroup.setId(groupId);
        this.domainGroup.setName(groupName);
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Group getDomainGroup() {
        return domainGroup;
    }

    public void setDomainGroup(Group domainGroup) {
        this.domainGroup = domainGroup;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserViewModel other = (UserViewModel) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
}
