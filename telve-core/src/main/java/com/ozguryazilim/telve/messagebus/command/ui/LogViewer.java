/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command.ui;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.view.Pages;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.scope.GroupedConversation;
import org.apache.deltaspike.core.api.scope.GroupedConversationScoped;

/**
 * Ayarlarda verilmiş olan log dosyasını göstermek için kullanılır.
 *
 * @author Hakan Uygun
 */
@Named
@GroupedConversationScoped
public class LogViewer implements Serializable {

    @Inject
    private GroupedConversation conversation;

    private List<String> logLines;

    /**
     * Regex için kullanılacak değer.
     */
    private String filter;

    /**
     * Read Filtred Log Lines
     *
     * @throws IOException
     */
    private void readFilteredLogLines() throws IOException {
        
        //Önce içini bir boşaltalım.
        logLines.clear();

        String logName = ConfigResolver.getProjectStageAwarePropertyValue("app.logFileName");
        
        if( Strings.isNullOrEmpty(logName)) return;
        
        File file = new File( logName );
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
}
