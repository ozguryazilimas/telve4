/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.entities;

import com.ozguryazilim.telve.entities.TreeNodeEntityBase;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Kullanıcı Grupları için tanım modeli
 * 
 * @author Hakan Uygun
 */
@Entity
@Table( name ="TLI_GROUP")
public class Group extends TreeNodeEntityBase<Group>{

    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "genericSeq")
    @Column(name = "ID")
    private Long id;
    
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupRole> roles = new ArrayList<>();

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<GroupRole> getRoles() {
        return roles;
    }

    public void setRoles(List<GroupRole> roles) {
        this.roles = roles;
    }
    
    
    
}
