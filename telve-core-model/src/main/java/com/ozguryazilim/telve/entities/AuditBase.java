/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.deltaspike.data.api.audit.CreatedOn;
import org.apache.deltaspike.data.api.audit.ModifiedBy;
import org.apache.deltaspike.data.api.audit.ModifiedOn;

/**
 * Değişiklik ve kimin yaptığı bilgilerini tutmak için üst sınıf.
 * 
 * DeltaSpike Data Audit yapısı kullaılır.
 * 
 * @author Hakan Uygun
 */
@MappedSuperclass
public abstract class AuditBase extends EntityBase{
    
    @Column(name="CREATE_DATE")
    @Temporal(value=TemporalType.TIMESTAMP)
    @CreatedOn    
    private Date createDate;
    
    @Column(name="UPDATE_DATE")
    @Temporal(value=TemporalType.TIMESTAMP)
    @ModifiedOn(onCreate = true)
    private Date updateDate;
    
    //CreatedBy yokmuş :(
    //@Column(name="CREATE_USER")
    //@ModifiedBy    
    //private String createUser;
    
    @Column(name="UPDATE_USER")
    @ModifiedBy
    private String updateUser;

    /**
     * Kaydın oluşturulduğu tarih saati döndürür.
     * @return 
     */
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * Kaydın son değiştirildiği tarih saati döndürür.
     * @return 
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * Kaydı son değiştiren kullanıcıyı döndürür.
     * @return 
     */
    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    
}
