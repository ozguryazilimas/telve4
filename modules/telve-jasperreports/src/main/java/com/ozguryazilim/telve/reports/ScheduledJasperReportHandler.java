package com.ozguryazilim.telve.reports;

import com.google.common.base.Splitter;
import com.ozguryazilim.telve.channel.email.EmailChannel;
import com.ozguryazilim.telve.channel.notify.NotifyChannel;
import com.ozguryazilim.telve.messages.TelveResourceBundle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.util.ByteArrayDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author @author Aydoğan Sel <aydogan.sel at iova.com.tr>>
 */
@RequestScoped
@Named("scheduledJasperReportHandler")
public class ScheduledJasperReportHandler implements ScheduledReportHandler{
    
    private static final Logger LOG = LoggerFactory.getLogger(ScheduledJasperReportHandler.class);

    @Inject
    private NotifyChannel notifyChannel;
    
    @Inject
    private EmailChannel emailChannel;
    
    @Inject 
    private JasperReportHandler reportHandler;

    @Override
    public void execute(ReportCommand command) {
        LOG.info("Rapor Çalıştırılacak : {}", command.getName());
        
        byte[] pdf = null;
        
        try {
            
            Map<String, Object> params = command.getReportParams();
            
            
            //Sonra resource bundle'ı bağlayalım,
            Locale locale = new Locale(command.getLocale());
            if( "TelveResourceBundle".equals(command.getBundleName())){
                params.put(JRParameter.REPORT_RESOURCE_BUNDLE, TelveResourceBundle.getBundle( locale ));
            } else {
                params.put(JRParameter.REPORT_RESOURCE_BUNDLE, ResourceBundle.getBundle(command.getBundleName(), locale));
            }

            //Şimdide Locele bilgisini bağlayaalım...
            params.put(JRParameter.REPORT_LOCALE, locale);
            
            pdf = reportHandler.reportToPDFBytes(command.getTemplateName(), command.getResultName(), params);
        } catch (JRException ex) {
            LOG.error( "Rapor alınırken hata oluştu", ex);
            return;
        }
        
        DataSource ds = new ByteArrayDataSource(pdf, "application/pdf");
        DataHandler dataHandler = new DataHandler(ds);
        
        Map<String,DataHandler> attachments = new HashMap<>();
        attachments.put(command.getResultName() + ".pdf", dataHandler);
        
        
        Map<String,Object> headers = new HashMap<>();
        headers.put("reportName", command.getName());
        headers.put("info", command.getInfo());
        headers.put("messageClass", "REPORT");
        
        notifyChannel.sendMessage(command.getUser(), "İstenilen Rapor Hazırlandı", "Rapor hazır ve gönderildi", headers);
        
        
        
        List<String> emails =Splitter.on(',').omitEmptyStrings().trimResults().splitToList(command.getEmails());
        for( String mailAddr : emails ){
            emailChannel.sendMessageWithAttachments(mailAddr, "Rapor hazırlandı", "Rapor hazır ve nazırdır.", attachments, headers);
        }
        
    }
    
    
    
}
