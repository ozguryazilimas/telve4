/*
 *   Copyleft 2013 IOVA SOFTWARE
 *   
 *  Distributable under LGPL license.
 *  See terms of license at gnu.org.
 *  http://www.gnu.org/licenses/lgpl.html
 *   
 *  www.openohs.com
 *  www.iova.com.tr
 */

package com.ozguryazilim.telve.audit;

import com.ozguryazilim.telve.audit.AuditLogFilter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.joda.time.DateTime;


import com.ozguryazilim.telve.config.TelveConfigResolver;
import com.ozguryazilim.telve.messages.TelveResourceBundle;
import com.ozguryazilim.telve.query.filters.DateValueType;
import com.ozguryazilim.telve.reports.JasperReportBase;
import com.ozguryazilim.telve.reports.Report;
import com.ozguryazilim.telve.reports.ReportDate;
import com.ozguryazilim.telve.view.Pages;
import net.sf.jasperreports.engine.JRParameter;

/**
 * AuditLog'lar için standart rapor
 * 
 * @author Hakan Uygun
 */
@Report( filterPage = Pages.Admin.AdminReportPages.AuditLogReport.class, permission="auditLogReport", path="/admin/audit", template = "auditLogReport", resource = "adminReports")
public class AuditLogReport extends JasperReportBase{

    @Inject
    private TelveConfigResolver telveConfigResolver;
    
    private AuditLogFilter filter;
    
    public AuditLogFilter getFilter() {
	if (filter == null) {
            buildFilter();
        }
        return filter;
    }

    public void setFilter(AuditLogFilter filter) {
        this.filter = filter;
    }

    public void buildFilter() {
        filter = new AuditLogFilter();

        DateTime dt = new DateTime();

        filter.setEndDate(new ReportDate(DateValueType.Today));
        filter.setBeginDate(new ReportDate(DateValueType.Yesterday));
    }

    @Override
    protected boolean buildParam(Map<String, Object> params) {
	params.put("BEGIN_DATE", getFilter().getBeginDate());
        params.put("END_DATE", getFilter().getEndDate());
        
        String logo = telveConfigResolver.getProperty("brand.company.reportLogo");
        String title = telveConfigResolver.getProperty("brand.company.reportTitle");

        if (logo != null) {
            BufferedImage image;
	    try {
		image = ImageIO.read(getClass().getResource("/META-INF/resources/brand/img/".concat(logo)));
		params.put("FIRM_LOGO", image);
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
        }
        params.put("FIRM_TITLE", title);
        
        return true;
    }

    @Override
    protected void decorateI18NParams(Map<String, Object> params) {
        super.decorateI18NParams(params); //To change body of generated methods, choose Tools | Templates.
        
        params.put(JRParameter.REPORT_RESOURCE_BUNDLE, TelveResourceBundle.getBundle());

    }
    

    
}
