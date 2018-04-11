/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.reports;

import com.ozguryazilim.telve.messages.FacesMessages;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.primefaces.context.RequestContext;

/**
 * Custom Raporlar için Taban Sınıf.
 * 
 * Bu sınıf misras alınarak üretilen raporlarda geliştirici kendi motorunu kullanır.
 * 
 * Çoğunlukla Özel excel çıktıları için kullanılır.
 * 
 * Rapor filtre arayüzü için reportFilterBase.xhtml miras alınır.
 * 
 * @author Hakan Uygun
 */
public abstract class CustomReportBase implements ReportController, Serializable{

    @Inject
    private ViewConfigResolver viewConfigResolver;
    
    @Inject
    private FacesContext facesContext;
    
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
    
    
    public abstract void execReport();

    /**
     * Dialogu hiç bir şey seçmeden kapatır.
     */
    public void cancelDialog() {
        RequestContext.getCurrentInstance().closeDialog("Rapordan İptalle Çıkıldı");
    }
    
    
    /**
     * Üretilen raporu http response olarak gönderir.
     *
     * @param fileName sonuç dosya adı
     * @param mimeType dosya mimeType bilgisi
     * @param data gönderilecek olan veri
     */
    protected void sendResponse(String fileName, String mimeType, byte[] data) {

        //System ayarlarından raporlar icin tanimlanan on eki alir.
        String repPrefix = ConfigResolver.getProjectStageAwarePropertyValue("report.prefix");
        repPrefix = repPrefix == null ? "" : repPrefix + "_";

        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.reset();
        response.setContentType(mimeType);
        
        response.setHeader("Content-disposition", "attachment;filename=" + repPrefix + fileName );
        response.setContentLength(data.length);

        try {

            try (OutputStream out = response.getOutputStream()) {
                out.write(data);

                out.flush();
            }

            facesContext.responseComplete();
        } catch (IOException ex) {
            FacesMessages.error("Error while downloading the file: " + fileName );
        }
    }
}
