/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.reports;

import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import org.apache.deltaspike.core.api.provider.BeanProvider;

/**
 * Sistemde tanımlı Raporlara erişim ve çalıştırma için API sağlar.
 * 
 * Raporlara erişim için folder yapısı ve API sağlar.
 * Raporları çalışırmak için API sağlar. Bunlar Registery ve Hanlder'lar ile salanır.
 * 
 * @author Hakan Uygun
 */
@Named
@Dependent
public class ReportManager implements Serializable{
    
    /**
     * Folder'lara hızlı bir şekilde erişim için fullpath key tutuyoruz.
     * 
     * keza folder parent'da da parent pah'in tamamı duruyor.
     */
    private Map<String,ReportFolder> folders = new HashMap<>();

    /**
     * İsmi verilen raporu bileşen kütüphanesinde bulup çalıştırır.
     * 
     * @param report 
     */
    public void execReport(String report) {
        //Sınıf ismini EL ismi haline getiriyoruz.
        String name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, report);
        ReportController rc = BeanProvider.getContextualReference(name, true, ReportController.class);
        if (rc != null) {
            rc.execute();
        }
    }
    
    /**
     * sistemde tanımlı bütün klasörleri döndürür.
     * @return 
     */
    public List<ReportFolder> getFolderList(){
        return new ArrayList(folders.values());
    }
    
    /**
     * Verilen Path için parent folder listesini döndürür.
     * 
     * @param path
     * @return 
     */
    public List<ReportFolder> getParentFolderList( String path ){
        List<ReportFolder> result = new ArrayList<>();
        String s = getParentPath(path);
        while( !"#".equals(s) ){
            ReportFolder rf = folders.get(s);
            if( rf != null ){
                result.add(0, rf);
            }
            s = getParentPath(s);
        }
        
        return result;
    }
    
    public ReportFolder findOrCreateFolder( String path ){
        return findOrCreateFolder(path, "report");
    }
    
    /**
     * Verilen pathe sahip folder'ı yoksa oluşturarak bulup döndürür.
     * @param path
     * @param type
     * @return 
     */
    public ReportFolder findOrCreateFolder( String path, String type ){
        ReportFolder rf = folders.get(path);
        if( rf != null ){
            return rf;
        }
        
        String pp = getParentPath(path);
        String fn = getFolderName(path);
        
        ReportFolder rfn = new ReportFolder( pp, path, fn, type);
        
        if( !"#".equals(pp)){
            ReportFolder rfp = findOrCreateFolder( pp, type );
            rfp.getSubfolders().add(rfn);
        }
        
        folders.put(path, rfn);
        
        return rfn;
    }
    
    /**
     * Adı verilen folder'a adı verilen raporu ekler.
     * 
     * Eğer folder yok ise önce oluşturur.
     * 
     * @param path
     * @param report 
     */
    public void addReport( String path, String report ){
        ReportFolder rf = findOrCreateFolder(path, "report");
        rf.getReports().add(report);
    }
    
    /**
     * Verilen path'e sahip flderı döndürür.
     * 
     * Yoksa nul döner.
     * 
     * @param path
     * @return 
     */
    public ReportFolder getFolder( String path ){
        return folders.get(path);
    }
    
    /**
     * Verilen folder'daki raporları döndürür.
     * @param path
     * @return 
     */
    public List<String> getReports( String path ){
        ReportFolder rf = getFolder(path);
        if( rf == null ) return Collections.EMPTY_LIST;
        return getFolder(path).getReports();
    }
    
    /**
     * / ile ayrılmış string içerisinde üst kısmı döndürür.
     * 
     * Örnek /abc/def/zxc için geriye /abc/def döner.
     * 
     * TODO: Kod açıklara karşı düzenlenmeli. ya iinde / yoksa ya sadece en sonda varsa ya sadece / varsa gibi.
     * 
     * @param path
     * @return 
     */
    public String getParentPath( String path ){
        List<String> ls = Splitter.on('/').omitEmptyStrings().trimResults().splitToList(path);
        //Root'a vardıysak geriye # döndürelim...
        if( ls.isEmpty() ) return "#";
        if( ls.size() == 1 ) return "#";
        return "/" + Joiner.on('/').join(ls.subList(0, ls.size() - 1));
    }
    
    /**
     * Son /'dan sonrasını folder ismi olarak döndürür.
     * getParentPath için yazılanlar burası için de geçerli.
     * @param path
     * @return 
     */
    public String getFolderName( String path ){
        List<String> ls = Splitter.on('/').omitEmptyStrings().trimResults().splitToList(path);
        if( ls.isEmpty() ) return "#";
        return ls.get(ls.size()-1);
    }
    
    
    
    /**
     * Verilen folder içerisinde ismi verilen folder'ı arar.
     * 
     * Bulamaz ise geriye null döner.
     * 
     * @param folder
     * @param folderName
     * @return 
     */
    public ReportFolder findSubfolder( ReportFolder folder, String folderName ){
        for( ReportFolder rf : folder.getSubfolders()){
            if( rf.getName().equals(folderName)){
                return rf;
            }
        }
        return null;
    }
    
}
