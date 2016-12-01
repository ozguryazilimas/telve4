/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.view;

import com.ozguryazilim.telve.auth.SecuredPage;
import com.ozguryazilim.telve.nav.AdminNavigationSection;
import com.ozguryazilim.telve.nav.MainNavigationSection;
import com.ozguryazilim.telve.nav.Navigation;
import javax.enterprise.context.ApplicationScoped;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.spi.config.view.ViewConfigRoot;
import org.apache.deltaspike.jsf.api.config.view.Folder;
import org.apache.deltaspike.jsf.api.config.view.View;

/**
 * Sistem genelinde kullanılacak page root'u.
 *
 * @author Hakan Uygun
 */
@ApplicationScoped
@ViewConfigRoot
@Folder(name = "./")
@View(navigation = View.NavigationMode.REDIRECT, viewParams = View.ViewParameterMode.INCLUDE)
public interface Pages extends ViewConfig {

    @SecuredPage
    @Navigation(icon = "fa fa-dashboard", section = MainNavigationSection.class, order = 0)
    class Home implements Pages {
    };

    class Login implements Pages {
    };
    //@PageTitle("Hata Sayfası")
    //class Error extends DefaultErrorView {};

    @SecuredPage
    class Options implements Pages {
    };

    @SecuredPage
    interface Admin extends Pages {

        @SecuredPage("scheduledCommand")
        @Navigation(icon = "fa fa-gears", section = AdminNavigationSection.class)
        class ScheduledCommandBrowse implements Admin {
        };

        @SecuredPage("executionLog")
        @Navigation(icon = "fa fa-recycle", section = AdminNavigationSection.class)
        class ExecutionLogBrowse implements Admin {
        };

        @SecuredPage("suggestion")
        @Navigation(icon = "fa fa-list", section = AdminNavigationSection.class)
        class SuggestionBrowse implements Admin {
        };

        @SecuredPage("auditLog")
        @Navigation(icon = "fa fa-history", section = AdminNavigationSection.class)
        class AuditLogBrowse implements Admin {
        };
        
        @SecuredPage()
        @View
        class AuditLogClearCommand implements Admin {
        };

        @Folder(name = "/admin/reports")
        public interface AdminReportPages extends Pages.Admin {

            @SecuredPage
            @View
            @PageTitle("report.name.auditLogReport")
            class AuditLogReport implements AdminReportPages {
            }
        }

        class AllOptionsPane implements Admin {
        };

        class GuiOptionPane implements Admin {
        };
    }

    @SecuredPage
    interface Reports extends Pages {

        @Navigation(icon = "fa fa-print", section = MainNavigationSection.class, order = 100)
        class ReportConsole implements Reports {
        };

        //Navigasyon yok. ReportConsole contextine gidecek
        class ScheduledReportConsole implements Reports {
        };
    }

    @SecuredPage
    interface Calendar extends Pages {

        @Navigation(icon = "fa fa-calendar", section = MainNavigationSection.class)
        class CalendarConsole implements Calendar {
        };

        class CalendarReminderCommand implements Calendar {
        };

        class SimpleEventDialog implements Calendar {
        };
    }
}
