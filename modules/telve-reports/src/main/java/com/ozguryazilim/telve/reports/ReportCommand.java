package com.ozguryazilim.telve.reports;

import com.ozguryazilim.telve.messagebus.command.AbstractCommand;
import java.util.Map;

/**
 * Arka planda çalıştırılacak olan raporlar için Command
 * @author Hakan Uygun
 */
public class ReportCommand extends AbstractCommand{
    
    
    /**
     * Report name
     */
    private String name;
    
    /**
     * Requested User name
     */
    private String user;
    
    /**
     * Report Paramaters
     */
    private Map<String,Object> reportParams;
    
    /**
     * Report Template resource name
     */
    private String templateName;
    
    /**
     * Report result file name
     */
    private String resultName;
    
    /**
     * i18n resource name
     */
    private String bundleName;
    
    /**
     * i18n locale name ( tr, en etc )
     */
    private String locale;
    
    /**
     * Result send to emails.
     */
    private String emails;

    /**
     * Extra information for scheduled report
     */
    private String info;
    
    /**
     * Zamanlanmış raporun hangi engine ile çalışacağı
     */
    private String engine;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Map<String, Object> getReportParams() {
        return reportParams;
    }

    public void setReportParams(Map<String, Object> reportParams) {
        this.reportParams = reportParams;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getResultName() {
        return resultName;
    }

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

    public String getBundleName() {
        return bundleName;
    }

    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }
    
    
}
