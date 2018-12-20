package com.ozguryazilim.telve.reports;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.primefaces.event.RateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Rapor Listesini sunan GUI'i Control Sınıfı.
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
        reportManager.execReport(report);
    }

    /**
     * Favori rporlar için rating mapi
     *
     * @return
     */
    public Map<String, Integer> getReportRatings() {
        return reportManager.getReportRatings();
    }

    /**
     * Favori rporlar için rating mapi
     *
     * @param reportRatings
     */
    public void setReportRatings(Map<String, Integer> reportRatings) {
        reportManager.setReportRatings(reportRatings);
    }

    public void onrate(RateEvent rateEvent) {
        reportManager.checkFavReports();
        reportManager.saveFavReports();
    }

    public void oncancel() {
        reportManager.checkFavReports();
        reportManager.saveFavReports();
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
