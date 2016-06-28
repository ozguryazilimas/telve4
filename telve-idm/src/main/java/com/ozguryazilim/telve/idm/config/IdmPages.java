/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.config;

import com.ozguryazilim.telve.auth.SecuredPage;
import com.ozguryazilim.telve.nav.AdminNavigationSection;
import com.ozguryazilim.telve.nav.Navigation;
import com.ozguryazilim.telve.view.Pages;
import javax.enterprise.context.ApplicationScoped;
import org.apache.deltaspike.jsf.api.config.view.Folder;
import org.apache.deltaspike.jsf.api.config.view.View;

/**
 * IDM sayfa tanımları
 * 
 * @author Hakan Uygun
 */
@ApplicationScoped
@Folder(name="/admin/idm")
public interface IdmPages extends Pages.Admin{
    @SecuredPage("user") @View 
    class User implements Admin {};
    
    @SecuredPage("user") @View @Navigation(icon = "fa fa-users", section = AdminNavigationSection.class, order = 20)
    class UserBrowse implements Admin {};
    
    @SecuredPage("user") @View 
    class UserView implements Admin {};
    
    @SecuredPage("user") @View 
    class UserMasterView implements Admin {};
    
    @SecuredPage("userRole") @View 
    class UserRoleSubView implements Admin {};
    
    @SecuredPage("role") @View @Navigation(icon = "fa fa-slideshare", section = AdminNavigationSection.class, order = 22)
    class Role implements Admin {};
    
    @SecuredPage() @View 
    class RoleLookup implements Admin {};
    
    @SecuredPage("userRole") @View @Navigation(icon = "fa fa-street-view", section = AdminNavigationSection.class, order = 21)
    class UserRole implements Admin {};
    
    @SecuredPage @View 
    class PasswordOptionPane implements Admin {};
}
