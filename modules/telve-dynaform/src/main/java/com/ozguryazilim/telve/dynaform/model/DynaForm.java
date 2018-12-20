package com.ozguryazilim.telve.dynaform.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DynaForm temel model sınıfı.
 * 
 * Form için gerekli tüm bilgileri üzerinde bulundurur.
 * 
 * @author Hakan Uygun
 */
public class DynaForm implements Serializable {
    
    private String id;
    private String domain;
    private String label;
    private String permission;
    private String metaData;
    private String version = "0";
    private String prefix = "FRM";
    private Boolean doubleColumn = Boolean.FALSE;
    
    private List<DynaContainer> containers = new ArrayList<>();
    
    private List<DynaCalcField> calcFields = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public List<DynaContainer> getContainers() {
        return containers;
    }

    public void setContainers(List<DynaContainer> containers) {
        this.containers = containers;
    }

    public List<DynaCalcField> getCalcFields() {
        return calcFields;
    }

    public void setCalcFields(List<DynaCalcField> calcFields) {
        this.calcFields = calcFields;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Boolean getDoubleColumn() {
        return doubleColumn;
    }

    public void setDoubleColumn(Boolean doubleColumn) {
        this.doubleColumn = doubleColumn;
    }

    public DynaForm addContainer( DynaContainer cont ){
        containers.add(cont);
        return this;
    }

    public DynaForm addContainer( String id, String label, DynaField ... fields ){
        
        DynaContainer c = new DynaContainer(id, label);
        
        for( DynaField f : fields ){
            c.addField(f);
        }
        
        containers.add(c);
        
        return this;
    }
}
