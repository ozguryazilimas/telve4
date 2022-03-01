package com.ozguryazilim.telve.dashboard;

import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.ozguryazilim.mutfak.kahve.Kahve;
import com.ozguryazilim.mutfak.kahve.KahveEntry;
import com.ozguryazilim.mutfak.kahve.annotations.UserAware;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Any;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.shiro.subject.Subject;
import org.primefaces.component.dashboard.Dashboard;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kullanıcı dashboard'unu yönetir.
 *
 * PrimeFaces dashbord model ve render ile oynamak gerekecek. Şu anda mevcut
 * dashlet yapısını bu duruma göre düzenlemek lazım. Şimdilik sadece Home
 * sayfası normal gelecek şekilde kapatıldı.
 *
 * @author Hakan Uygun
 */
@Named
@SessionScoped
public class DashboardManager implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(DashboardManager.class);

    private static final String TAB_KEY = "dashboard.tab.";
    
    private Integer board;
    private Integer currentBoard = 0;
    private List<DashboardDataModel> dashboards = new ArrayList<>();

    /**
     * Editor işlemleri sırasında kullanılan veri modeli
     */
    private DashboardDataModel selectedBoard;

    /**
     * Editör üzerinden gelecek olan kolon/dashlet bilgisi.
     *
     * kolonlar ; ile dasletler , ile ayrılmış olarak gelecekler.
     *
     * dashlet,dashlet;dashlet,dashlet;dashlet,dashlet
     */
    private String layoutMap;

    /**
     * Aktif dashbord PF Modeli
     */
    private DashboardModel model = null;
    /**
     * Aktif dashbord dashletleri
     */
    private final List<String> dashlets = new ArrayList<>();

    @Inject
    private DashletRegistery registery;

    @Inject @UserAware
    private Kahve kahve;

    @Inject
    @Any
    private Subject identity;

    @PostConstruct
    public void init() {
        //Kullanıcıya ait dash boardlar yüklenecek.
        loadData();
        if (dashboards.isEmpty()) {
            //kullanıcı sistemden ilk kez istemiş dolayısı ile ilk dashbordu hazırlıyoruz.
            initSystemDashbord();
        }
    }

    protected void initSystemDashbord() {

        DashboardDataModel model = new DashboardDataModel();
        model.setName(ConfigResolver.getProjectStageAwarePropertyValue("dashboard.name", "Ana Sayfa"));
        int tc = Integer.parseInt(ConfigResolver.getProjectStageAwarePropertyValue("dashboard.layout", "0"));
        model.setLayout(tc);

        //Kolon 1
        String col = ConfigResolver.getProjectStageAwarePropertyValue("dashboard.column1", "firstRunDashlet");

        for (String d : Splitter.on(',').omitEmptyStrings().trimResults().split(col)) {
            model.getColumn1().add(d);
        }

        //Kolon 2
        col = ConfigResolver.getProjectStageAwarePropertyValue("dashboard.column2", "");

        for (String d : Splitter.on(',').omitEmptyStrings().trimResults().split(col)) {
            model.getColumn2().add(d);
        }

        //Kolon 3
        col = ConfigResolver.getProjectStageAwarePropertyValue("dashboard.column3", "");

        for (String d : Splitter.on(',').omitEmptyStrings().trimResults().split(col)) {
            model.getColumn3().add(d);
        }

        dashboards.add(model);
        saveData();

    }

    /**
     * Seçili olan dashboard için PF modeli döndürür.
     *
     * @return
     */
    public DashboardModel getDashboardModel() {
        checkDashboard();
        LOG.debug("Current : {}, Board : {}", currentBoard, board);
        return model;
    }

    /**
     * Aktif dashboard için PF modelini üretir.
     */
    protected void initDashboardModel() {
        model = new DefaultDashboardModel();
        
        //Tanımlardan gelen bilgiler Avail Dashlets listesinde var mı diye kontrol etmek için listeyi alalım.
        List<String> ads = getAvailDashlets();

        //Tanımlı hiç dashborad yok :( Normal şartlarda olmamasını bekleriz.
        if (dashboards.isEmpty()) {
            return;
        }

        DashboardDataModel data = dashboards.get(currentBoard);

        DefaultDashboardColumn column1 = new DefaultDashboardColumn();
        for (String w : data.getColumn1()) {
            if( ads.contains(w)){
                column1.addWidget(w);
            }
        }

        DefaultDashboardColumn column2 = new DefaultDashboardColumn();
        for (String w : data.getColumn2()) {
            if( ads.contains(w)){
                column2.addWidget(w);
            }
        }

        DefaultDashboardColumn column3 = new DefaultDashboardColumn();
        for (String w : data.getColumn3()) {
            if( ads.contains(w)){
                column3.addWidget(w);
            }
        }

        //Kolon widget atamaları
        dashlets.clear();
        switch (data.getLayout()) {
            case DashboardDataModel.LAYOUT_ONE:
                dashlets.addAll(column1.getWidgets());
                model.addColumn(column1);
                break;

            case DashboardDataModel.LAYOUT_ONE_TWO:

            case DashboardDataModel.LAYOUT_TWO_ONE:

            case DashboardDataModel.LAYOUT_TWO:
                dashlets.addAll(column1.getWidgets());
                dashlets.addAll(column2.getWidgets());

                model.addColumn(column1);
                model.addColumn(column2);
                break;

            case DashboardDataModel.LAYOUT_TREE:

            case DashboardDataModel.LAYOUT_TOP_ONETWO:

            case DashboardDataModel.LAYOUT_TOP_TWO_ONE:

            case DashboardDataModel.LAYOUT_TOP_TWO:

                dashlets.addAll(column1.getWidgets());
                dashlets.addAll(column2.getWidgets());
                dashlets.addAll(column3.getWidgets());

                model.addColumn(column1);
                model.addColumn(column2);
                model.addColumn(column3);
                break;

        }

        //Stil atamaları
        switch (data.getLayout()) {
            case DashboardDataModel.LAYOUT_ONE:
                column1.setStyleClass("col-xs-12");
                break;

            case DashboardDataModel.LAYOUT_ONE_TWO:
                column1.setStyleClass("col-md-5 col-xs-12");
                column2.setStyleClass("col-md-7 col-xs-12");
                break;

            case DashboardDataModel.LAYOUT_TWO_ONE:
                column1.setStyleClass("col-md-7 col-xs-12");
                column2.setStyleClass("col-md-5 col-xs-12");
                break;

            case DashboardDataModel.LAYOUT_TWO:
                column1.setStyleClass("col-md-6 col-xs-12");
                column2.setStyleClass("col-md-6 col-xs-12");
                break;

            case DashboardDataModel.LAYOUT_TREE:
                column1.setStyleClass("col-md-4 col-xs-12");
                column2.setStyleClass("col-md-4 col-xs-12");
                column3.setStyleClass("col-md-4 col-xs-12");
                break;

            case DashboardDataModel.LAYOUT_TOP_ONETWO:
                column1.setStyleClass("col-xs-12");
                column2.setStyleClass("col-md-5 col-xs-12");
                column3.setStyleClass("col-md-7 col-xs-12");
                break;

            case DashboardDataModel.LAYOUT_TOP_TWO_ONE:
                column1.setStyleClass("col-xs-12");
                column2.setStyleClass("col-md-7 col-xs-12");
                column3.setStyleClass("col-md-5 col-xs-12");
                break;

            case DashboardDataModel.LAYOUT_TOP_TWO:
                column1.setStyleClass("col-xs-12");
                column2.setStyleClass("col-md-6 col-xs-12");
                column3.setStyleClass("col-md-6 col-xs-12");
                break;
        }
    }

    /**
     * Aktif dashbord üzerinde bulunan dashletleri döndürür.
     *
     * @return
     */
    public List<String> getDashlets() {
        checkDashboard();
        return dashlets;
    }

    /**
     * İstenilen dashboard ile mevcut dashboardun aynı olup olmadığını kontrol
     * eder.
     *
     * Eğer aynı değilerse yeniden hazırlanmasını sağlar
     */
    private void checkDashboard() {
        if (getBoard() != null) {
            if (currentBoard.equals(getBoard())) {
                //mevcut board ile isteilen aynı ise ve henüz oluşturulmamışsa oluştur aksi halde dokunma
                if (model == null || dashlets.isEmpty()) {
                    initDashboardModel();
                }
            } else {
                //aynı değillerse oluştur
                //GUI'den saçma bir talep gelebilir.
                if (getBoard() >= 0 && getBoard() <= dashboards.size()) {
                    currentBoard = getBoard();
                    initDashboardModel();
                }
            }
        }

        //Eğer hala makul bir şey yoksa oluştur
        if (model == null || dashlets.isEmpty()) {
            currentBoard = 0;
            initDashboardModel();
        }
    }

    public List<DashboardDataModel> getDashboards() {
        return dashboards;
    }

    public void setDashboards(List<DashboardDataModel> dashboards) {
        this.dashboards = dashboards;
    }

    //-------------------------------
    // Veritabanı İşlemleri
    //
    public void loadData() {

        KahveEntry o = kahve.get("dashboard.tab.count");
        if (o == null) {
            //Kullanıcı için tanımlı dashbord yok bir tane sistem defaultu ile oluşturalım
            initSystemDashbord();
            return;
        }

        int tc = o.getAsInteger();
        for (int i = 0; i < tc; i++) {
            DashboardDataModel t = new DashboardDataModel();

            o = kahve.get(TAB_KEY + i + ".name");
            t.setName(o.getAsString());

            o = kahve.get(TAB_KEY + i + ".layout");
            t.setLayout(o.getAsInteger());

            o = kahve.get(TAB_KEY + i + ".col1");

            String col = o.getAsString();
            setColumnDashets(t.getColumn1(), col);

            o = kahve.get(TAB_KEY + i + ".col2");
            col = o.getAsString();
            setColumnDashets(t.getColumn2(), col);

            o = kahve.get(TAB_KEY + i + ".col3");
            col = o.getAsString();
            setColumnDashets(t.getColumn3(), col);

            dashboards.add(t);
        }
    }

    /**
     * Dashbordlar üzerindeki tüm veriyi veri tabanına kaydeder
     */
    public void saveData() {

        kahve.put("dashboard.tab.count", new KahveEntry(dashboards.size()));

        for (int i = 0; i < dashboards.size(); i++) {

            DashboardDataModel data = dashboards.get(i);

            //Tab ismi setleniyor
            kahve.put(TAB_KEY + i + ".name", data.getName());

            //Tab layoutIndex setleniyor
            kahve.put(TAB_KEY + i + ".layout", data.getLayout());
            

            //Tab colonları setleniyor
            String s = Joiner.on(",").join(data.getColumn1());
            kahve.put(TAB_KEY + i + ".col1", s);

            s = Joiner.on(",").join(data.getColumn2());
            kahve.put(TAB_KEY + i + ".col2", s);
            

            s = Joiner.on(",").join(data.getColumn3());
            kahve.put(TAB_KEY + i + ".col3", s);

        }
    }

    /**
     * virgüllerle ayrılmış dashletleri verilen colon listesine ekler.
     *
     * @param column
     * @param dashlets
     */
    private void setColumnDashets(List<String> column, String dashlets) {
        column.clear();
        if (!Strings.isNullOrEmpty(dashlets)) {
            for (String s : Splitter.on(",").omitEmptyStrings().trimResults().split(dashlets)) {
                column.add(s);
            }
        }
    }

    //------------------------------------------
    // GUI Actions
    //
    public DashboardDataModel getSelectedBoard() {
        return selectedBoard;
    }

    public void setSelectedBoard(DashboardDataModel selectedBoard) {
        this.selectedBoard = selectedBoard;
    }

    public String getLayoutMap() {
        return layoutMap;
    }

    /**
     * Kolon Dashlet map'i setler.
     *
     * Kolonlar ; dashletler ise , ile ayrılmış gelmeli
     *
     * Örnek : dashlet1, dashlet2 ; dashlet3, dashlet4
     *
     * @param layoutMap
     */
    public void setLayoutMap(String layoutMap) {
        if (StringUtils.isBlank(layoutMap)) {
           return;
        }
        this.layoutMap = layoutMap;

        //set edildiğinde değerler parse edilip yerlerine yerleştirilecek...
        selectedBoard.getColumn1().clear();
        selectedBoard.getColumn2().clear();
        selectedBoard.getColumn3().clear();

        int i = 0;
        for (String col : Splitter.on(';').omitEmptyStrings().trimResults().split(layoutMap)) {
            for (String d : Splitter.on(',').omitEmptyStrings().trimResults().split(col)) {
                d = d.replace("editForm:", "");
                d = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, d);
                switch (i) {
                    case 0:
                        selectedBoard.getColumn1().add(d);
                        break;
                    case 1:
                        selectedBoard.getColumn2().add(d);
                        break;
                    case 2:
                        selectedBoard.getColumn3().add(d);
                        break;
                }
            }
            i++;
        }
        //LOG.info("LayoutMap #0, col1 : #1, col2 : #2, col3: #3", layoutMap, selectedBoard.getColumn1(), selectedBoard.getColumn2(), selectedBoard.getColumn3());
    }

    /**
     * Yeni veri girişi için model sınıfı hazırlar
     */
    public void newDashboard() {
        selectedBoard = new DashboardDataModel();
    }

    /**
     * Edit edilmek üzere model sınıfı hazırlar
     *
     * @param board
     */
    public void editDashboard(DashboardDataModel board) {
        selectedBoard = board;
    }

    /**
     * Üzerinde değişiklik yapılan model sınıfı saklar
     */
    public void saveDashboard() {
        if (!dashboards.contains(selectedBoard)) {
            dashboards.add(selectedBoard);
        }

        saveData();
        //Modelde değişiklik oldu dashboard istendiğinde yeniden oluşturulsun
        model = null;
        dashlets.clear();
        selectedBoard = null;
    }

    /**
     * Seçili modeli boşaltır.
     */
    public void cancelDashboard() {
        //Yapacak bişi yok
        selectedBoard = null;
    }

    /**
     * Verilen modeli siler
     *
     * @param board
     */
    public void deleteDashboard(DashboardDataModel board) {
        dashboards.remove(board);
        saveData();
    }

    /**
     * Layout'a göre editor column 1 css style classını döndürür
     *
     * @return
     */
    public String getEditorColumn1Style() {

        if (selectedBoard == null) {
            return "hidden";
        }

        switch (selectedBoard.getLayout()) {
            case DashboardDataModel.LAYOUT_ONE:
                return "col-xs-12";
            case DashboardDataModel.LAYOUT_ONE_TWO:
                return "col-xs-4";
            case DashboardDataModel.LAYOUT_TWO_ONE:
                return "col-xs-8";
            case DashboardDataModel.LAYOUT_TWO:
                return "col-xs-6";
            case DashboardDataModel.LAYOUT_TREE:
                return "col-xs-4";
            case DashboardDataModel.LAYOUT_TOP_ONETWO:  ;
            case DashboardDataModel.LAYOUT_TOP_TWO_ONE:  ;
            case DashboardDataModel.LAYOUT_TOP_TWO:
                return "col-xs-12";
        }
        return "hidden";
    }

    /**
     * Layout'a göre editor column 2 css style classını döndürür
     *
     * @return
     */
    public String getEditorColumn2Style() {
        if (selectedBoard == null) {
            return "hidden";
        }

        switch (selectedBoard.getLayout()) {
            case DashboardDataModel.LAYOUT_ONE:
                return "hidden";
            case DashboardDataModel.LAYOUT_ONE_TWO:
                return "col-xs-8";
            case DashboardDataModel.LAYOUT_TWO_ONE:
                return "col-xs-4";
            case DashboardDataModel.LAYOUT_TWO:
                return "col-xs-6";
            case DashboardDataModel.LAYOUT_TREE:
                return "col-xs-4";
            case DashboardDataModel.LAYOUT_TOP_ONETWO:
                return "col-xs-4";
            case DashboardDataModel.LAYOUT_TOP_TWO_ONE:
                return "col-xs-8";
            case DashboardDataModel.LAYOUT_TOP_TWO:
                return "col-xs-6";
        }
        return "hidden";
    }

    /**
     * Layout'a göre editor column 3 css style classını döndürür
     *
     * @return
     */
    public String getEditorColumn3Style() {
        if (selectedBoard == null) {
            return "hidden";
        }

        switch (selectedBoard.getLayout()) {
            case DashboardDataModel.LAYOUT_ONE:
                return "hidden";
            case DashboardDataModel.LAYOUT_ONE_TWO:
                return "hidden";
            case DashboardDataModel.LAYOUT_TWO_ONE:
                return "hidden";
            case DashboardDataModel.LAYOUT_TWO:
                return "hidden";
            case DashboardDataModel.LAYOUT_TREE:
                return "col-xs-4";
            case DashboardDataModel.LAYOUT_TOP_ONETWO:
                return "col-xs-8";
            case DashboardDataModel.LAYOUT_TOP_TWO_ONE:
                return "col-xs-4";
            case DashboardDataModel.LAYOUT_TOP_TWO:
                return "col-xs-6";
        }
        return "hidden";
    }

    /**
     * Mevcut duruma ve kullanıcıya göre erişilebilir dashlet listesini
     * döndürür.
     *
     * @return
     */
    public List<String> getAvailDashlets() {
        List<String> ls = new ArrayList<>();

        for (Map.Entry<String, Dashlet> e : registery.getDashlets().entrySet()) {
            String p = e.getValue().permission();
            if (p.isEmpty()) {
                p = e.getKey();
            }
            //Kullanıcının bu dashlet'i görme yetkisi var mı?
            if ( "public".equals(p) || identity.isPermitted(p  + ":select")) {
                ls.add(e.getKey());
            }
        }

        return ls;
    }

    /**
     * Mevcut duruma göre palete yerleştirilebilecek dashlet listesini döndürür.
     *
     * @return
     */
    public List<String> getPaletDashlets() {
        List<String> ls = new ArrayList<>();
        if (selectedBoard == null) {
            return ls;
        }

        List<String> dl = getAvailDashlets();

        LOG.debug("Avail Dashlets : {}", dl);
        //LOG.info("Current Layout #0, col1 : #1, col2 : #2, col3: #3", selectedBoard.getName(), selectedBoard.getColumn1(), selectedBoard.getColumn2(), selectedBoard.getColumn3());
        checkColumns();

        //log.info("After Check Current Layout #0, col1 : #1, col2 : #2, col3: #3", selectedBoard.getName(), selectedBoard.getColumn1(), selectedBoard.getColumn2(), selectedBoard.getColumn3());
        //Hali hazırda bir kolon da olmayan bileşenler.
        for (String d : dl) {
            if (!selectedBoard.getColumn1().contains(d)
                    && !selectedBoard.getColumn2().contains(d)
                    && !selectedBoard.getColumn3().contains(d)) {
                ls.add(d);
            }
        }
        LOG.debug("Palet Dashlets : {}", ls);
        return ls;
    }

    public void setPaletDashlets(List<String> ls) {
        //İçerik anlamlı değil GUI için var
    }

    /**
     * Seçili dashbord için layouta göre kalkan kolon varsa dashletlerini
     * kaldırır.
     */
    private void checkColumns() {
        if (selectedBoard == null) {
            return;
        }
        switch (selectedBoard.getLayout()) {
            case DashboardDataModel.LAYOUT_ONE:
                selectedBoard.getColumn2().clear();
            case DashboardDataModel.LAYOUT_ONE_TWO:
            case DashboardDataModel.LAYOUT_TWO_ONE:  ;
            case DashboardDataModel.LAYOUT_TWO:
                selectedBoard.getColumn3().clear();
        }
    }

    /**
     * PF Dashboard'an gelen değişiklik bilgileri handle eder.
     *
     * @param event
     */
    public void handleReorder(DashboardReorderEvent event) {
        //Kolon Sıralam değişikliğini PF kendi yapıyor. Biz data ile ilgileneceğiz.

        LOG.info("Reordered: " + event.getWidgetId());
        LOG.info("Item index: " + event.getItemIndex() + ", Column index: " + event.getColumnIndex() + ", Sender index: " + event.getSenderColumnIndex());

        DashboardDataModel data = dashboards.get(currentBoard);
        switch (event.getSenderColumnIndex()) {
            case 0:
                data.getColumn1().remove(event.getWidgetId());
                break;
            case 1:
                data.getColumn2().remove(event.getWidgetId());
                break;
            case 2:
                data.getColumn3().remove(event.getWidgetId());
                break;
        }

        switch (event.getColumnIndex()) {
            case 0:
                data.getColumn1().add(event.getItemIndex(), event.getWidgetId());
                break;
            case 1:
                data.getColumn2().add(event.getItemIndex(), event.getWidgetId());
                break;
            case 2:
                data.getColumn3().add(event.getItemIndex(), event.getWidgetId());
                break;
        }

        saveData();
    }

    /**
     * Dashletlerin kapanma kontrolü
     *
     * @param event
     */
    public void handleClose(CloseEvent event) {

        //Önce GUI Modelden çıkartıyoruz
        for (DashboardColumn col : model.getColumns()) {
            col.removeWidget(event.getComponent().getId());
        }

        //Sonra data'dan çıkartıp kaydediyoruz
        DashboardDataModel data = dashboards.get(currentBoard);
        data.getColumn1().remove(event.getComponent().getId());
        data.getColumn2().remove(event.getComponent().getId());
        data.getColumn3().remove(event.getComponent().getId());

        saveData();

        LOG.info("Closed Dashlet: " + event.getComponent().getId());
    }

    public Integer getBoard() {
        //FXIME: View param olarak çalışmadı. Nedenine bir bakalım.
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String r = request.getParameter("board");
        if( !Strings.isNullOrEmpty(r) ){
            return Integer.parseInt(r);
        }
        return null;
        
        //return board;
    }

    public void setBoard(Integer board) {
        this.board = board;
    }

    public boolean isDefaultDashboardName(DashboardDataModel dashboard) {
        List<String> defaultDashboardNames = Arrays.asList(ConfigResolver.getProjectStageAwarePropertyValue("default.dashboard.name", "Ana Sayfa").split(","));
        return defaultDashboardNames.stream().anyMatch(dashboard.getName()::equals);
    }
}
