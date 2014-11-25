/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.batch;

import com.ozguryazilim.telve.view.Pages;
import javax.enterprise.context.ApplicationScoped;
import org.apache.deltaspike.jsf.api.config.view.Folder;

/**
 * Telve Batch Sayfa tanımları
 * @author Hakan Uygun
 */
@ApplicationScoped
@Folder(name="/batch")
public interface BatchPages extends Pages{
    
    //@SecuredPage @View @PageTitle("User Definition")
    //class User implements Admin {};
}
