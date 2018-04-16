/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.reports;

import com.ozguryazilim.telve.config.LocaleSelector;
import com.ozguryazilim.telve.messages.FacesMessages;
import com.ozguryazilim.telve.messages.Messages;
import com.ozguryazilim.telve.messages.TelveResourceBundle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperCsvExporterBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperXlsxExporterBuilder;
import net.sf.dynamicreports.report.builder.style.TemplateStylesBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRParameter;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DynamicReports için taban sınıf.
 * 
 * 
 * 
 * @author Hakan Uygun
 * @param <F> Filter Model Class
 */
public abstract class DynamicReportBase<F> extends AbstractReportBase{

    private static final Logger LOG = LoggerFactory.getLogger(DynamicReportBase.class);

    @Inject
    private FacesContext facesContext;
    
    
    private F filter;

    public F getFilter() {
        if( filter == null ){
            filter = buildFilter();
        }
        return filter;
    }

    public void setFilter(F filter) {
        this.filter = filter;
    }
    
    /**
     * Filter nesnesi ilk değerleri ile oluşturulur.
     * @return 
     */
    protected abstract F buildFilter();
    
    
    /**
     * Rapor detayları bu method içerisinde hazırlanacak
     * @param report 
     */
    protected abstract void buildReport( JasperReportBuilder report, Boolean forExport );
    
    /**
     * Rapor parametrelerinin alt sınıflar tarafından değiştirilebilmesini sağlar.
     * @param params 
     */
    protected void decorateParams( Map<String, Object> params ){
        
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
    
    protected void decorateBrand( Map<String, Object> params ){
        String logo = ConfigResolver.getPropertyValue("brand.company.reportLogo");
        String title = ConfigResolver.getPropertyValue("brand.company.reportTitle");

        if (logo != null) {
            BufferedImage image;
            try {
                image = ImageIO.read(getClass().getResource("/META-INF/resources/brand/img/".concat(logo)));
                params.put("FIRM_LOGO", image);
            } catch (IOException e) {
                LOG.debug("Brand image not found", e);
            }
        }
        
        params.put("FIRM_TITLE", title);
    }
    
    /**
     * Rapor için kullanılacak verilerin çekileceği veri kaynağı
     * @return 
     */
    protected abstract JRDataSource getReportDataSource();
    
    /**
     * Sınıf adından i18n dil dosyası için key üretir.
     * @return 
     */
    protected String getReportTitle(){
        return "report.name." + getClass().getSimpleName();
    }
    
    /**
     * İstenir ise üzeri yazılarak rapor için ek açıklama çıkartılabilir.
     * Geriye i18n key ya da metin dönebilir.
     * @return 
     */
    protected String getReportSubTitle(){
        return "";
    }
    
    /**
     * İstenir ise üzeri yazılarak rapor için dosya adı üretilir.
     * Varsayılan hali ile rapor Sınıf adını kullanır.
     * @return 
     */
    protected String getResultFileName(){
        return getClass().getSimpleName();
    }
    
    /**
     * Rapor için taban şablon ve gereklilikler tanımlanıyor.
     * @throws DRException 
     */
    public JasperReportBuilder initReport() throws DRException{
        Map<String,Object> params = new HashMap<>();
        params.put("REPORT_TITLE",  Messages.getMessage(getReportTitle()));
        params.put("REPORT_SUBTITLE", Messages.getMessage(getReportSubTitle()));
    
        //decorateI18NParams(params);
        decorateBrand(params);
        decorateParams(params);
        
        //Template'lerin isimleri configden alınıyor. Bu sayede uygulamalar override edebilir. Varsayılan değerler bu proje içerisinde.
        InputStream ist = getClass().getResourceAsStream("/" + ConfigResolver.getPropertyValue("dynamicreports.template", "telveTemplate.jrxml"));
        InputStream iss = getClass().getResourceAsStream("/" + ConfigResolver.getPropertyValue("dynamicreports.styles", "telve.jrtx"));
        
        TemplateStylesBuilder tsb = stl.loadStyles(iss);
        
        JasperReportBuilder rb = report()
            .templateStyles(tsb)
            .setResourceBundle(((ResourceBundle)params.get(JRParameter.REPORT_RESOURCE_BUNDLE)))
            .setColumnStyle(stl.templateStyle("Base"))
            .setColumnTitleStyle(stl.templateStyle("ColumnTitle"))
            .setTemplateDesign(ist)
            .setParameters(params)
            .setSubtotalStyle(stl.templateStyle("Subtotal"));
                
        buildReport( rb, Boolean.FALSE );
        
        return rb;
    }
    
    protected JasperReportBuilder initReportForExport() throws DRException{
        Map<String,Object> params = new HashMap<>();
        
        decorateI18NParams(params);
        decorateParams(params);
        
        JasperReportBuilder rb = report()
            .ignorePageWidth()
	    .ignorePagination()
            .setParameters(params);
                
        buildReport( rb, Boolean.TRUE );
        
        return rb;
    }
    
    public void execPDF() {
        
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.reset();
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "attachment;filename=" + getResultFileName() + ".pdf");

        try {

            try (OutputStream out = response.getOutputStream()) {
                initReport()
                    .setDataSource(getReportDataSource())
                    .toPdf(out);

                out.flush();
            } catch (DRException ex) {
                LOG.error("Report cannot generated", ex);
            }

            facesContext.responseComplete();
        } catch (IOException ex) {
            LOG.error("Report generation exception", ex);
            FacesMessages.error("Error while downloading the file: " + getResultFileName() );
        }
        
    }
    
    
    public void execCSV() {
        
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.reset();
        response.setContentType("text/csv");
        response.setHeader("Content-disposition", "attachment;filename=" + getResultFileName() + ".csv");

        try {

            
            
            try (OutputStream out = response.getOutputStream()) {
                
                JasperCsvExporterBuilder exb = export.csvExporter(out)
                        .setFieldDelimiter(","); //TODO: Burda değeri kullanıcı ayarlarından alalım. Yeni exceller ; kullanıyor
                        
                        
                initReportForExport()
                    .setDataSource(getReportDataSource())
                    .toCsv(exb);

                out.flush();
            } catch (DRException ex) {
                LOG.error("Report cannot generated", ex);
            }

            facesContext.responseComplete();
        } catch (IOException ex) {
            LOG.error("Report generation exception", ex);
            FacesMessages.error("Error while downloading the file: " + getResultFileName() );
        }
        
    }
    
