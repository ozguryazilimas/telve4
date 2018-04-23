/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.reports;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.primefaces.context.RequestContext;

/**
 *
 * @author oyas
 */
public abstract class AbstractReportBase implements ReportController, Serializable{
    
    @Inject
    private ViewConfigResolver viewConfigResolver;

    
    
    
    @Override
    public void execute() {
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        //options.put("draggable", false);  
        options.put("resizable", false);
        options.put("contentHeight", 450);

        RequestContext.getCurrentInstance().openDialog(getDialogName(), options, null);
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
        return this.getClass().getAnnotation(Report.class).filterPage();
    }

    /**
     * Dialogu hiç bir şey seçmeden kapatır.
     */
    public void cancelDialog() {
        //RequestContext.getCurrentInstance().closeDialog("Rapordan İptalle Çıkıldı");
        RequestContext.getCurrentInstance().closeDialog(null);
    }
    
    protected String getTemplateName() {
        String s = this.getClass().getAnnotation(Report.class).template();
        if (s.isEmpty()) {
            s = this.getClass().getSimpleName();
        }
        return s;
    }
    
    protected String getBundleName(){
        //Önce Resource Bundle adını öğrenelim
        String s = this.getClass().getAnnotation(Report.class).resource();
        return s;
    }
    
    /**
     * Raporun yetki domainini döndürür.
     * @return 
     */
    public String getPermission(){
        String s = this.getClass().getAnnotation(Report.class).permission();
        if (s.isEmpty()) {
            s = this.getClass().getSimpleName();
        }
        return s;
    }
}
