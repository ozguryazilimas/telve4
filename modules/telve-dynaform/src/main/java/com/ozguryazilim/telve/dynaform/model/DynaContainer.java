package com.ozguryazilim.telve.dynaform.model;

import com.google.common.base.Strings;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DynaForm için içinde fieldların tutulduğu veri gurubu modeli.
 * 
 * Bu model UI üzerinde accordion olarak tanımlanacak
 * 
 * @author Hakan Uygun
 */
public class DynaContainer implements Serializable{
 
    private String id;
    private String label;
    private String icon;
    private String permission;
    private Boolean doubleColumn = Boolean.FALSE;
    
    private List<DynaField> fields = new ArrayList<>();

    public DynaContainer(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public DynaContainer(String id, String label, String icon) {
        this.id = id;
        this.label = label;
        this.icon = icon;
    }

    public DynaContainer(String id, String label, String icon, String permission) {
        this.id = id;
        this.label = label;
        this.icon = icon;
        this.permission = permission;
    }

    
    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Boolean getDoubleColumn() {
        return doubleColumn;
    }

    public void setDoubleColumn(Boolean doubleColumn) {
        this.doubleColumn = doubleColumn;
    }

    public List<DynaField> getFields() {
        return fields;
    }

    public void setFields(List<DynaField> fields) {
        this.fields = fields;
    }
    
    public DynaContainer addField( DynaField field ){
        if( Strings.isNullOrEmpty(field.getId())){
            field.setId(id + ":" + fields.size());
        }
        fields.add(field);
        return this;
    }
    
}
