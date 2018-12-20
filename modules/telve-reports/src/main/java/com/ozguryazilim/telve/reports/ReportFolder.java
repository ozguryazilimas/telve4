package com.ozguryazilim.telve.reports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Rapor folder'ı ve içindeki rapor bilgisini tutmak için model sınıf.
 * 
 * Bu model sınıf UI oluşturmak için de kullanılır.
 * 
 * @author Hakan Uygun
 */
public class ReportFolder implements Serializable{
    
    private String parent;
    private String path;
    private String name;
    
    /**
     * Folder tipi. Favorite, Report, SavedReport olabilir.
     */
    private String type;
    
    /**
     * Alt folderlar
     */
    private List<ReportFolder> subfolders = new ArrayList<>();
    
    /**
     * Folder içindeki raporlar.
     */
    private List<String> reports = new ArrayList<>();

    public ReportFolder(String parent, String path, String name, String type) {
        this.parent = parent;
        this.path = path;
        this.name = name;
        this.type = type;
    }

    
    
    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ReportFolder> getSubfolders() {
        return subfolders;
    }

    public void setSubfolders(List<ReportFolder> subfolders) {
        this.subfolders = subfolders;
    }

    public List<String> getReports() {
        return reports;
    }

    public void setReports(List<String> reports) {
        this.reports = reports;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.name);
        return hash;
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
        final ReportFolder other = (ReportFolder) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ReportFolder{" + "parent=" + parent + ", path=" + path + ", name=" + name + ", type=" + type + ", subfolders=" + subfolders + ", reports=" + reports + '}';
    }
    
        
}
