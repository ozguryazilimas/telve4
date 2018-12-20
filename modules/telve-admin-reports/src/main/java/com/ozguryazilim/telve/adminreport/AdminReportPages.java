package com.ozguryazilim.telve.adminreport;

import com.ozguryazilim.telve.auth.SecuredPage;
import com.ozguryazilim.telve.view.PageTitle;
import com.ozguryazilim.telve.view.Pages;
import javax.enterprise.context.ApplicationScoped;
import org.apache.deltaspike.jsf.api.config.view.Folder;
import org.apache.deltaspike.jsf.api.config.view.View;

/**
 *
 * @author Hakan Uygun
 */
@ApplicationScoped
@Folder(name = "/admin/reports")
public interface AdminReportPages extends Pages.Admin {

    @SecuredPage
    @View
    @PageTitle("report.name.auditLogReport")
    class AuditLogReport implements AdminReportPages {}

}
