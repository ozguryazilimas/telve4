/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.reports;

import com.ozguryazilim.telve.messages.FacesMessages;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsExporterConfiguration;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.hibernate.engine.spi.SessionImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jasper Report ile rpor üretimi ile ilgili işler bu sınıftadır.
 *
 * @author Hakan Uygun
 */
public class JasperReportHandler {

    private static final Logger LOG = LoggerFactory.getLogger(JasperReportHandler.class);

    //Rapor tipleri
    private static final String PDF = ".pdf";
    private static final String CSV = ".csv";
    private static final String XSL = ".xsl";

    //Rapor MIME typeları
    private static final String PDF_MIME = "application/pdf";
    private static final String CSV_MIME = "application/csv";
    private static final String XSL_MIME = "application/vnd.ms-excel";

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
    public void reportToPDF(String name, String fileName, Map params) throws JRException {

        LOG.info("Jasper Rapor Exec : {} {}", name, params);

        decorateParams(params);

        InputStream is = getReportSource(name);
        if (is == null) {
            LOG.warn("Dosya Bulunamadı : {}.jasper ", name);
            return;
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
            return;
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
    public void reportToXls(String name, String fileName, Map params) throws JRException {

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

            JRXlsExporter exporter = new JRXlsExporter(); //getJasperExporter(outputFormat);

            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setConfiguration( new SimpleXlsExporterConfiguration());
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));
            
            /*
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
            exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, true);
            exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, true);
            exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, true);
            exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, false);
            exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, false);
            exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, true);
            */
            
            /*
             JExcelApiMetadataExporter exporter = new JExcelApiMetadataExporter();
            
            
             //JRCsvMetadataExporterParameter.
             exporter.setParameter(JExcelApiExporterParameter.JASPER_PRINT, jasperPrint);
             exporter.setParameter(JExcelApiExporterParameter.OUTPUT_STREAM, os );
             exporter.setParameter(JExcelApiExporterParameter.CHARACTER_ENCODING, "UTF-8" );
             //exporter.setParameter(JExcelApiExporterParameter.WRITE_HEADER, true );
             //exporter.setParameter(JRCsvExporterParameter.IGNORE_PAGE_MARGINS, true );
             */
            exporter.exportReport();

            sendResponse(fileName, XSL, os.toByteArray());
            
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
                case XSL:
                    response.setContentType(XSL_MIME);
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
    protected void decorateParams(Map params) {
        if (params != null) {
            params.put("SUBREPORT_DIR", "jasper/");
        }
    }

    /**
     * Geriye JasperReport .jasper rapor kaynağını döner
     *
     * @param name ismin sonunda .jasper olmaması gerek
     * @return
     */
    protected InputStream getReportSource(String name) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream("jasper/" + name + ".jasper");
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
