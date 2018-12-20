package com.ozguryazilim.telve.reports;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.jasperreports.engine.JRDataSource;

/**
 * DynamicReports için Subreport tanım alt yapısı.
 * 
 * @author Hakan Uygun
 */
public abstract class DynamicSubreportBase extends AbstractSubreportBase{
   
   
    /**
     * Geriye hazırlanmış olan Subreport döndürülür.
     * 
     * @param reportParameters
     * @return 
     */
    public abstract JasperReportBuilder getSubreport( ReportParameters reportParameters );
    
    /**
     * Geriye subreport'un kullanacağı datasource döndürürlür.
     * 
     * @param reportParameters
     * @return 
     */
    public abstract JRDataSource getDataSource( ReportParameters reportParameters );
    
}
