/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.config;

import javax.enterprise.context.ApplicationScoped;

import org.apache.deltaspike.jsf.api.config.view.Folder;
import org.apache.deltaspike.jsf.api.config.view.View;

import com.ozguryazilim.telve.auth.SecuredPage;
import com.ozguryazilim.telve.view.PageTitle;
import com.ozguryazilim.telve.view.Pages;

/**
 * Rapor filtre sayfalarının tanılarını belirler.
 *
 * @author Aydoğan Sel <aydogan.sel at iova.com.tr>
 */
@ApplicationScoped
@Folder(name = "/admin/idm/reports")
public interface IdmReportPages extends Pages.Admin {

    @SecuredPage
    @View
    @PageTitle("report.name.userRoleReport")
    class UserRoleReport implements IdmReportPages {
    }

}
