package com.ozguryazilim.telve.query.columns;

import com.google.common.base.Strings;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import javax.persistence.metamodel.Attribute;
import org.apache.commons.beanutils.BeanUtils;

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
    private Attribute<? super E, ?> attribute;
    
    private String labelKey;
    private String permission;
    private String keyPrefix;
    private Boolean sortAsc = Boolean.TRUE;

    public Column(Attribute<? super E, ?> attribute, String labelKey) {
        this.attribute = attribute;
        this.labelKey = labelKey;
    }

    public Attribute<? super E, ?> getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute<? super E, ?> attribute) {
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

    /**
     * Eğer sort için kullanılacak ise asc olacak.
     * @return 
     */
    public Boolean getSortAsc() {
        return sortAsc;
    }

    public void setSortAsc(Boolean sortAsc) {
        this.sortAsc = sortAsc;
    }
    
    public void export( E e, Writer doc ) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException{
        doc.write("\"");
        String val = BeanUtils.getProperty(e, getName());
        if( !Strings.isNullOrEmpty(val)){
            doc.write( val );
        }
        doc.write("\"");
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
