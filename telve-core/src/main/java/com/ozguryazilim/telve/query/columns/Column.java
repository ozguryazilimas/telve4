package com.ozguryazilim.telve.query.columns;

import java.io.Serializable;
import javax.persistence.metamodel.SingularAttribute;

/**
 *
 * Sorgu sonuçlarının gösterilmesi sırasında kullanılacak kolon tanımları
 *
 * @author Hakan Uygun
 * @param <E> Entity sınıf adı
 *
 */
public abstract class Column<E> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Kolon adı : bean.name
     */
    private SingularAttribute<? super E, ?> attribute;
    
    private String labelKey;
    private String permission;
    private String keyPrefix;

    public Column(SingularAttribute<? super E, ?> attribute, String labelKey) {
        this.attribute = attribute;
        this.labelKey = labelKey;
    }

    public SingularAttribute<? super E, ?> getAttribute() {
        return attribute;
    }

    public void setAttribute(SingularAttribute<? super E, ?> attribute) {
        this.attribute = attribute;
    }
    
    /**
     * Attribute adını döndürür.
     * @return 
     */
    public String getName(){
        return getAttribute().getName();
    }

    public String getLabelKey() {
        return labelKey;
    }

    public void setLabelKey(String labelKey) {
        this.labelKey = labelKey;
    }

    public abstract String getTemplate();

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((attribute == null) ? 0 : attribute.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Column other = (Column) obj;
        if (attribute == null) {
            if (other.attribute != null) {
                return false;
            }
        } else if (!attribute.equals(other.attribute)) {
            return false;
        }
        return true;
    }

}
