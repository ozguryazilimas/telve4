/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Parametre tipi entityler için taban sınıf.
 * 
 * @author Hakan Uygun
 */
@MappedSuperclass
public abstract class ParamEntityBase extends EntityBase{

    @Column(name = "CODE", length = 30, nullable = false, unique = true )
    @NotNull @Size( min = 1, max = 30) 
    private String code;
    
    @Column(name = "NAME", length= 100, nullable = false )
    @NotNull @Size(min = 1, max = 100) 
    private String name; 

    @Column(name="ISACTIVE")
    private Boolean active = Boolean.TRUE;

    @Column(name = "INFO", length= 255)
    private String info; 

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    
    public String getCaption(){
        return "[" + getCode() + "] " + getName();
    }
}
