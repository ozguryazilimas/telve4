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

package com.ozguryazilim.telve.adminreport;

import com.ozguryazilim.telve.adminreport.AuditLogFilter;
import com.ozguryazilim.telve.audit.AuditLogRepository;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;

import org.apache.deltaspike.data.api.Query;
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
    
    @Inject
    private AuditLogRepository repository;
    
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
        filter.setUser("");
        filter.setDomain("");
        filter.setAction("");
        filter.setCategory("");
    }

    @Override
    protected boolean buildParam(Map<String, Object> params) {
    	params.put("BEGIN_DATE", getFilter().getBeginDate());
        params.put("END_DATE", getFilter().getEndDate());
        params.put("USER", getFilter().getUser());
        params.put("DOMAIN", getFilter().getDomain());
        params.put("CATEGORY", getFilter().getCategory());
        params.put("ACTION", getFilter().getAction());
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
        params.put(JRParameter.REPORT_RESOURCE_BUNDLE, TelveResourceBundle.getBundle());
        super.decorateI18NParams(params); //To change body of generated methods, choose Tools | Templates.           
    }
    
    
    public List<String> getDomains() {
    	List<String> domains = repository.findDistinctDomains();
    	return domains;
    }
    
    public List<String> getActions() {
    	List<String> actions = repository.findDistinctActions();
    	return actions;
    }
    
    public List<String> getCategories() {
    	List<String> categories = repository.findDistinctCategories();
    	return categories;
    }

    
}
