/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.admin;

import com.ozguryazilim.telve.auth.SecuredPage;
import com.ozguryazilim.telve.view.PageTitle;
import com.ozguryazilim.telve.view.Pages;
import javax.enterprise.context.ApplicationScoped;
import org.apache.deltaspike.jsf.api.config.view.View;

/**
 *
 * @author haky
 */
@ApplicationScoped
public interface AdminPages extends Pages.Admin{
    @SecuredPage @View @PageTitle("User Definition")
    class User implements Admin {};
    
    @SecuredPage @View @PageTitle("Role Definition")
    class Role implements Admin {};
}
