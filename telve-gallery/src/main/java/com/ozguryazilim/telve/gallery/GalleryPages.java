/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.gallery;

import com.ozguryazilim.telve.auth.SecuredPage;
import com.ozguryazilim.telve.nav.AdminNavigationSection;
import com.ozguryazilim.telve.nav.Navigation;
import com.ozguryazilim.telve.view.Pages;
import javax.enterprise.context.ApplicationScoped;
import org.apache.deltaspike.jsf.api.config.view.Folder;
import org.apache.deltaspike.jsf.api.config.view.View;

/**
 * Gallery için View Tanımları
 * @author Hakan Uygun
 */
@ApplicationScoped
@Folder(name="/gallery")
public interface GalleryPages extends Pages{
    
    @SecuredPage() @View @Navigation( icon = "fa fa-image", section = AdminNavigationSection.class)
    class GalleryConsole implements GalleryPages {};
}
