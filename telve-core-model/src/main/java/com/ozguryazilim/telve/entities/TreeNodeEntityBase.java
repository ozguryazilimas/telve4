/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 * TreeNode modeller için abstract katman
 * @author Hakan Uygun
 * @param <E>
 */
@MappedSuperclass
public abstract class TreeNodeEntityBase<E extends TreeNodeModel> extends EntityBase implements TreeNodeModel<E>{

    @Column(name = "CODE")
    @NotNull
    private String code;
    
    @Column(name = "PATH")
    private String path;
    
    @ManyToOne
    @JoinColumn(name = "PID")
    private E parent;
    
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<E> children = new ArrayList<E>();
    
    /**
     * Node code alanını döndürür.
     * @return 
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * Geriye parent node id'sini döndürür.
     * @return 
     */
    @Override
    public Long getParentId() {
        return getParent().getId();
    }

    /**
     * Geriye caption string döndürür.
     * Default code döner. Bunu override edin.
     * @return 
     */
    @Override
    public String getCaption() {
        return code;
    }

    /**
     * Alt node listesini döndürür.
     * @return 
     */
    @Override
    public List<E> getChildren() {
        return children;
    }

    /**
     * Parent node'u döndürür.
     * @return 
     */
    @Override
    public E getParent() {
        return parent;
    }

    /**
     * Parent node'u setler.
     * @param parent 
     */
    @Override
    public  void setParent(E parent) {
        this.parent = parent;
    }

    /**
     * Geriye NodePath'i döndürür.
     * 
     * /1/21/125 şeklinde ID listesi
     * 
     * @return 
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * Node Path'ini setler.
     * 
     * gelmesi gereken değer /1/32/123 şeklinde id listesi
     * 
     * @param path 
     */
    @Override
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Kod alanını setler.
     * @param code 
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Alt Node listesini setler.
     * @param children 
     */
    public void setChildren(List<E> children) {
        this.children = children;
    }
 
    
}
