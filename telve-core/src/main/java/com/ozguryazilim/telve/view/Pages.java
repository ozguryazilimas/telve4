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

/**
 * Sistem genelinde kullanılacak page root'u.
 * @author Hakan Uygun
 */
@ApplicationScoped
@ViewConfigRoot
@Folder(name = "./")
public interface Pages {
    
    @SecuredPage @PageTitle("Ana Sayfa")
    class Home implements ViewConfig {};
    @PageTitle("Giriş Sayfası")
    class Login implements ViewConfig {};
    @PageTitle("Hata Sayfası")
    class Error extends DefaultErrorView {};
    @SecuredPage @PageTitle("Deneme")
    class Deneme implements ViewConfig {};
}
