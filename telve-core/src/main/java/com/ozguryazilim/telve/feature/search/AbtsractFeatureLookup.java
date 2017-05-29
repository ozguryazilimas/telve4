/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.feature.search;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.ozguryazilim.telve.entities.FeaturePointer;
import com.ozguryazilim.telve.feature.FeatureLink;
import com.ozguryazilim.telve.feature.FeatureRegistery;
import com.ozguryazilim.telve.lookup.Lookup;
import com.ozguryazilim.telve.lookup.LookupSelectTuple;
import com.ozguryazilim.telve.utils.ELUtils;
import com.ozguryazilim.telve.view.DialogBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 * FeatureLookup için temel sınıf.
 * 
 * Temel methıdlar bu sınıf üzerinde olacak. 
 * Asıl kullanılabilir olan FeatureLookup benzeri uygulamalar bu sınıfı miras alıp kendi özelleşmiş lookuplarını hazırlayabilirler.
 * 
 * @author Hakan Uygun
 */
public abstract class AbtsractFeatureLookup extends DialogBase implements Serializable{
    
    private String featureName;
    private String searchText;
    private String profile;
    private String listener;
    private Map<String, String> profileProperties;
    
    private List<FeatureSearchResult> results = new ArrayList<>();
    private FeatureSearchResult selected;
    
    
    @PostConstruct
    public void init() {
        
    }

    @Override
    public Class<? extends ViewConfig> getPage() {
        return this.getClass().getAnnotation(Lookup.class).dialogPage();
    }
    
    
    /**
     * Gelen profile stringine göre bir şey yapılacaksa alt sınıflar tarafından override edilir.
     */
    public void initProfile(){
        //Override edilmek üzere içi boş.
    }


    /**
     * İlgili sınıfa ait dialogu açar
     *
     * @param profile sorgu profili
     * @param listener sonuçlar nereye gidecek?
     * @param value mevcut veri. Ağaç tipi sınıflarda seçim için
     */
    public void openDialog(String profile, String listener, Object value) {

        this.profile = profile;
        this.listener = listener;
        
        
        /*
        if( value != null && value instanceof EntityBase && model instanceof LookupTreeModel){
            ((LookupTreeModel)model).setSelectedNodes( ((EntityBase)value).getId().toString());
        }
        */
        
        parseProfile();
        initProfile();


        if( autoSearch() ){
            search();
        }

        openDialog();
    }

    @Override
    protected void decorateDialog(Map<String, Object> options){
        options.put("modal", true);
        //options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 450);
    }

    /**
     * Popup açıldığında otomatik arama yapmaması için override edilmeli.
     * @return
     */
    protected boolean autoSearch(){
        return false;
    }


    /**
     * Seçim bilgisi ile birlikte dialoğu kapatır.
     *
     * Eğer bir den fazla seçilen değer varsa bunları List olarak, tek seçim
     * varsa Object olarak seçim yoksa null değer olarak döndürür.
     *
     * İlgili seçimle ilgilenen sınıf bu bilgileri şu şekilde alabilir :
     *
     * <code>
     * public void onCarChosen(SelectEvent event) {
     *       Car car = (Car) event.getObject();
     *       ......
     *   }
     * </code>
     *
     *
     */
    @Override
    public void closeDialog() {
        
        LookupSelectTuple sl = getLookupSelectTuple();

        //Eğer bir şey seçilmemiş ise sadece dialoğu kapatalım.
        if( sl == null ){
            RequestContext.getCurrentInstance().closeDialog(null);
            return;
        }

        //eğer bir event listener var ise
        if( listener.contains("event:")){
            triggerListeners(listener,sl.getValue());
        }
        //Buraya listede gelebilir
        //lookupSelectEvent.fire((E)sl.getValue());
        
        RequestContext.getCurrentInstance().closeDialog(sl);
        
    }

