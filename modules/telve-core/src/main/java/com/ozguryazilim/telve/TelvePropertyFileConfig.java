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

    @Override
    public boolean isOptional() {
        return true;
    }
}
