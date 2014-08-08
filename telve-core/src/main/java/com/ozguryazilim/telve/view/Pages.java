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
    
    @SecuredPage
    class Home implements ViewConfig {};
    class Login implements ViewConfig {};
    class Error extends DefaultErrorView {};
    @SecuredPage
    class Deneme implements ViewConfig {};
}
