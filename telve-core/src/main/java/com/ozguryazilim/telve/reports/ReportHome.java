/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.reports;

import com.google.common.base.CaseFormat;
import com.google.common.base.Splitter;
import com.ozguryazilim.mutfak.kahve.Kahve;
import com.ozguryazilim.mutfak.kahve.KahveEntry;
import com.ozguryazilim.mutfak.kahve.annotations.UserAware;
import com.ozguryazilim.telve.config.TelveConfigResolver;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Any;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.picketlink.Identity;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.RateEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
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

    private static final String NAME_SPLITTER = " » ";
    private static final String FOLDER_TYPE = "folder";

    /**
     * Kullanıcının rapor listesini tutar.
     */
    private final Set<String> reports = new HashSet<>();

    /**
     * Kullanıcının favori raporları
     */
    private final List<String> favReports = new ArrayList<>();

    /**
     * Accordion sunumu için rapor gruplarını tutar.
     */
    private final Map< String, List<String>> reportGroups = new HashMap<>();

    /**
     * Favori raporlar için rapor rating bilgisi.
     */
    private Map<String, Integer> reportRatings = new HashMap<>();

    /**
     * Rapor ağacı için root node
     */
    private final TreeNode rootNode = new DefaultTreeNode("Root");
    private TreeNode favNode;
    private TreeNode selectedData;
    
    private byte[] reportData;
    private Boolean showReport = false;
    private StreamedContent media;

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
        initTree();
        initFavReports();
        initReportGroups();
    }

    /**
     * Kullanıcının hakkı olan roparlardan ağaç oluşturur.
     */
    protected void initTree() {

        favNode = new DefaultTreeNode(FOLDER_TYPE, "favorites", rootNode);
        selectedData = favNode;
        
        for (Entry<String, Report> e : ReportRegistery.getReports().entrySet()) {
            //Kullanıcının yetkisi var mı? Eğer permission tanımlanmamışsa Sınıf ismini kullan.
            String p = e.getValue().permission();
            if (p.isEmpty()) {
                p = e.getKey();
            }

            if (identity.hasPermission(p, "select")) {
                reports.add(e.getKey());
                //Rating tablosu için 0 ile init ediyoruz.
                //initFavorits sırasında içi doldurulacak.
                reportRatings.put(e.getKey(), 0);

                TreeNode node = rootNode;
                for (String s : Splitter.on('/').omitEmptyStrings().trimResults().split(e.getValue().path())) {
                    TreeNode fn = findTreeNode(node, s);
                    if (fn == null) {
                        fn = new DefaultTreeNode(FOLDER_TYPE, s, node);
                    }
                    node = fn;
                }

                TreeNode rep = new DefaultTreeNode(e.getValue().type().name(), e.getKey(), node);

            }
        }
    }

    /**
     * Verilen node içinde istenilen veriye sahip node'u arar.
     *
     * Bulamazsa geriye null döner
     *
     * @param parent
     * @param data
     * @return
     */
    private TreeNode findTreeNode(TreeNode parent, String data) {
        if (parent != null) {
            for (TreeNode n : parent.getChildren()) {
                if (n.getData().equals(data)) {
                    return n;
                }
            }
        }
        return null;
    }

    /**
     * Option sistemi üzerinden kullanıcının favori raporlarının listesini
     * oluşturur.
     */
    protected void initFavReports() {
        favReports.clear();

        KahveEntry o = kahve.get("reports.fav.count");
        if (o == null) {
            //Kullanıcın favori raporu yok.
            return;
        }

        Integer fvc = o.getAsInteger();

        for (int i = 0; i < fvc; i++) {
            //İsmi kondu
            o = kahve.get("reports.fav." + i + ".name");
            String r = o.getAsString();
            favReports.add(r);
            //Rate'i de kondu.
            o = kahve.get("reports.fav." + i + ".rate");
            reportRatings.put(r, o.getAsInteger());
        }

        for (String s : favReports) {
            //FIXME: Rapor tipini nasıl öğreneceğiz?
            TreeNode rep = new DefaultTreeNode("JasperReport", s, favNode);
        }
    }

    /**
     * Favori rapor listesini Option sistemine kaydeder.
     */
    public void saveFavReports() {

        int i = 0;
        for (String r : favReports) {
            kahve.put("reports.fav." + i + ".name", r);
            kahve.put("reports.fav." + i + ".rate", reportRatings.get(r));
            i++;
        }

        kahve.put( "reports.fav.count", favReports.size());
    }

    /**
     * İsmi verilen rapor controllerı üzerinden çalıştırılır.
     *
     * @param report
     */
    public void execReport(String report) {
        if (isReport(report)) {
            //Sınıf ismini EL ismi haline getiriyoruz.
            String name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, report);
            ReportController rc = BeanProvider.getContextualReference(name, true, ReportController.class);
            if (rc != null) {
                rc.execute();
            }
        }
    }

    public List<String> getFavReports() {
        return favReports;
    }

    public TreeNode getRootNode() {
        return rootNode;
    }

    public TreeNode getSelectedData() {
        return selectedData;
    }

    public void setSelectedData(TreeNode selectedData) {
        this.selectedData = selectedData;
    }

    public void onNodeSelect(NodeSelectEvent event) {
        String s = event.getTreeNode().toString();
        if (isReport(s)) {
            //execReport(s);
        } else {
            this.showReport = false;
            initReportGroups();
        }
    }

    /**
     * Folderlar ile raporları ayırmak için kullanılır.
     *
     * eğer verilen string rapor listesinde ise true döner.
     *
     * @param name
     * @return
     */
    public boolean isReport(String name) {
        return reports.contains(name);
    }

    private void initReportGroups() {
        reportGroups.clear();
        if (selectedData == null) {
            //Sadece Favori raporlar olacak demek. Ona göre implemente edelim
        } else {
            String parentNamePath = resolveParentNamePath(selectedData);

            buildChilds(parentNamePath, selectedData);

        }
    }

    private void buildChilds(String parentNamePath, TreeNode node) {
        if (FOLDER_TYPE.equals(node.getType())) {
            //Eğer Node hala folder ise namepath'e ekleyip childları çağırıyoruz.
            String pp = parentNamePath + NAME_SPLITTER + messages.get("report.folder." + node.getData().toString());
            for (TreeNode c : node.getChildren()) {
                buildChilds(pp, c);
            }
        } else {
            List<String> rls = reportGroups.get(parentNamePath);
            if (rls == null) {
                rls = new ArrayList<>();
                reportGroups.put(parentNamePath, rls);
            }
            rls.add((String) node.getData());
        }
    }

    private String resolveParentNamePath(TreeNode node) {
        StringBuilder sb = new StringBuilder();
        TreeNode parent = node.getParent();

        while (parent != null) {
            //RootNode'u listeye eklemiyoruz
            if (parent != rootNode) {
                sb.insert(0, NAME_SPLITTER + messages.get("report.folder." + parent.getData().toString()));
            }
            parent = parent.getParent();
        }

        //sb.append(NAME_SPLITTER).append(node.getData());
        return sb.toString();
    }

    public Map<String, List<String>> getReportGroups() {
        return reportGroups;
    }

    /**
     * Verilen rapor ismi için icon yolunu döndürür.
     *
     * @param report
     * @return
     */
    public String getIconPath(String report) {
        Report r = ReportRegistery.getReports().get(report);
        if (r == null) {
            return "/img/ribbon/large/report.png";
        }
        return "/img/ribbon/large/" + r.icon();
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
        }

    }

    public void onrate(RateEvent rateEvent) {
        LOG.info("OnRate");
        LOG.info("Ratings : {}", reportRatings);
        checkFavReports();
        saveFavReports();
    }

    public void oncancel() {
        LOG.info("OnCancel : {}");
        LOG.info("Ratings : {}", reportRatings);
        checkFavReports();
        saveFavReports();
    }

    public byte[] getReportData() {
        return reportData;
    }

    public void setReportData(byte[] reportData) {
        this.reportData = reportData;
        media = new DefaultStreamedContent(new ByteArrayInputStream(this.reportData), "application/pdf", "Rapor");
    }

    public Boolean getShowReport() {
        return false; //showReport; p:media ile embeded için 
    }

    public void setShowReport(Boolean showReport) {
        this.showReport = showReport;
    }

    /**
     * GUI'de rapor gösterilmesi için setler.
     */
    public void onReportRun(SelectEvent event){
        LOG.info(event.getObject().toString());
        this.showReport = true;
    }

    public StreamedContent getMedia() {
        return media;
    }
    
    public StreamedContent getStream(){
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
            // So, browser is requesting the media. Return a real StreamedContent with the media bytes.
            return media;
        }
    }

    
}
