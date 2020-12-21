package com.ozguryazilim.telve.bpm;

import com.ozguryazilim.telve.auth.SecuredPage;
import com.ozguryazilim.telve.nav.AdminNavigationSection;
import com.ozguryazilim.telve.nav.MainNavigationSection;
import com.ozguryazilim.telve.nav.Navigation;
import com.ozguryazilim.telve.view.Pages;
import javax.enterprise.context.ApplicationScoped;
import org.apache.deltaspike.jsf.api.config.view.Folder;
import org.apache.deltaspike.jsf.api.config.view.View;

/**
 *
 * @author haky
 */
@ApplicationScoped
@Folder(name="/bpm")
public interface TaskPages extends Pages{
    
    @SecuredPage("taskConsole") @View @Navigation( icon = "fa fa-tasks", section = MainNavigationSection.class)
    class TaskConsole implements TaskPages {};
    
    @SecuredPage("processBrowse") @View @Navigation( icon = "fa fa-tasks", section = AdminNavigationSection.class)
    class ProcessBrowse implements TaskPages {};
}
