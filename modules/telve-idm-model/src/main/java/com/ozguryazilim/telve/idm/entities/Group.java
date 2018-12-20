package com.ozguryazilim.telve.idm.entities;

import com.ozguryazilim.telve.entities.TreeNodeEntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    @Column(name = "AUTO_CREATED")
    private Boolean autoCreated = Boolean.FALSE;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAutoCreated() {
        return autoCreated;
    }

    public void setAutoCreated(Boolean autoCreated) {
        this.autoCreated = autoCreated;
    }
}
