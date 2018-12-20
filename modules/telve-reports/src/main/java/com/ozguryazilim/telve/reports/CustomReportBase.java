package com.ozguryazilim.telve.reports;

import com.ozguryazilim.telve.messages.FacesMessages;
import java.io.IOException;
import java.io.OutputStream;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import org.apache.deltaspike.core.api.config.ConfigResolver;

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
public abstract class CustomReportBase extends AbstractReportBase{

    
    @Inject
    private FacesContext facesContext;
    
    public abstract void execReport();

    
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
