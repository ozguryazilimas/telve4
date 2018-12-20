package com.ozguryazilim.telve.reports.config;

import com.ozguryazilim.telve.auth.SecuredPage;
import com.ozguryazilim.telve.nav.MainNavigationSection;
import com.ozguryazilim.telve.nav.Navigation;
import com.ozguryazilim.telve.view.Pages;
import javax.enterprise.context.ApplicationScoped;
import org.apache.deltaspike.jsf.api.config.view.Folder;
import org.apache.deltaspike.jsf.api.config.view.View;

/**
 * Reports için view ve naviagtion tanımları.
 * @author Hakan Uygun
 */
@ApplicationScoped
@Folder(name = "./reports")
public interface ReportPages extends Pages {

    @SecuredPage
    @View
    @Navigation(icon = "fa fa-print", section = MainNavigationSection.class, order = 100)
    class ReportConsole implements ReportPages {
    };

    @SecuredPage
    @View
    //Navigasyon yok. ReportConsole contextine gidecek
    class ScheduledReportConsole implements ReportPages {
    };

}
