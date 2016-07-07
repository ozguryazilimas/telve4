/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.user;

import com.ozguryazilim.telve.entities.ViewModel;
import com.ozguryazilim.telve.idm.entities.Group;
import com.ozguryazilim.telve.idm.entities.User;
import java.io.Serializable;

/**
 *
 * @author oyas
 */
public class UserGroupViewModel implements ViewModel, Serializable{

    private Long id;
    private User user;
    private Group group;

    public UserGroupViewModel() {
    }

    public UserGroupViewModel(Long id, User user, Group group) {
        this.id = id;
        this.user = user;
        this.group = group;
    }

    public UserGroupViewModel(Long id, Long userId, String loginName, Long groupId, String groupName ) {
        this.id = id;
        this.user = new User();
        this.user.setId(userId);
        this.user.setLoginName(loginName);
        this.group = new Group();
        this.group.setId(groupId);
        this.group.setName(groupName);
    }

    @Override
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
    
    
}
