/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.picketlink.idm.jpa.annotations.OwnerReference;
import org.picketlink.idm.jpa.annotations.PermissionOperation;
import org.picketlink.idm.jpa.annotations.PermissionResourceClass;
import org.picketlink.idm.jpa.annotations.PermissionResourceIdentifier;
import org.picketlink.idm.jpa.annotations.entity.PermissionManaged;

/**
 * FIXME: Bu sınıf picketlink modelleri ile aynı yere gidecek...
 * @author haky
 */
@Entity
@PermissionManaged
@Table(name = "PL_RESOURCE_PERMISSION")
public class ResourcePermission implements Serializable{

    private static final long serialVersionUID = -7409821749592191950L;
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "genericSeq")
    @Column(name="ID")
    private Long id;
    @OwnerReference
    @Column(name="ASSIGNEE")
    private String assignee;
    @PermissionResourceClass
    @Column(name="RESOURCE_CLASS")
    private String resourceClass;
    @PermissionResourceIdentifier
    @Column(name="RESOURCE_IDENTIFIER")
    private String resourceIdentifier;
    @PermissionOperation
    @Column(name="OPERATION")
    private String operation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getResourceClass() {
        return resourceClass;
    }

    public void setResourceClass(String resourceClass) {
        this.resourceClass = resourceClass;
    }

    public String getResourceIdentifier() {
        return resourceIdentifier;
    }

    public void setResourceIdentifier(String resourceIdentifier) {
        this.resourceIdentifier = resourceIdentifier;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
