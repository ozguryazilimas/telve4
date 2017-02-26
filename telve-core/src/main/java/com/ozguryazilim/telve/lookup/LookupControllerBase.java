/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.lookup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.Entity;

import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.entities.EntityBase;
import com.ozguryazilim.telve.entities.ViewModel;
import com.ozguryazilim.telve.utils.ELUtils;

/**
 * Lookup Controller Sınıfları için taban
 *
 * @author Hakan Uygun
 * @param <E> İşlem yapılacak entity sınıfı
 * @param <R> Gösterimde kullanılacak ViewModel sınıfı
 */
public abstract class LookupControllerBase<E extends EntityBase, R extends ViewModel> implements Serializable{

    private static final Logger LOG = LoggerFactory.getLogger(LookupControllerBase.class);

    private LookupModel<R, ?> model;

    private Map<String,List<LookupSelectListener>> listeners = new HashMap<>();

    @Inject
    private ViewConfigResolver viewConfigResolver;

    @Inject @LookupSelect
    private Event<E> lookupSelectEvent;

    @PostConstruct
    public void init() {
        initModel();
    }

    /**
     * Geriye açılacak olan popup için view adı döndürür.
     *
     * Bu view dialogBase sınıfından türetilmiş olmalıdır.
     *
     *
     * @return
     */
    public String getDialogName() {
        String viewId = getDialogPageViewId();
        return viewId.substring(0, viewId.indexOf(".xhtml"));
    }

    /**
     * Dialog için sınıf annotationı üzerinden aldığı Page ID'sini döndürür.
     *
     * @return
     */
    public String getDialogPageViewId() {
        return viewConfigResolver.getViewConfigDescriptor(getDialogPage()).getViewId();
    }

    /**
     * Sınıf işaretçisinden @Lookup page bilgisini alır
     *
     * @return
     */
    public Class<? extends ViewConfig> getDialogPage() {
        return this.getClass().getAnnotation(Lookup.class).dialogPage();
    }

    /**
     * Geriye kullanılacak olan Repository'i döndürür.
     *
     * @return
     */
    protected abstract RepositoryBase<E, R> getRepository();

    /**
     * Overlay GUI'ler için init fonksiyonu.
     *
     * Boş bir profille sadece listener atar.
     *
     * @param listener
     */
    public void initOverlay(String listener) {
        initOverlay("", listener);
    }

    /**
     * Overlay GUI'ler için init fonksiyonu.
     *
     * profille beraber listener atar.
     *
     * @param profile
     * @param listener
     */
    public void initOverlay(String profile, String listener) {
        model.setMultiSelect(false);
        model.setProfile(profile);
        model.setListener(listener);

        parseProfile();
        initProfile();

        search();
    }

