/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve;

import javax.enterprise.context.ApplicationScoped;
import org.apache.deltaspike.core.api.config.PropertyFileConfig;

/**
 * Telve temel konfigürasyon dosya adı
 * @author Hakan Uygun
 */
@ApplicationScoped
public class TelvePropertyFileConfig implements PropertyFileConfig{

    @Override
    public String getPropertyFileName() {
        return "telve.properties";
    }
    
}
