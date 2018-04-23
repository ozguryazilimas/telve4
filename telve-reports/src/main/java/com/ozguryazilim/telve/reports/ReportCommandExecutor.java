/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.reports;

import com.ozguryazilim.telve.channel.email.EmailChannel;
import com.ozguryazilim.telve.channel.notify.NotifyChannel;
import com.ozguryazilim.telve.messagebus.command.AbstractCommandExecuter;
import com.ozguryazilim.telve.messagebus.command.CommandExecutor;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Arka plan rapor çalıştırma.
 * 
 * @author Hakan Uygun
 */
@CommandExecutor(command = ReportCommand.class)
public class ReportCommandExecutor extends AbstractCommandExecuter<ReportCommand>{
    
    private static final Logger LOG = LoggerFactory.getLogger(ReportCommandExecutor.class);

    //@Inject
    //private JasperReportHandler reportHandler;
    
    @Inject
    private NotifyChannel notifyChannel;
    
    @Inject
    private EmailChannel emailChannel;
    
    @Override
    public void execute(ReportCommand command) {
        
        LOG.info("Rapor Çalıştırılacak : {}", command.getName());
        /* FIXME: Burada farklı motorlar için command executor'ı çeşitlemek gerekecek.
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
        */
    }
    
}