    public void execXLS() {
        
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.reset();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-disposition", "attachment;filename=" + getResultFileName() + ".xlsx");

        try {

            try (OutputStream out = response.getOutputStream()) {
                
                JasperXlsxExporterBuilder exb = export.xlsxExporter(out)
                        .setIgnoreAnchors(Boolean.TRUE)
                        .setIgnoreCellBackground(Boolean.TRUE)
                        .setIgnoreCellBorder(Boolean.TRUE)
                        .setIgnoreGraphics(Boolean.TRUE)
                        .setIgnoreHyperLink(Boolean.TRUE)
                        .setIgnorePageMargins(Boolean.TRUE)
                        .setWhitePageBackground(Boolean.FALSE)
                        .setRemoveEmptySpaceBetweenColumns(Boolean.TRUE)
                        .setRemoveEmptySpaceBetweenRows(Boolean.TRUE)
                        .setDetectCellType(Boolean.TRUE);
                        
                initReportForExport()
                    .setDataSource(getReportDataSource())
                    .toXlsx(exb);

                out.flush();
            } catch (DRException ex) {
                LOG.error("Report cannot generated", ex);
            }

            facesContext.responseComplete();
        } catch (IOException ex) {
            LOG.error("Report generation exception", ex);
            FacesMessages.error("Error while downloading the file: " + getResultFileName() );
        }
        
    }
    
    protected String msg( String key ){
        return Messages.getMessage(key);
    }
}
