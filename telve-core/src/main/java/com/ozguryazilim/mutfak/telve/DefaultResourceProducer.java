/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.mutfak.telve;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;

/**
 * Telve uygulamaları içerisinde kullanılacak temel kaynakları üretir.
 *
 * @author Hakan Uygun
 */
@RequestScoped
public class DefaultResourceProducer {

    @Produces @Default
    @RequestScoped
    public FacesContext produceFacesContext() {
        return FacesContext.getCurrentInstance();
    }
}
