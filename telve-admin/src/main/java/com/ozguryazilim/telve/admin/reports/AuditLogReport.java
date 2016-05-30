/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.admin.reports;

import com.ozguryazilim.telve.admin.AdminReportPages;
import com.ozguryazilim.telve.reports.JasperReportBase;
import com.ozguryazilim.telve.reports.Report;
import java.util.Map;

/**
 * AuditLog'lar için standart rapor
 * 
 * @author Hakan Uygun
 */
@Report( filterPage = AdminReportPages.AuditLogReport.class, permission="auditLogReport", path="/admin/audit", template = "auditLogReport", resource = "adminReports")
public class AuditLogReport extends JasperReportBase{

    @Override
    protected boolean buildParam(Map<String, Object> params) {
        return true;
    }
    
    
}
