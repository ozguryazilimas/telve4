/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.reports;

import com.ozguryazilim.telve.config.LocaleSelector;
import com.ozguryazilim.telve.messages.FacesMessages;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.inject.Inject;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JasperReport raporları için Controller Base sınıfı.
 *
 * @author Hakan Uygun
 */
public abstract class JasperReportBase implements ReportController, Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(JasperReportBase.class);
    
    @Inject
    private ViewConfigResolver viewConfigResolver;

    @Inject
    private JasperReportHandler jasperReportHandler;

    @Inject
    private ReportHome reportHome;

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
     * JasperReport'a gönderilecek parametreler setlenir.
     * Eğer bir sorun varsa geriye "False" döndürürlür.
     * Hataları FacesMessage olarak bildirilebilir.
     * @param params
     * @return 
     */
    protected abstract boolean buildParam( Map<String, Object> params );
    
    public void execPDF() {
        try {
            
            Map<String, Object> params = new HashMap<>();
            if( buildParam(params) ){
                decorateParams( params );
                jasperReportHandler.reportToPDF( getTemplateName(), getTemplateName(), params );
            }
        } catch (JRException ex) {
            LOG.error("JasperReport Error", ex);
            FacesMessages.error(ex.getMessage());
        }
    }

    /**
     * Dialogu hiç bir şey seçmeden kapatır.
     */
    public void cancelDialog() {
        RequestContext.getCurrentInstance().closeDialog("Rapordan İptalle Çıkıldı");
    }
    
    
    protected String getTemplateName(){
        String s = this.getClass().getAnnotation(Report.class).template();
        if( s.isEmpty() ){
            s = this.getClass().getSimpleName();
        }
        return s;
    }
    
    /**
     * Jasper rapor parametrelerini sistem tarafından gelen değerlerle zenginleştirir.
     * 
     * @param params 
     */
    protected void decorateParams( Map<String, Object > params ){
       decorateI18NParams( params ); 
    }
    
    /**
     * Jasper Report I18N desteği eklenir.
     * 
     * @param params 
     */
    protected void decorateI18NParams( Map<String, Object > params ){
        
        //Önce Resource Bundle adını öğrenelim
        String s = this.getClass().getAnnotation(Report.class).resource();
        if( s.isEmpty() ){
            //Eğer Resorce tanımlanmamışsa template ile aynı ismi kullanıyoruz.
            s = getTemplateName();
        }
        
        //Sonra resource bundle'ı bağlayalım,
        Locale locale = LocaleSelector.instance().getLocale();
        params.put(JRParameter.REPORT_RESOURCE_BUNDLE, ResourceBundle.getBundle(s, locale ));
        
        //Şimdide Locele bilgisini bağlayaalım...
        params.put(JRParameter.REPORT_LOCALE, locale );
        
    }
    
}
