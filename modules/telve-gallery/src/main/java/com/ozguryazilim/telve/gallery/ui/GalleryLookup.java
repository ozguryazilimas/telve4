package com.ozguryazilim.telve.gallery.ui;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.lookup.LookupSelectTuple;
import com.ozguryazilim.telve.utils.ELUtils;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.jcr.RepositoryException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gallery Images Lookup Dialog Control Class.
 * 
 * @author Hakan Uygun
 */
@Named
@SessionScoped
public class GalleryLookup extends AbstractGalleryController {

    private static final Logger LOG = LoggerFactory.getLogger(GalleryLookup.class);
    
    private String listener;
    private String selectedId;
    
    @PostConstruct
    public void init(){
        populateGalleries();
    }
    
    public void openDialog( String gallery, String listener ) throws RepositoryException{
        
        selectedId = "";
        this.listener = listener;
        
        setSelectedGallery(gallery);
        
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        //options.put("draggable", false);  
        options.put("resizable", false);
        options.put("contentHeight", 450);

        search();
        
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

        //eğer bir event listener var ise
        //if( model.getListener().contains("event:")){
        //    triggerListeners(model.getListener(),sl.getValue());
        //}
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
    
    
    public String getDialogName() {
        return "/gallery/galleryLookup";
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
        
        if( sl.getExpression().isEmpty() ) return;
        
        //EL üzerinden değeri yazacağız
        ELUtils.setObject(sl.getExpression(), sl.getValue());

    }
    
    /**
     * GUI'den seçilen bilgileri kullanarak seçim paketi oluşturur.
     *
     * @return
     */
    protected LookupSelectTuple getLookupSelectTuple() {
        LookupSelectTuple sl;

        String expression = "";
        if( !Strings.isNullOrEmpty(listener) && !listener.contains(":")){
            expression = "#{" + listener + "}";
        }

        sl = new LookupSelectTuple(expression, selectedId);

        return sl;
    }
    
    public void selectItem( String id ){
        selectedId = id;
        closeDialog();
    }
    
}
