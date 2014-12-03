/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.view;

import com.ozguryazilim.telve.auth.SecuredPage;
import javax.enterprise.context.ApplicationScoped;
import org.apache.deltaspike.core.api.config.view.DefaultErrorView;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.spi.config.view.ViewConfigRoot;
import org.apache.deltaspike.jsf.api.config.view.Folder;
import org.apache.deltaspike.jsf.api.config.view.View;

/**
 * Sistem genelinde kullanılacak page root'u.
 * @author Hakan Uygun
 */
@ApplicationScoped
@ViewConfigRoot
@Folder(name = "./") @View(navigation = View.NavigationMode.REDIRECT, viewParams = View.ViewParameterMode.INCLUDE)
public interface Pages extends ViewConfig {
    
    @SecuredPage @PageTitle("Ana Sayfa")
    class Home implements Pages {};
    @PageTitle("Giriş Sayfası")
    class Login implements Pages {};
    @PageTitle("Hata Sayfası")
    class Error extends DefaultErrorView {};
    @SecuredPage @PageTitle("Deneme")
    class Deneme implements Pages {};
    
    @SecuredPage @PageTitle("Ayarlar")
    class Options implements Pages {};
    
    @SecuredPage
    interface Admin extends Pages{
        
        @PageTitle("Stored Command Browse")
        class StoredCommandBrowse implements Admin {};
        
        @PageTitle("Scheduled Command Browse")
        class ScheduledCommandBrowse implements Admin {};
        
        @PageTitle("Execution Log Browse")
        class ExecutionLogBrowse implements Admin {};
        
        @PageTitle("ExecutionLogClearCommand")
        class ExecutionLogClearCommand implements Admin {};
        
        @PageTitle("Suggestion Browse")
        class SuggestionBrowse implements Admin {};
        
        @PageTitle("All Options")
        class AllOptionsPane implements Admin {};
        
        @PageTitle("GUI Options")
        class GuiOptionPane implements Admin {};
    }
    
    @SecuredPage
    interface Reports extends Pages{
        @PageTitle("Suggestion Browse")
        class ReportConsole implements Reports {};
    }
}
