/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.reports.schedule;

import com.google.common.base.Splitter;
import com.ozguryazilim.telve.channel.email.EmailChannel;
import com.ozguryazilim.telve.channel.notify.NotifyChannel;
import com.ozguryazilim.telve.messagebus.command.AbstractCommandExecuter;
import com.ozguryazilim.telve.messagebus.command.CommandExecutor;
import com.ozguryazilim.telve.reports.JasperReportHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.inject.Inject;
import javax.mail.util.ByteArrayDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
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

    @Inject
    private JasperReportHandler reportHandler;
    
    @Inject
    private NotifyChannel notifyChannel;
    
    @Inject
    private EmailChannel emailChannel;
    
    @Override
    public void execute(ReportCommand command) {
        
        LOG.info("Rapor Çalıştırılacak : {}", command.getName());
        
        byte[] pdf = null;
        
        try {
            
            Map<String, Object> params = command.getReportParams();
            
            
            //Sonra resource bundle'ı bağlayalım,
            Locale locale = new Locale(command.getLocale());
            params.put(JRParameter.REPORT_RESOURCE_BUNDLE, ResourceBundle.getBundle(command.getBundleName(), locale));

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
        attachments.put(command.getResultName(), dataHandler);
        
        notifyChannel.sendMessage(command.getUser(), "İstenilen Rapor Hazırlandı", "Rapor hazır ve gönderildi");
        
        List<String> emails =Splitter.on(',').omitEmptyStrings().trimResults().splitToList(command.getEmails());
        for( String mailAddr : emails ){
            emailChannel.sendMessageWithAttachments(mailAddr, "Rapor hazırlandı", "Rapor hazır ve nazırdır.", attachments);
        }
    }
    
}