    /**
     * Dialog geri dönüşlerinin varsayılan dnleyicisi.
     *
     * Select tuple üzerinde bulunan bilgiyi kullanarak EL ile ilgili yere atama
     * yapar.
     *
     * @param event
     */
    public void onSelect(SelectEvent event) {
	if(event.getObject() instanceof FeatureSearchResult) {
	    String expression = "";
	    if( !Strings.isNullOrEmpty(listener) && !listener.contains(":")){
		expression = "#{" + listener + "}";
	    }
	    if( Strings.isNullOrEmpty(expression)) {
		return;
	    }

	    //EL üzerinden değeri yazacağız
	    ELUtils.setObject(expression, event.getObject());
	    return;
	}

        LookupSelectTuple sl = (LookupSelectTuple) event.getObject();
        if (sl == null) {
            return;
        }

        if( sl.getExpression().isEmpty() ) {
	    return;
	}

        if( sl.getValue() instanceof FeatureLink) {
            FeatureLink fl = (FeatureLink) sl.getValue();
            FeaturePointer fp = new FeaturePointer();
            fp.setFeature(fl.getFeatureName());
            fp.setBusinessKey(fl.getBusinessKey());
            fp.setPrimaryKey((Long) fl.getId());
            ELUtils.setObject(sl.getExpression(), fp);
            return;
        }
        
        
        //EL üzerinden değeri yazacağız
        ELUtils.setObject(sl.getExpression(), sl.getValue());

    }
       /**
     * GUI'den seçilen bilgileri kullanarak seçim paketi oluşturur.
     *
     * @return
     */
    protected LookupSelectTuple getLookupSelectTuple() {
        LookupSelectTuple sl = null;

        String expression = "";
        if( !Strings.isNullOrEmpty(listener) && !listener.contains(":")){
            expression = "#{" + listener + "}";
        }

        if( selected != null ){
            //E e = getEntity(model.getSelectedViewModel());
            sl = new LookupSelectTuple(expression, selected.getFeaturePointer());
        }

        return sl;
    }

    /**
     * Seçim sonrası listener'la mesaj gönderilir.
     * @param o
     */
    protected void triggerListeners( String event, Object o ){
        /*
        List<LookupSelectListener> ls = listeners.get(event);
        if( ls != null ){
            for( LookupSelectListener l : ls ){
                l.onSelect(o);
            }
        }
        */
    }


    /**
     * Geriye kullanılacak olan model oluşturulur.
     *
     */
    protected abstract void initModel();

    /**
     * Veri sorgulaması yapılıp model nesnesi doldurulmalıdır.
     */
    public void populateData() {
        //model.setData(getRepository().lookupQuery(model.getSearchText()));
        if( Strings.isNullOrEmpty(featureName)) return;
        
        Map<String,Object> params = new HashMap<>();
        
        decorateParams( params );
        
        results = FeatureRegistery.getSearchHandler(featureName).search(searchText, params);
    }

    /**
     * GUI'den yeni arama talebi karşılar.
     */
    public void search() {
     
        if ( !results.isEmpty() ) {
            results.clear();
        }
        populateData();
     
    }
    
        /**
     * Profile stringlerini parse edip properties olarak model sınıflarına koyar.
     *
     * Profile string formatı :
     *
     * key1:value1;key2:value2 şeklinde olur.
     *
     */
    protected void parseProfile(){
        
        if( Strings.isNullOrEmpty(profile) ){
            profileProperties = Splitter.on(';').omitEmptyStrings().trimResults().withKeyValueSeparator(':').split(profile);
        } else {
            profileProperties = new HashMap<>();
        }
        
    }


    
    /**
     * Geriye üzerinde arama yapılabilecek olan feature'ların listesini döndürür.
     * @return 
     */
    public List<String> getSearchableFeatures(){
        //FIXME: Burada aranılabilir ve Profile'da istenelinler geriye döndürülecek sadece.
        return FeatureRegistery.getSearchableFeatureNames();
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public List<FeatureSearchResult> getResults() {
        return results;
    }

    public void setResults(List<FeatureSearchResult> results) {
        this.results = results;
    }

    public FeatureSearchResult getSelected() {
        return selected;
    }

    public void setSelected(FeatureSearchResult selected) {
        this.selected = selected;
    }

    /**
     * Alt sınıflardan ek sorgu parametreleri verilebilmesi için kullanılır.
     * @param params 
     */
    protected void decorateParams(Map<String, Object> params) {
        
    }

    
    
}
