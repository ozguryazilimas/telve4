/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.calendar.config;

import com.ozguryazilim.telve.auth.SecuredPage;
import com.ozguryazilim.telve.nav.MainNavigationSection;
import com.ozguryazilim.telve.nav.Navigation;
import com.ozguryazilim.telve.view.Pages;
import javax.enterprise.context.ApplicationScoped;
import org.apache.deltaspike.jsf.api.config.view.Folder;

/**
 *
 * @author oyas
 */
@ApplicationScoped
@Folder(name="/calendar")
public interface CalendarPages extends Pages{

    @SecuredPage
    @Navigation(icon = "fa fa-calendar", section = MainNavigationSection.class)
    class CalendarConsole implements CalendarPages {
    };

    @SecuredPage
    class CalendarReminderCommand implements CalendarPages {
    };

    @SecuredPage
    class SimpleEventDialog implements CalendarPages {
    };
}
