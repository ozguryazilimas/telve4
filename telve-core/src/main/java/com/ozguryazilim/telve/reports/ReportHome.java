/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.reports;

import com.google.common.base.CaseFormat;
import com.ozguryazilim.mutfak.kahve.Kahve;
import com.ozguryazilim.mutfak.kahve.annotations.UserAware;
import com.ozguryazilim.telve.config.TelveConfigResolver;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Any;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.picketlink.Identity;
import org.primefaces.event.RateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Rapor Listesini sunan GUI'i Control Sınıfı.
 *
 * TODO: FavReportlar rate'e göre sıralanmalı
 * TODO: PDF.js ile inline PDF gösterelim. 
 * 
 * @author Hakan Uygun
 */
@Named
@WindowScoped //WindowScope'da p:madia çalışmıyor
public class ReportHome implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(ReportHome.class);

    @Inject
    private UserAwareReportManager  reportManager;

    private ReportFolder selectedFolder;
    
    /**
     * Favori raporlar için rapor rating bilgisi.
     */
    private Map<String, Integer> reportRatings = new HashMap<>();

    @Inject
    @Any
    private Identity identity;

    @Inject
    private TelveConfigResolver configResolver;

    @Inject @UserAware
    private Kahve kahve;

    @Inject
    @Named("messages")
    private transient Map<String, String> messages;

    @PostConstruct
    public void init() {
        selectFolder("/favorites");
    }

    /**
     * İsmi verilen rapor controllerı üzerinden çalıştırılır.
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
     * Favori rporlar için rating mapi
     *
     * @return
     */
    public Map<String, Integer> getReportRatings() {
        return reportRatings;
    }

    /**
     * Favori rporlar için rating mapi
     *
     * @param reportRatings
     */
    public void setReportRatings(Map<String, Integer> reportRatings) {
        this.reportRatings = reportRatings;
    }

    /**
     * Rating değerlerine göre favori raporları düzenler.
     */
    protected void checkFavReports() {
        /*
        for (Entry<String, Integer> e : reportRatings.entrySet()) {
            if (e.getValue() > 0) {
                if (!favReports.contains(e.getKey())) {
                    favReports.add(e.getKey());
                    new DefaultTreeNode("JasperReport", e.getKey(), favNode);
                }
            } else {
                if (favReports.contains(e.getKey())) {
                    favReports.remove(e.getKey());
                    for (TreeNode n : favNode.getChildren()) {
                        if (e.getKey().equals(n.getData())) {
                            favNode.getChildren().remove(n);
                            return;
                        }
                    }
                }
            }
        }*/

    }

    public void onrate(RateEvent rateEvent) {
        LOG.info("OnRate");
        LOG.info("Ratings : {}", reportRatings);
        checkFavReports();
        //saveFavReports();
    }

    public void oncancel() {
        LOG.info("OnCancel : {}");
        LOG.info("Ratings : {}", reportRatings);
        checkFavReports();
        //saveFavReports();
    }

    /**
     * Geriye tüm folderların listesini döndürür.
     * @return 
     */
    public List<ReportFolder> getFolderList(){
        return reportManager.getFolderList();
    }
    
    /**
     * Geriye tüm folderların listesini döndürür.
     * @return 
     */
    public List<ReportFolder> getParentFolderList( String path ){
        return reportManager.getParentFolderList(path);
    }

    /**
     * Seçilmiş olan aktif rapor klasörünü döndürür
     * @return 
     */
    public ReportFolder getSelectedFolder() {
        return selectedFolder;
    }

    /**
     * Seçilmiş olan rapor klasörünü setler
     * @param selectedFolder 
     */
    public void setSelectedFolder(ReportFolder selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    /**
     * RemoteCommand ile gelen nodeId bilgisini kullanarak seçilmi raporu setler
     */
    public void selectNode(){
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = params.get("nodeId");
        selectedFolder = reportManager.getFolder(id);
    }
    
    /**
     * Verilen isimli / idli folder'ı seçilmiş olarak setler.
     * @param id 
     */
    public void selectFolder( String id ){
        selectedFolder = reportManager.getFolder(id);
    }
}
