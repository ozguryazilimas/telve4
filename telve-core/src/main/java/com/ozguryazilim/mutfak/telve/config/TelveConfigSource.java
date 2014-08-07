/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.mutfak.telve.config;

import java.util.Collections;
import java.util.Map;
import org.apache.deltaspike.core.spi.config.ConfigSource;

/**
 * Telve'nin veri tabanından configürasyon bilgilerini çeker.
 * @author Hakan Uygun
 */
public class TelveConfigSource implements ConfigSource{

        
    @Override
    public int getOrdinal() {
        return 150;
    }

    @Override
    public Map<String, String> getProperties() {
        TelveConfigReporsitory r = TelveConfigReporsitory.instance();
        return r == null ? Collections.EMPTY_MAP : r.getProperties();
    }

    @Override
    public String getPropertyValue(String key) {
        TelveConfigReporsitory r = TelveConfigReporsitory.instance();
        return r == null ? null : r.getProperty(key);
    }

    @Override
    public String getConfigName() {
        return "TelveConfigSource";
    }

    @Override
    public boolean isScannable() {
        return false;
    }
    
}
