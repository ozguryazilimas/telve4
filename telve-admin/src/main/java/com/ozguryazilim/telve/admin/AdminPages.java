/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.admin;

import com.ozguryazilim.telve.auth.SecuredPage;
import com.ozguryazilim.telve.nav.AdminNavigationSection;
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
@Folder(name="/admin/idm")
public interface AdminPages extends Pages.Admin{
    @SecuredPage("user") @View @Navigation(icon = "/user.png", section = AdminNavigationSection.class, order = 20)
    class User implements Admin {};
    
    @SecuredPage("role") @View @Navigation(icon = "/role.png", section = AdminNavigationSection.class, order = 21)
    class Role implements Admin {};
    
    @SecuredPage @View 
    class PasswordOptionPane implements Admin {};
}
