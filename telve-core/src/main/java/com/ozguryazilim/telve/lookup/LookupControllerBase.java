/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.lookup;

import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.entities.EntityBase;
import com.ozguryazilim.telve.entities.ViewModel;
import com.ozguryazilim.telve.utils.ELUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.Entity;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @Inject
    private ViewConfigResolver viewConfigResolver;

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
        search();
    }

    public void closeOverlay() {

        LookupSelectTuple sl = getLookupSelectTuple();

        ELUtils.setObject(sl.getExpression(), sl.getValue());

    }

    

    /**
     * İlgili sınıfa ait dialogu açar
     *
     * @param multiSelect
     * @param listener
     */
    public void openDialog(Boolean multiSelect, String listener) {
        openDialog(multiSelect, "", listener);
    }

    /**
     * İlgili sınıfa ait dialogu açar
     *
     * @param multiSelect çoklu seçime izin var mı?
     * @param profile sorgu profili
     * @param listener sonuçlar nereye gidecek?
     */
    public void openDialog(Boolean multiSelect, String profile, String listener) {
        model.setMultiSelect(multiSelect);
        model.setProfile(profile);
        model.setListener(listener);

        Map<String, Object> options = new HashMap<>();
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

        LookupSelectTuple sl = getLookupSelectTuple();

        /* FIXME: event mekanizması nasıl olacak?
         //Eğer event varsa...
         if (srcs.length > 1) {
         //Bean.method formatında beklyoruz.
         s = srcs[1].split("\\.");
         sl.setEventBean(s[0]);
         sl.setEventMethod(s[1]);
         }
         */
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
    private E getEntity(R viewModel) {
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
        List<E> ls = getRepository().suggestion(text);
        LOG.info("Suggest List : {}", ls);
        return ls;
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
        LookupSelectTuple sl = (LookupSelectTuple) event.getObject();
        if (sl == null) {
            return;
        }
        //EL üzerinden değeri yazacağız
        ELUtils.setObject(sl.getExpression(), sl.getValue());


        /* FIXME: Bu işi CDI ile birlikte nasıl yapalım?
         //Eğer tanımlanmış bir event varsa, veri setledikten sonra onu çağırır.
         if (!Strings.isNullOrEmpty(sl.getEventBean())) {

            
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
            
         }
         */
    }

    /**
     * GUI'den seçilen bilgileri kullanarak seçim paketi oluşturur.
     *
     * @return
     */
    protected LookupSelectTuple getLookupSelectTuple() {
        LookupSelectTuple sl;

        String expression = "#{" + model.getListener() + "}";

        if (model.getMultiSelect()) {

            List<E> ls = new ArrayList<>();

            for (R o : model.getSelectedViewModels()) {
                E e = getEntity(o);
                if (e != null) {
                    ls.add(e);
                }
            }

            //Listeyi el ile value olarak gömmek için...
            sl = new LookupSelectTuple(expression, ls);

        } else {
            E e = getEntity(model.getSelectedViewModel());
            sl = new LookupSelectTuple(expression, e);
        }

        return sl;
    }
    
    /**
     * Geriye listelerde gösterim için hangi attributun kullanılacağı bilgisini döndürür.
     * @return 
     */
    public abstract String getCaptionFieldName();
}
