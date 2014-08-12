/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.lookup;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.entities.EntityBase;
import com.ozguryazilim.telve.entities.ViewModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.Entity;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * LookUp Dialogları için taban sınıf.
 *
 * Bu sınıf inherit alınarak gerekli lookup dialogları hazırlanır.
 *
 *
 * Farklı profillere sahip dialog yapılacaksa "profile" bilgisi kullanılmalıdır.
 * Örneğin ContactLookup için "ALL", "CUSTOMER", "CONTACT" gibi değerler
 * olabilir ve bunlar filtreleme sırasında kullanılmalıdır.
 *
 *
 * @author Hakan Uygun
 * @param <T> Kullanılacak olan Entity Sınıfı
 * @param <F> Seçimlerde kullanılacak olan Model sınıfı
 */
public abstract class LookupDialogBase<T extends EntityBase, F extends ViewModel> implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(LookupDialogBase.class);

    
    
    
    private LookupDataModel<F> model;
    private F selectedData;
    private F[] selectedDatas;
    private Boolean multiSelect = false;
    private String profile;
    private String searchText;
    private Class<T> entityClass;
    private String listener;

    
    @Inject
    private ViewConfigResolver viewConfigResolver;
    
    
    @PostConstruct
    public void init(){
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
    public String getDialogName(){
        String viewId = getDialogPageViewId();
        return viewId.substring( 0, viewId.indexOf(".xhtml")) ;
    }
    
    public String getDialogPageViewId(){
        return viewConfigResolver.getViewConfigDescriptor(getDialogPage()).getViewId();
    }
    
    public Class<? extends ViewConfig> getDialogPage(){
        return this.getClass().getAnnotation(Lookup.class).dialogPage();
    }

    protected abstract RepositoryBase<T,F> getRepository();
    
    public void initOverlay( String listener ){
        initOverlay("", listener);
    }
    
    public void initOverlay( String profile, String listener ){
        setMultiSelect(false);
        setProfile(profile);
        setListener(listener);
        search();
    }
    
    public void closeOverlay(){
        //FIXME: Burada ki kodu DRY'a tabi tut.
        SelectTuple sl;
        
        if (isMultiSelect()) {

            List<T> ls = new ArrayList<T>();

            for (F o : selectedDatas) {
                T e = getEntity(o);
                if (e != null) {
                    ls.add(e);
                }
            }
            
            //eğer .'dan sonra bir değer yoksa birinci değeri paslıyoruz
            sl = new SelectTuple("", "", ls);
            
        } else {
            T e = getEntity(selectedData);
            sl = new SelectTuple("", "", e);
        }
        //Value değerini EL olarak da saklıyoruz.
        sl.setExpression("#{" + listener + "}");
        
        setObject(sl.getExpression(), sl.getValue());
    }
    
    /**
     * İlgili sınıfa ait dialogu açar
     *
     * @param multiSelect
     */
    public void openDialog(Boolean multiSelect, String listener) {
        openDialog(multiSelect, "", listener);
    }

    /**
     * İlgili sınıfa ait dialogu açar
     *
     * @param multiSelect
     * @param profile
     */
    public void openDialog(Boolean multiSelect, String profile, String listener) {
        setMultiSelect(multiSelect);
        setProfile(profile);
        setListener(listener);
        
        Map<String,Object> options = new HashMap<String, Object>();  
        options.put("modal", true);  
        //options.put("draggable", false);  
        options.put("resizable", false);  
        options.put("contentHeight", 450); 
        
        RequestContext.getCurrentInstance().openDialog(getDialogName(), options, null);
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
        //TODO burayı biraz daha adam etmek lazım.

        SelectTuple sl;

        //Önce event value ayrıştırması yapıyoruz...
        // 0. değer value 1. değer event
        String[] srcs = listener.split(";");

        //Value ataması yapıyoruz...
        String[] s = srcs[0].split("\\.");
        if (isMultiSelect()) {

            List<T> ls = new ArrayList<T>();

            for (F o : selectedDatas) {
                T e = getEntity(o);
                if (e != null) {
                    ls.add(e);
                }
            }
            
            //eğer .'dan sonra bir değer yoksa birinci değeri paslıyoruz
            sl = new SelectTuple(s[0], s.length >  1 ? s[1] : s[0], ls);
            
        } else {
            T e = getEntity(selectedData);
            sl = new SelectTuple(s[0], s.length >  1 ? s[1] : s[0], e);
        }
        //Value değerini EL olarak da saklıyoruz.
        sl.setExpression("#{" + srcs[0] + "}");
        

        //Eğer event varsa...
        if (srcs.length > 1) {
            //Bean.method formatında beklyoruz.
            s = srcs[1].split("\\.");
            sl.setEventBean(s[0]);
            sl.setEventMethod(s[1]);
        }

        RequestContext.getCurrentInstance().closeDialog(sl);
    }

    
    /**
     * Seçimlerde kullanılan Filtre Modelden Entity Model'e çevrim yapar.
     *
     * @param filterModel
     * @return
     */
    private T getEntity(F filterModel) {
        T result;
        
        //Eğer üzerinde Entity işareti varsa hiç arama yapmadan aynısını geri döndürüyoruz.
        if( filterModel.getClass().isAnnotationPresent(Entity.class) ){
            result = (T) filterModel;
        } else {
            //Demek ki farklı bir model kullanılıyor. Hadi çevirelim.
            result = getRepository().findBy(filterModel.getId());
        }

        return result;
    }
    
    /**
     * Dialogu hiç bir şey seçmeden kapatır.
     */
    public void cancelDialog() {
        RequestContext.getCurrentInstance().closeDialog(null);
    }

    /**
     * Geriye seçilen veriyi döndürür
     *
     * @return
     */
    public F getSelectedData() {
        return selectedData;
    }

    /**
     * Seçilen veriyi setler
     *
     * @param selectedData
     */
    public void setSelectedData(F selectedData) {
        this.selectedData = selectedData;
    }

    /**
     * Seçilen verileri döndürür
     *
     * @return
     */
    public F[] getSelectedDatas() {
        return selectedDatas;
    }

    /**
     * Seçilen verileri setler
     *
     * @param selectedDatas
     */
    public void setSelectedDatas(F[] selectedDatas) {
        this.selectedDatas = selectedDatas;
    }

    /**
     * Çoklu seçim yapılıp yapılamıyacağını belirler
     *
     * @return çoklu seçim yapılacaksa True aksi halde false döner
     */
    public Boolean isMultiSelect() {
        return multiSelect;
    }

    /**
     * Çoklu seçim olup olmayacağını setler
     *
     * @param multiSelect
     */
    public void setMultiSelect(Boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    /**
     * Dialogun hangi profilde açılacağı bilgisi.
     *
     * Bazı dialoglar örneğin Bağlantı Seçim Dialoğu açılırken ön prfil
     * bilgisine ihtiyaç duyar, Hepsi, Müşteri, Sağlayıcı v.b.
     *
     * @return
     */
    public String getProfile() {
        return profile;
    }

    /**
     * Dialogun hangi profilde açılacağı bilgisini setler.
     *
     * Bazı dialoglar örneğin Bağlantı Seçim Dialoğu açılırken ön prfil
     * bilgisine ihtiyaç duyar, Hepsi, Müşteri, Sağlayıcı v.b.
     *
     * @param profile
     */
    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getListener() {
        return listener;
    }

    public void setListener(String listener) {
        this.listener = listener;
    }

    /**
     * Generik arama yapılacak olan metin.
     *
     * @return
     */
    public String getSearchText() {
        return searchText;
    }

    /**
     * Generik arama yapılacak olan metni setler.
     *
     * @param searchText
     */
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    
    
    /**
     * Geriye kullanılacak olan model oluşturulur.
     *
     * içinde yeni sınıf oluşturulması ve columns atamaları yapılmalıdır.
     *
     */
    protected void initModel(){
        LookupDataModel<F> model = new LookupDataModel<>();
        buildModel(model);
        setModel(model);
    };

    
    /**
     * Verilen modeli doldurup GUI'de hangi kolonların çıkacağı belirlenir.
     * @param model 
     */
    public abstract void buildModel(LookupDataModel<F> model);

    /**
     * Veri sorgulaması yapılıp model nesnesi doldurulmalıdır.
     */
    public void populateData() {
        //FIXME: Burada veri repository'den alınacak. Geri dönüş mapper işi çözülmeli.
        addData(getRepository().lookupQuery(searchText));
    }

    /**
     * Model nesnesine yeni veri ekler.
     *
     * @param data
     */
    public void addData(List<F> data) {
        model.setWrappedData(data);
    }

    /**
     * GUI'den yeni arama talebi karşılar.
     */
    public void search() {
        LOG.info("Searh Called");
        if (model.getWrappedData() != null) {
            ((List<F>) model.getWrappedData()).clear();
        }
        populateData();
    }

    public List<T> suggest(String text){
        List<T> ls = getRepository().suggestion(text);
        LOG.info("Suggest List : {}", ls);
        return ls;
    }
    
    
    /**
     * Sunumda kullanılacak olan veri modelini döndürür.
     *
     * @return
     */
    public LookupDataModel<F> getModel() {
        return model;
    }

    /**
     * Sunumda kullanılacak olan veri modelini setler.
     *
     * @param model
     */
    public void setModel(LookupDataModel<F> model) {
        this.model = model;
    }

    public void onSelect(SelectEvent event) {
        SelectTuple sl = (SelectTuple) event.getObject();
        if (sl == null) {
            return;
        }
        //EL üzerinden değeri yazacağız
        setObject(sl.getExpression(), sl.getValue());
        /*
        Object bean = Component.getInstance(sl.getBean());
        try {
            BeanUtils.setProperty(bean, sl.getProperty(), sl.getValue());
        } catch (IllegalAccessException ex) {
            LOG.error("onSelect Exception", ex);
        } catch (InvocationTargetException ex) {
            LOG.error("onSelect Exception", ex);
        }*/

        //Eğer tanımlanmış bir event varsa, veri setledikten sonra onu çağırır.
        if (!Strings.isNullOrEmpty(sl.getEventBean())) {

            /* FIXME: Bu işi CDI ile birlikte nasıl yapalım?
            Object eventBean = Component.getInstance(sl.getEventBean());

            Method m;
            try {
                m = eventBean.getClass().getMethod(sl.getEventMethod());
                m.invoke(eventBean);

            } catch (NoSuchMethodException ex) {
                LOG.error("Lookup Event Methodu Hatalı", ex);
            } catch (SecurityException ex) {
                LOG.error("Lookup Event Methodu Hatalı", ex);
            } catch (IllegalAccessException ex) {
                LOG.error("Lookup Event Methodu Hatalı", ex);
            } catch (IllegalArgumentException ex) {
                LOG.error("Lookup Event Methodu Hatalı", ex);
            } catch (InvocationTargetException ex) {
                LOG.error("Lookup Event Methodu Hatalı", ex);
            }
            */
        }
    }

    /**
     * EL kullanarak verilen değeri ilgili yere yazar.
     * 
     * @param expression
     * @param newValue 
     */
    public static void setObject(String expression, Object newValue) {
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application app = facesContext.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        ValueExpression valueExp = elFactory.createValueExpression(elContext, expression, Object.class);

        //Check that the input newValue can be cast to the property type
        //expected by the managed bean.
        //Rely on Auto-Unboxing if the managed Bean expects a primitive
        Class bindClass = valueExp.getType(elContext);
        if (bindClass.isPrimitive() || bindClass.isInstance(newValue)) {
            valueExp.setValue(elContext, newValue);
        }
    }

    /**
     * Select Değişimi için veri modeli kullanıyoruz
     */
    public class SelectTuple implements Serializable {

        private String bean;
        private String property;
        private Object value;
        private String eventBean;
        private String eventMethod;
        private String expression;

        SelectTuple(String bean, String property, Object value) {
            this.bean = bean;
            this.property = property;
            this.value = value;
        }

        public String getBean() {
            return bean;
        }

        public void setBean(String bean) {
            this.bean = bean;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public String getEventBean() {
            return eventBean;
        }

        public void setEventBean(String eventBean) {
            this.eventBean = eventBean;
        }

        public String getEventMethod() {
            return eventMethod;
        }

        public void setEventMethod(String eventMethod) {
            this.eventMethod = eventMethod;
        }

        public String getExpression() {
            return expression;
        }

        public void setExpression(String expression) {
            this.expression = expression;
        }

        
    }
}
