/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.reports;

import com.ozguryazilim.telve.config.LocaleSelector;
import com.ozguryazilim.telve.messages.FacesMessages;
import com.ozguryazilim.telve.messages.TelveResourceBundle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.inject.Inject;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JasperReport raporları için Controller Base sınıfı.
 *
 * @author Hakan Uygun
 */
public abstract class JasperReportBase extends AbstractReportBase{

    private static final Logger LOG = LoggerFactory.getLogger(JasperReportBase.class);

    @Inject
    private JasperReportHandler jasperReportHandler;

    @Inject
    private ReportHome reportHome;
    
    @Inject
    private ReportScheduleDialog scheduleDialog;
    
    @Inject
    private Subject identity;

    /**
     * JasperReport'a gönderilecek parametreler setlenir. Eğer bir sorun varsa
     * geriye "False" döndürürlür. Hataları FacesMessage olarak bildirilebilir.
     *
     * @param params
     * @return
     */
    protected abstract boolean buildParam(Map<String, Object> params);

    public void execPDF() {
        try {

            Map<String, Object> params = new HashMap<>();
            if (buildParam(params)) {
                decorateParams(params);
                jasperReportHandler.reportToPDF(getTemplateName(), getTemplateName(), params);
            }
        } catch (JRException ex) {
            LOG.error("JasperReport Error", ex);
            FacesMessages.error(ex.getMessage());
        }
    }

    public void execScheduler() {

        Map<String, Object> params = new HashMap<>();
        if (buildParam(params)) {
            decorateParams(params);
            
            ReportCommand command = new ReportCommand();
            
            //rapor locale bilgilerini koyuyoruz.
            //Bundle ve Locale varsa siliyoruz. Çünkü serialize olmuyorlar.
            Object o = params.get(JRParameter.REPORT_RESOURCE_BUNDLE);
            
            if( o instanceof TelveResourceBundle ){
                command.setBundleName("TelveResourceBundle");
            } else {
                command.setBundleName(getBundleName());
            }
            
            Locale locale = LocaleSelector.instance().getLocale();
            command.setLocale(locale.getLanguage());
            
            params.remove(JRParameter.REPORT_RESOURCE_BUNDLE);
            params.remove(JRParameter.REPORT_LOCALE);
            

            command.setName(getClass().getSimpleName());
            command.setTemplateName(getTemplateName());
            command.setResultName(getTemplateName());
            command.setReportParams(params);
            command.setUser(identity.getPrincipal().toString());
            
            
            scheduleDialog.openDialog(command);
        }
    }

    public void execCSV() {
        try {

            Map<String, Object> params = new HashMap<>();
            if (buildParam(params)) {
                decorateParams(params);
                jasperReportHandler.reportToCSV(getTemplateName(), getTemplateName(), params);
            }
        } catch (JRException ex) {
            LOG.error("JasperReport Error", ex);
            FacesMessages.error(ex.getMessage());
        }
    }

    public void execXLS() {
        try {

            Map<String, Object> params = new HashMap<>();
            if (buildParam(params)) {
                decorateParams(params);
                jasperReportHandler.reportToXLS(getTemplateName(), getTemplateName(), params);
            }
        } catch (JRException ex) {
            LOG.error("JasperReport Error", ex);
            FacesMessages.error(ex.getMessage());
        }
    }

    /**
     * Jasper rapor parametrelerini sistem tarafından gelen değerlerle
     * zenginleştirir.
     *
     * @param params
     */
    protected void decorateParams(Map<String, Object> params) {
        decorateI18NParams(params);
    }

    /**
     * Jasper Report I18N desteği eklenir.
     *
     * @param params
     */
    protected void decorateI18NParams(Map<String, Object> params) {

        String s = getBundleName();
        Locale locale = LocaleSelector.instance().getLocale();
        
        if( s.isEmpty() ){
            //Eğer bundle name verilmemiş ise TelveResourceBundle bağlıyoruz.
            params.put(JRParameter.REPORT_RESOURCE_BUNDLE, TelveResourceBundle.getBundle(locale));
        } else {
            //Sonra resource bundle'ı bağlayalım,
            params.put(JRParameter.REPORT_RESOURCE_BUNDLE, ResourceBundle.getBundle(s, locale));
        }

        //Şimdide Locele bilgisini bağlayaalım...
        params.put(JRParameter.REPORT_LOCALE, locale);

    }
}
