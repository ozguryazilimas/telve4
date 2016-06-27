/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.admin;

import com.ozguryazilim.telve.auth.SecuredPage;
import com.ozguryazilim.telve.view.PageTitle;
import com.ozguryazilim.telve.view.Pages;
import javax.enterprise.context.ApplicationScoped;
import org.apache.deltaspike.jsf.api.config.view.Folder;
import org.apache.deltaspike.jsf.api.config.view.View;

/**
 * Admin Raporları 
 * @author Hakan Uygun
 */
@ApplicationScoped
@Folder(name="/admin/reports")
public interface AdminReportPages extends Pages.Admin{
    
    @SecuredPage
    @View @PageTitle("report.name.auditLogReport")
    class AuditLogReport implements AdminReportPages {}
}
