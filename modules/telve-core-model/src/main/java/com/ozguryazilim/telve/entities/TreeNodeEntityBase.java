package com.ozguryazilim.telve.entities;

import com.ozguryazilim.telve.annotations.BizKey;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TreeNode modeller için abstract katman
 * @author Hakan Uygun
 * @param <E>
 */
@MappedSuperclass
public abstract class TreeNodeEntityBase<E extends TreeNodeModel> extends EntityBase implements TreeNodeModel<E>{

    @Column(name = "CODE", length = 255, nullable = false, unique = true )
    @NotNull @Size(max = 255)
    private String code;

    @Column(name = "NAME", length = 255, nullable = false)
    @NotNull @BizKey
    @Size(min = 1, max = 255)
    private String name;

    @Column(name = "INFO", length = 255)
    private String info;

    @Column(name = "ISACTIVE")
    private Boolean active = Boolean.TRUE;

    @Column(name = "PATH")
    private String path;
    
    @ManyToOne
    @JoinColumn(name = "PID")
    private E parent;
    
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<E> children = new ArrayList<>();
    
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
        return getParent() != null ? getParent().getId() : 0l;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
 

    
}