    public void closeOverlay() {

        LookupSelectTuple sl = getLookupSelectTuple();

        //Eğer listener bir expression ise
        if( !sl.getExpression().isEmpty() ){
            ELUtils.setObject(sl.getExpression(), sl.getValue());
        }

        //Eğer listener bir event ise
        if( model.getListener().startsWith("event:")){
            triggerListeners(model.getListener(),sl.getValue());
        }

        //Bundan emin değilim...
        lookupSelectEvent.fire((E)sl.getValue());
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
     * @param multiSelect
     * @param listener
     */
    public void openDialog(Boolean multiSelect, String listener) {
        openDialog(multiSelect, false, "", listener);
    }

    /**
     * İlgili sınıfa ait dialogu açar
     *
     * @param multiSelect çoklu seçime izin var mı?
     * @param leafSelect ağaçlar için leaf node seçim izni
     * @param profile sorgu profili
     * @param listener sonuçlar nereye gidecek?
     */
    public void openDialog(Boolean multiSelect, Boolean leafSelect, String profile, String listener) {
        openDialog( multiSelect, leafSelect, true, profile, listener );
    }


    /**
     * İlgili sınıfa ait dialogu açar
     *
     * @param multiSelect çoklu seçime izin var mı?
     * @param leafSelect ağaçlar için leaf node seçim izni
     * @param profile sorgu profili
     * @param listener sonuçlar nereye gidecek?
     */
    public void openDialog(Boolean multiSelect, Boolean leafSelect, Boolean fullPath, String profile, String listener) {
        openDialog( multiSelect, leafSelect, fullPath, profile, listener, null );
    }

    /**
     * İlgili sınıfa ait dialogu açar
     *
     * @param multiSelect çoklu seçime izin var mı?
     * @param leafSelect ağaçlar için leaf node seçim izni
     * @param fullPath
     * @param profile sorgu profili
     * @param listener sonuçlar nereye gidecek?
     * @param value mevcut veri. Ağaç tipi sınıflarda seçim için
     */
    public void openDialog(Boolean multiSelect, Boolean leafSelect, Boolean fullPath, String profile, String listener, Object value) {
        model.setMultiSelect(multiSelect);
        model.setLeafSelect(leafSelect);
        model.setProfile(profile);
        model.setListener(listener);

        model.setFullPathResult(fullPath);

        model.clearSelections();
        model.setSearchText("");
        if( value != null && value instanceof EntityBase && model instanceof LookupTreeModel){
            ((LookupTreeModel)model).setSelectedNodes( ((EntityBase)value).getId().toString());
        }

        parseProfile();
        initProfile();

        Map<String, Object> options = new HashMap<>();

        decorateDialog(options);

        if( autoSearch() ){
            search();
        }

        RequestContext.getCurrentInstance().openDialog(getDialogName(), options, null);
    }

    /**
     * Açılacak olan diolog özellikleri setlenir.
     *
     * Alt sınıflar isterse bu methodu override ederk dialoğ özellikleirni değiştirebilirler.
     *
     * @param options
     */
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
        return true;
    }

