/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Feature'lara bağlı görece küçük imaj saklamak için veri modeli.
 * 
 * 
 * TODO: boyut bilgileri model üzerinde olmalı mı? length zaten byte array nedeni ile otomatik gelecek. Ama heigth-witdh bilgisi sorgu için kullanılabilir belki?
 * 
 * @author Hakan Uygun
 */
@Entity
@Table(name = "TLV_IMAGE")
public class SimpleImage extends EntityBase{
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "genericSeq")
    @Column(name = "ID")
    private Long id;

    /**
     * Image kimin eklediği
     */
    @Column( name = "OWNER")
    private String owner;
    
    /**
     * Resim için uygulama tarafından atanan anahtar değer.
     * 
     */
    @Column( name="IMAGE_KEY")
    private String imageKey;
    
    /**
     * Resim için farklılaşma anahtarı.
     * 
     * original, thumbnail, 120x120
     * 
     */
    @Column( name="ASPECT")
    private String aspect;
    
    /**
     * Her resim için PK yerine de geçebilecek unique id.
     * 
     * UUID ile oluşturulacak.
     * 
     */
    @Column( name="IMAGE_ID")
    private String imageId;
    
    /**
     * Oluşturulma tarihi.
     */
    @Column(name="CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    
    /**
     * Ek açıklama. İstenir ise.
     */
    @Column(name = "INFO")
    private String info;
    
    
    /**
     * MimeType. image/* türü bişi olmalı.
     * 
     */
    @Column(name = "MIME")
    private String mimeType;
    
    /**
     * Image içeriği
     */
    @Lob @Column( name = "CONTENT")
    private byte[] content;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
    
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
    
    
}
