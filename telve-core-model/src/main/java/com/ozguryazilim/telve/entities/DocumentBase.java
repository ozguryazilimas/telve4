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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Fiş / Belge türü kayıtlar için temel sınıf.
 * 
 * @author Hakan Uygun
 */
@MappedSuperclass
public abstract class DocumentBase extends AuditBase{
    @Column(name="SERIAL", length=30, nullable=false, unique=true)
    @NotNull
    @Size(max=30, min=1)
    private String serial;
    
    @Column(name="CODE", length=30)
    @Size(max=30)
    private String code;
    
    @Column(name="INFO")
    private String info;
    
    
    @Column(name="REFERENCE", length=30)
    @Size(max=30)
    private String reference;
    
    @Column(name="ISACTIVE")
    private Boolean active = Boolean.TRUE;
    
    //Belgenin Düzenlenme Tarihi
    @Column(name="TXNDATE")
    @Temporal(value = TemporalType.DATE)
    private Date date;
    
    //Belgenin Düzenlenme Saati
    @Column(name="TXNTIME")
    @Temporal(value = TemporalType.TIME)
    private Date time;
    
    /**
     * Geriye txn'lerde ayrıştırmaya yarayacak docType anahtar stringgi döndür. 
     * Bu Stringin karşılığı dil dosyalarında "documentType." ile başlar şekilde verilmelidir.
     * 
     * FIXME: Ayrıca gidilecek olan view içinde bir registery makul olabilir
     * 
     * @return 
     */
    public abstract String getDocumentType();

    /**
     * Belge için Fiş Numarası döndürür.
     * Sistem için BizKey'i oluşturur.
     * @return 
     */
    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    /**
     * Belge için özel kod alanını döndürür.
     * @return 
     */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Belge açıklamasını döndürür.
     * @return 
     */
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * Belge için karşılık referansı döndürür.
     * 
     * Örneğin basılı bir evrak üzerinde bulunan numara gibi.
     * 
     * @return 
     */
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * Belgenin geçerli olup olmadığı
     * @return 
     */
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * Belge tarihini döndürür.
     * 
     * TODO: DateTime'ı toplayarak işleyen bir method lazım.
     * 
     * @return 
     */
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Belge saatini döndürür.
     * @return 
     */
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
    
    
    
}
