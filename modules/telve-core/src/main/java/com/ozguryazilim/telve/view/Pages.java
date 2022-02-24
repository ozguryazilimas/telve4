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
    
    class ChangePassword implements Pages {
    };
    
    @SecuredPage
    class FeatureLookup implements Pages {};

    @SecuredPage
    @View
    class CleanupExpiredNotifiesCommand implements Admin {
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

        class AllOptionsPane implements Admin {
        };

        class GuiOptionPane implements Admin {
        };
        
        class NavigationOptionPane implements Admin {
        };
        
        @SecuredPage("SystemOptions")
        @View
        @Navigation(icon = "fa fa-gears", section = AdminNavigationSection.class)
        class SystemOptions implements Admin {
        };

    }

}
