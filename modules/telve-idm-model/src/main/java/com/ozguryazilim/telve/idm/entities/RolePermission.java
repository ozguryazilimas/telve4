package com.ozguryazilim.telve.idm.entities;

import com.ozguryazilim.telve.entities.EntityBase;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Role'ün sahip olduğu yetki tanımları.
 * 
 * @author Hakan Uygun
 */
@Entity
@Table( name ="TLI_ROLE_PERM")
public class RolePermission extends EntityBase{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "genericSeq")
    @Column(name = "ID")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "ROLE_ID", foreignKey = @ForeignKey(name = "FK_RP_ROLEID"))
    private Role role;
    
    @Column( name = "PERMISSION")
    private String permission;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
    
    
    
}