    /**
     * Ağaç için leafSelect modlu openDialog sürümü.
     *
     * @param multiSelect
     * @param leafSelect
     * @param listener
     */
    public void openDialog(Boolean multiSelect, Boolean leafSelect, String listener) {
        openDialog(multiSelect, leafSelect, "", listener);
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
    public void closeDialog() {

        LookupSelectTuple sl = getLookupSelectTuple();

        //Eğer bir şey seçilmemiş ise sadece dialoğu kapatalım.
        if( sl == null ){
            RequestContext.getCurrentInstance().closeDialog(null);
            return;
        }

        //eğer bir event listener var ise
        if( model.getListener().contains("event:")){
            triggerListeners(model.getListener(),sl.getValue());
        }
        //Buraya listede gelebilir
        //lookupSelectEvent.fire((E)sl.getValue());

        RequestContext.getCurrentInstance().closeDialog(sl);

    }

    /**
     * Dialogu hiç bir şey seçmeden kapatır.
     */
    public void cancelDialog() {
        RequestContext.getCurrentInstance().closeDialog(null);
    }

    /**
     * Seçimlerde kullanılan View Modelden Entity Model'e çevrim yapar.
     *
     * @param viewModel
     * @return
     */
    protected E getEntity(R viewModel) {
        E result;

        //Eğer üzerinde Entity işareti varsa view model olarak da entity kullanılmıştır.
        //Hiç arama yapmadan aynısını geri döndürüyoruz.
        if (viewModel.getClass().isAnnotationPresent(Entity.class)) {
            result = (E) viewModel;
        } else {
            //Demek ki farklı bir model kullanılıyor. Hadi çevirelim.
            result = getRepository().findBy(viewModel.getId());
        }

        return result;
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
        model.setData(getRepository().lookupQuery(model.getSearchText()));
    }

    /**
     * GUI'den yeni arama talebi karşılar.
     */
    public void search() {
        LOG.info("Searh Called");
        if (!model.isDataEmpty() ) {
            model.clearData();
        }
        populateData();
    }

    /**
     * SelectOneMenu, SuggestBox gibi bileşenlerde kullanmak için.
     *
     * Verilen text'i kullanarak aramayapıp sonucunu döndürür.
     *
     * @param text
     * @return
     */
    public List<E> suggest(String text) {

        FacesContext context = FacesContext.getCurrentInstance();
        String leafSelect = (String) UIComponent.getCurrentComponent(context).getAttributes().get("leafSelect");
        String multiSelect = (String) UIComponent.getCurrentComponent(context).getAttributes().get("multiSelect");
        String lookupProfile = (String) UIComponent.getCurrentComponent(context).getAttributes().get("lookupProfile");
        String lookupListener = (String) UIComponent.getCurrentComponent(context).getAttributes().get("lookupListener");

        model.setLeafSelect("true".equals(leafSelect));
        model.setMultiSelect("true".equals(multiSelect));
        model.setListener(lookupListener);

        model.setProfile(lookupProfile);
        parseProfile();
        initProfile();

        List<E> ls = populateSuggestData(text);
        LOG.info("Suggest List : {}", ls);
        return ls;
    }

    /**
     * Alt sınıflardan override edilip sorgu mantığı değiştirilebilmesi için
     * @param text
     * @return
     */
    protected List<E> populateSuggestData(String text){
        return model.getLeafSelect() ? getRepository().suggestionLeaf(text) : getRepository().suggestion(text);
    }

    /**
     * Dialoglarda kullanılacak model sınıfı dondürür.
     *
     * @return
     */
    public LookupModel<R, ?> getModel() {
        return model;
    }

    /**
     * Dialoglarda kullanılan model sınıfı setler.
     *
     * @param model
     */
    public void setModel(LookupModel<R, ?> model) {
        this.model = model;
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
	if(event.getObject() instanceof EntityBase) {
	    String expression = "";
	    if( !Strings.isNullOrEmpty(model.getListener()) && !model.getListener().contains(":")){
		expression = "#{" + model.getListener() + "}";
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
        if( !Strings.isNullOrEmpty(model.getListener()) && !model.getListener().contains(":")){
            expression = "#{" + model.getListener() + "}";
        }

        if (model.getMultiSelect()) {

            List<E> ls = new ArrayList<>();

            for (R o : model.getSelectedViewModels()) {
                E e = getEntity(o);
                if (e != null) {
                    ls.add(e);
                }
            }

            if( !ls.isEmpty() ){
                //Listeyi el ile value olarak gömmek için...
                sl = new LookupSelectTuple(expression, ls);
            }

        } else {
            if( model.getSelectedViewModel() != null ){
                E e = getEntity(model.getSelectedViewModel());
                sl = new LookupSelectTuple(expression, e);
            }
        }

        return sl;
    }

    /**
     * Geriye listelerde gösterim için hangi attributun kullanılacağı bilgisini döndürür.
     * @return
     */
    public abstract String getCaptionFieldName();

    /**
     * Lookup için yeni bir select listener ekler.
     * @param l
     */
    public void registerListener( String event, LookupSelectListener l ){
        List<LookupSelectListener> ls = listeners.get(event);
        if( ls == null ){
            ls = new ArrayList<>();
            listeners.put(event, ls);
        }
        ls.add(l);
    }

    /**
     * Lookupdan daha önce kaydedilmiş bir listener'ı çıkarır.
     * @param l
     */
    public void unregisterListener( String event, LookupSelectListener l ){
        List<LookupSelectListener> ls = listeners.get(event);
        if( ls != null ){
            ls.remove(l);
        }
    }

    /**
     * Seçim sonrası listener'la mesaj gönderilir.
     * @param o
     */
    protected void triggerListeners( String event, Object o ){
        List<LookupSelectListener> ls = listeners.get(event);
        if( ls != null ){
            for( LookupSelectListener l : ls ){
                l.onSelect(o);
            }
        }
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
        if( model.getProfile() != null ){
            Map<String,String> prop = Splitter.on(';').omitEmptyStrings().trimResults().withKeyValueSeparator(':').split(model.getProfile());
            model.setProfileProperties(prop);
        } else {
            model.setProfileProperties(new HashMap<String, String>());
        }
    }


    public E findBy( Long pk ){
        return getRepository().findBy(pk);
    }

    public void onRowSelect(SelectEvent event) {
        //Sadece UI tetiklesin diye var.
    }
}
