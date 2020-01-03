package com.ozguryazilim.telve.reports;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.hibernate.engine.spi.SessionImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ozguryazilim.telve.config.LocaleSelector;
import com.ozguryazilim.telve.messages.FacesMessages;
import com.ozguryazilim.telve.messages.TelveResourceBundle;
import javax.inject.Named;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvMetadataExporter;
import net.sf.jasperreports.engine.export.JRXlsMetadataExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleCsvMetadataExporterConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

/**
 * Jasper Report ile rpor üretimi ile ilgili işler bu sınıftadır.
 *
 * @author Hakan Uygun
 */
@RequestScoped
@Named
public class JasperReportHandler {

    private static final Logger LOG = LoggerFactory.getLogger(JasperReportHandler.class);

    //Rapor tipleri
    private static final String PDF = ".pdf";
    private static final String CSV = ".csv";
    private static final String XLS = ".xls";

    //Rapor MIME typeları
    private static final String PDF_MIME = "application/pdf";
    private static final String CSV_MIME = "application/csv";
    private static final String XLS_MIME = "application/vnd.ms-excel";

    @Inject
    private FacesContext facesContext;

    @Inject
    private EntityManager entityManager;

    /**
     * Verilen isimli jasper reportu verilen isimli pdf olarak istemciye
     * gönderir. Rapor parametreleri MAp içerisinde gönderilecektir.
     *
     * @param name Jasper Dosya adı. /jasper/{name}.jasper olarak aranacaktır
     * @param fileName İstemciye gönderilecek olan pdf adı {fileName}.pdf olarak
     * yollanacaktır.
     * @param params rapor parametreleri
     * @throws net.sf.jasperreports.engine.JRException
     */
    @SuppressWarnings("rawtypes")
    public byte[] reportToPDFBytes(String name, String fileName, Map params) throws JRException {

        LOG.info("Jasper Rapor Exec : {} {}", name, params);

        decorateParams(params);
        decorateI18NParams("", params);

        InputStream is = getReportSource(name);
        if (is == null) {
            LOG.warn("Dosya Bulunamadı : {}.jasper ", name);
            return null;
        }

        //Rapor üretiliyor
        Connection conn = null;
        byte[] data = null;

        //LOG.info("Bağlantı : {}", dataSource);
        try {
            conn = getConnection();

            //FIXME: Jasper Reportlar için I18N desteği olarak resource eklemek lazım.
            JasperReport report = (JasperReport) JRLoader.loadObject(is);

            data = JasperRunManager.runReportToPdf(report, params, conn);
            LOG.debug("Report is ready");

        } catch (SQLException ex) {
            LOG.error("Connection Error : ", ex);
            FacesMessages.error("Veri tabanı bağlantı hatası!");
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOG.error("Bağlantı kapatılırken hata oluştu. Sebebi: {}", e);
                    FacesMessages.error("Bağlantı kapatılırken hata oluştu. Sebebi: {}", e.getLocalizedMessage());
                }
            }
        }

        return data;
    }

    public byte[] reportToPDFBytes(String name, String fileName, Map params, Collection data) throws JRException {

        LOG.info("Jasper Rapor Exec : {} {}", name, params);

        decorateParams(params);

        InputStream is = getReportSource(name);
        if (is == null) {
            LOG.warn("Dosya Bulunamadı : {}.jasper ", name);
            return null;
        }

        //Rapor üretiliyor
        byte[] result = null;

        //LOG.info("Bağlantı : {}", dataSource);

        JasperReport report = (JasperReport) JRLoader.loadObject(is);

        JRDataSource ds = new JRBeanCollectionDataSource(data);
        result = JasperRunManager.runReportToPdf(report, params, ds);
        LOG.debug("Report is ready");



        return result;
    }

    /**
     * Jasper report ile PDF üretir.
     *
     * @param name Jasper dosyasının adı
     * @param fileName Üretilecek PDF adı
     * @param params parametreler
     * @throws JRException
     */
    public void reportToPDF(String name, String fileName, Map params) throws JRException {

        byte[] data = reportToPDFBytes(name, fileName, params);

        //Üretilen rapor sonuç olarak gönderiliyor
        sendResponse(fileName, PDF, data);
    }

    /**
     * Hazır bir verinin Jasper'a gönderilmesi durumunda kullanılır.
     *
     * @param name
     * @param fileName
     * @param params
     * @param data Örneğin ArrayList içerisinde veriler gönderilir.
     * @throws JRException
     */
    public void reportToPDF(String name, String fileName, Map params, Collection data ) throws JRException {

        byte[] result = reportToPDFBytes(name, fileName, params, data);

        //Üretilen rapor sonuç olarak gönderiliyor
        sendResponse(fileName, PDF, result);
    }

    /**
     * JasperReport'u dil desteği ile çağırır.
     *
     * @param name Jasper dosyasının adı
     * @param bundleName Dil dosyasının adı
     * @param fileName Üretilecek PDF adı
     * @param params Parametreler
     * @throws JRException
     */
    public void reportToPDF(String name, String bundleName, String fileName, Map params) throws JRException {

        decorateI18NParams(bundleName, params);

        byte[] data = reportToPDFBytes(name, fileName, params);

        //Üretilen rapor sonuç olarak gönderiliyor
        sendResponse(fileName, PDF, data);
    }

    /**
     * Verilen isimli jasper reportu verilen isimli xsl olarak istemciye
     * gönderir. Rapor parametreleri Map içerisinde gönderilecektir.
     *
     * @param name Jasper Dosya adı. /jasper/{name}.jasper olarak aranacaktır
     * @param fileName İstemciye gönderilecek olan pdf adı {fileName}.xsl olarak
     * yollanacaktır.
     * @param params rapor parametreleri
     * @throws net.sf.jasperreports.engine.JRException
     */
    @SuppressWarnings("rawtypes")
    public void reportToXLS(String name, String fileName, Map params) throws JRException {

        //LOG.info("Jasper Rapor Exec : {} {} {}", name, fileName, params);
        decorateParams(params);

        InputStream is = getReportSource(name);

        if (is == null) {
            LOG.warn("Dosya Bulunamadı : {}.jasper ", name);
            return;
        }

        Connection conn = null;
        ByteArrayOutputStream os = null;

        try {
            conn = getConnection();

            os = new ByteArrayOutputStream();

            JasperPrint jasperPrint = JasperFillManager.fillReport(is, params, conn);

            JRXlsMetadataExporter exporter = new JRXlsMetadataExporter();

            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            //exporter.setConfiguration(new SimpleXlsExporterConfiguration());
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));

            exporter.exportReport();

            sendResponse(fileName, XLS, os.toByteArray());

        } catch (SQLException ex) {
            LOG.error("Connection Error : ", ex);
            FacesMessages.error("Veri tabanı bağlantı hatası!");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOG.error("Bağlantı kapatılırken hata oluştu. Sebebi: {}", e);
                    FacesMessages.error("Bağlantı kapatılırken hata oluştu. Sebebi: {}", e.getLocalizedMessage());
                }
            }
        }
    }
    
    /**
     * Verilen isimli jasper reportu verilen isimli xsl olarak istemciye
     * gönderir.Rapor parametreleri Map içerisinde gönderilecektir.
     *
     * @param name Jasper Dosya adı. /jasper/{name}.jasper olarak aranacaktır
     * @param fileName İstemciye gönderilecek olan pdf adı {fileName}.xsl olarak
     * yollanacaktır.
     * @param params rapor parametreleri
     * @param data
     * @throws net.sf.jasperreports.engine.JRException
     */
    @SuppressWarnings("rawtypes")
    public void reportToXLS(String name, String fileName, Map params, Collection data) throws JRException {

        //LOG.info("Jasper Rapor Exec : {} {} {}", name, fileName, params);
        decorateParams(params);

        InputStream is = getReportSource(name);

        if (is == null) {
            LOG.warn("Dosya Bulunamadı : {}.jasper ", name);
            return;
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        JRDataSource ds = new JRBeanCollectionDataSource(data);
            
        JasperPrint jasperPrint = JasperFillManager.fillReport(is, params, ds );
        JRXlsMetadataExporter exporter = new JRXlsMetadataExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        //exporter.setConfiguration(new SimpleXlsExporterConfiguration());
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));

        exporter.exportReport();

        sendResponse(fileName, XLS, os.toByteArray());
    }

    public void reportToCSV(String name, String fileName, Map params, Collection data) throws JRException {
        decorateParams(params);

        InputStream is = getReportSource(name);

        if (is == null) {
            LOG.warn("Dosya Bulunamadı : {}.jasper ", name);
            return;
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        JRDataSource ds = new JRBeanCollectionDataSource(data);
        
        JasperPrint jasperPrint = JasperFillManager.fillReport(is, params, ds);
        JRCsvMetadataExporter exporter = new JRCsvMetadataExporter(); //getJasperExporter(outputFormat);
        
        SimpleCsvMetadataExporterConfiguration conf = new SimpleCsvMetadataExporterConfiguration();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        //exporter.setConfiguration( conf );
        exporter.setExporterOutput(new SimpleWriterExporterOutput(os));

        exporter.exportReport();

        sendResponse(fileName, CSV, os.toByteArray());

    }
    
    public void reportToCSV(String name, String fileName, Map params) throws JRException {
        decorateParams(params);

        InputStream is = getReportSource(name);

        if (is == null) {
            LOG.warn("Dosya Bulunamadı : {}.jasper ", name);
            return;
        }

        Connection conn = null;
        ByteArrayOutputStream os = null;

        try {
            conn = getConnection();

            os = new ByteArrayOutputStream();

            JasperPrint jasperPrint = JasperFillManager.fillReport(is, params, conn);

            JRCsvMetadataExporter exporter = new JRCsvMetadataExporter(); //getJasperExporter(outputFormat);

            SimpleCsvMetadataExporterConfiguration conf = new SimpleCsvMetadataExporterConfiguration();

            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            //exporter.setConfiguration( conf );
            exporter.setExporterOutput(new SimpleWriterExporterOutput(os));

            exporter.exportReport();

            sendResponse(fileName, CSV, os.toByteArray());

        } catch (SQLException ex) {
            LOG.error("Connection Error : ", ex);
            FacesMessages.error("Veri tabanı bağlantı hatası!");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOG.error("Bağlantı kapatılırken hata oluştu. Sebebi: {}", e);
                    FacesMessages.error("Bağlantı kapatılırken hata oluştu. Sebebi: {}", e.getLocalizedMessage());
                }
            }
        }
    }

    /**
     * Üretilen raporu http response olarak gönderir.
     *
     * @param fileName sonuç dosya adı
     * @param type gönderilen veri tipi. Tanımlı sabitler kullanılmalı
     * @param data gönderilecek olan veri
     */
    protected void sendResponse(String fileName, String type, byte[] data) {

        //System ayarlarından raporlar icin tanimlanan on eki alir.
        String repPrefix = ConfigResolver.getProjectStageAwarePropertyValue("report.prefix");
        repPrefix = repPrefix == null ? "" : repPrefix + "_";

        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.reset();
        if (null != type) {
            switch (type) {
                case PDF:
                    response.setContentType(PDF_MIME);
                    break;
                case CSV:
                    response.setContentType(CSV_MIME);
                    break;
                case XLS:
                    response.setContentType(XLS_MIME);
                    break;
            }
        }

        response.setHeader("Content-disposition", "attachment;filename=" + repPrefix + fileName + type);
        response.setContentLength(data.length);

        try {

            try (OutputStream out = response.getOutputStream()) {
                out.write(data);

                out.flush();
            }

            facesContext.responseComplete();
        } catch (IOException ex) {
            FacesMessages.error("Error while downloading the file: " + fileName + type);
        }
    }

    /**
     * Parametrelere SUBREPORT yolunu ekler
     *
     * @param params
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void decorateParams(Map<String,Object> params) {
        if (params != null) {
            params.put("SUBREPORT_DIR", "jasper/");

            //MAcro durumdaki dateleri date'e çevirelim.
            for( Map.Entry<String,Object> ent : params.entrySet()){

                if( ent.getValue() instanceof ReportDate ){
                    ent.setValue(((ReportDate)ent.getValue()).getCalculatedValue());
                }
            }
            LOG.debug("Decorated Report Params {}", params);
        }
    }

    protected void decorateI18NParams(String name, Map params) {
        if (params == null) {
            return;
        }
        //Eğer resource tanımlanmamış ise TelveResourceBundle verelim.
        if (params.get(JRParameter.REPORT_RESOURCE_BUNDLE) == null) {
            Locale locale = (Locale) params.get(JRParameter.REPORT_LOCALE);
            if(locale==null) {
        	locale = LocaleSelector.instance().getLocale();
        	//Şimdide Locele bilgisini bağlayaalım...
                params.put(JRParameter.REPORT_LOCALE, locale);
            }
            params.put(JRParameter.REPORT_RESOURCE_BUNDLE, TelveResourceBundle.getBundle(locale));
        }
    }

    /**
     * Geriye JasperReport .jasper rapor kaynağını döner
     *
     * @param name ismin sonunda .jasper olmaması gerek
     * @return
     */
    protected InputStream getReportSource(String name) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream("/jasper/" + name + ".jasper");
    }

    /**
     * Jasper report şablonunu derler
     *
     * @param is
     * @return
     * @throws JRException
     * @throws Exception
     */
    private JasperReport compileReport(InputStream is) throws JRException {
        return JasperCompileManager.compileReport(is);
    }

    /**
     * Geriye veri tabanı için bağlantıyı döner.
     *
     * TODO: bunu aslında bi rproducer'dan mı alsak
     *
     * @return
     * @throws SQLException
     */
    protected Connection getConnection() throws SQLException {
        Connection conn;

        //Hibernate 4.x API
        SessionImplementor sessionImplementor = entityManager.unwrap(SessionImplementor.class);
        conn = sessionImplementor.getJdbcConnectionAccess().obtainConnection();

        return conn;
    }

}
