package com.ozguryazilim.telve.nav;

import java.util.Objects;

/**
 * Navigation Sectionlar için abstract sınıf.
 * @author Hakan Uygun
 */

public abstract class AbstractNavigationSection implements NavigationSection{

    @Override
    public String getLabel() {
        return "nav.sec.label." + this.getClass().getSimpleName();
    }
    
    @Override
    public Integer getOrder() {
        return 50;
    }

    
    @Override
    public int compareTo(NavigationSection o) {
        return getOrder().compareTo(o.getOrder());
    }

    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(getLabel());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NavigationSection other = (NavigationSection) obj;
        if (!Objects.equals(this.getLabel(), other.getLabel())) {
            return false;
        }
        return true;
    }

    
    
    
}
