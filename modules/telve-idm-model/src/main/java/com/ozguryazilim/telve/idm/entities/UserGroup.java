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
 * Bir user'ın üyesi olduğu gruplar
 * 
 * @author Hakan Uygun
 */
@Entity
@Table( name ="TLI_USER_GROUP")
public class UserGroup extends EntityBase{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "genericSeq")
    @Column(name = "ID")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_UG_USERID"))
    private User user;
    /**
     * Üyesi olunan grup
     */
    @ManyToOne
    @JoinColumn(name = "GROUP_ID", foreignKey = @ForeignKey(name = "FK_UG_GROUPID"))
    private Group group;

    @Column(name = "ISAUTOCREATED")
    private Boolean autoCreated = Boolean.FALSE;

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

    public Boolean getAutoCreated() {
        return autoCreated;
    }

    public void setAutoCreated(Boolean autoCreated) {
        this.autoCreated = autoCreated;
    }
}
