/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command.ui;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.ozguryazilim.telve.messages.FacesMessages;
import com.ozguryazilim.telve.view.Pages;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.scope.GroupedConversation;
import org.apache.deltaspike.core.api.scope.GroupedConversationScoped;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ayarlarda verilmiş olan log dosyasını göstermek için kullanılır.
 *
 * @author Hakan Uygun
 */
@Named
@GroupedConversationScoped
public class LogViewer implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(LogViewer.class);
    
    @Inject
    private GroupedConversation conversation;

    private List<String> logLines;

    /**
     * Regex için kullanılacak değer.
     */
    private String filter = "ERROR";

    
    private List<String> logNames;
    
    /**
     * Sununulacak olan log'un ahngisi olduğu
     */
    private String logName = "";
    
    /**
     * Configürasyondan hangi logların takip edilebileceğini toparlar.
     */
    @PostConstruct
    public void init(){
        
        String loglst = ConfigResolver.getProjectStageAwarePropertyValue("logviwer.log.list");
        
        if( Strings.isNullOrEmpty(loglst)){
            logNames = new ArrayList<>();
            return;
        } 
        
        logNames = Splitter.on(',').omitEmptyStrings().trimResults().splitToList(loglst);
        
        //İlkini default olarak seçiyoruz.
        if( !logNames.isEmpty() ){
            logName = logNames.get(0);
        }
        
    }
    
    /**
     * Verilen Log Name için UI caption'ı döndürür.
     * 
     * eğer bulamaz ise logName'i döndürür.
     * 
     * TODO: Bu method i18n desteği sağlamalı
     * 
     * @param logName
     * @return 
     */
    public String getLogCaption( String logName ){
        return ConfigResolver.getProjectStageAwarePropertyValue("logviwer.log." + logName + ".caption", logName);
    }
    
    /**
     * Verilen LogName için kullanılacak dosya adını döndürür.
     * 
     * Eğer config'de tanımlanmamış ise null döner.
     * 
     * @param logName
     * @return 
     */
    public String getLogFileName( String logName ){
        return ConfigResolver.getProjectStageAwarePropertyValue("logviwer.log." + logName + ".fileName");
    }
    /**
     * Read Filtred Log Lines
     *
     * @throws IOException
     */
    private void readFilteredLogLines() throws IOException {
        
        //Önce içini bir boşaltalım.
        logLines.clear();

        String logFileName = getLogFileName(this.logName);
        
        if( Strings.isNullOrEmpty(logFileName)){
            FacesMessages.warn("message.logfile.NotConfigured");
            return;
        }
        
        File file = new File( logFileName );
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        Pattern pattern = null;
        Matcher matcher = null;

        //Eğer filtre varsa regex işine giriyoruz.
        if (!Strings.isNullOrEmpty(filter)) {
            pattern = Pattern.compile(filter);
            matcher = pattern.matcher("\\D");
        }

        String line;
        while ((line = br.readLine()) != null) {
            //Eğer filtre yoksa tüm satırları al
            if (matcher != null) {
                matcher.reset(line);
                if (matcher.find()) {
                    logLines.add(line);
                }
            } else {
                logLines.add(line);
            }
        }
        br.close();
        fr.close();
    }

    public List<String> getLogLines() throws IOException {
        if (logLines == null) {
            logLines = new ArrayList<>();
            readFilteredLogLines();
        }

        return logLines;
    }
    
    public void refresh() throws IOException{
        readFilteredLogLines();
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Class<? extends ViewConfig> close() {
        //tailer.stop();
        conversation.close();
        return Pages.Home.class;
    }

    public List<String> getLogNames() {
        return logNames;
    }

    public void setLogNames(List<String> logNames) {
        this.logNames = logNames;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }
    
    /**
     * Seçili olan log dosyasını download etmek üzere çalışır.
     * @return PF fileDownload için Stream
     */
    public StreamedContent getLogFile() {
        String logFileName = getLogFileName(this.logName);
        InputStream is;
        try {
            is = new FileInputStream(new File(logFileName));
            return new DefaultStreamedContent(is, "text/plain", logName +".log.txt");
        } catch (FileNotFoundException ex) {
            FacesMessages.warn("message.logfile.NotConfigured");
            LOG.error("LogFile Not Found", ex);
        }
        return null;
    }
}
