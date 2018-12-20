package com.ozguryazilim.telve.query;

import com.ozguryazilim.telve.entities.ViewModel;
import java.io.Serializable;

/**
 * View Model'ler için taban sınıf.
 * 
 * @author Hakan Uygun
 */
public class AbstractViewModel implements ViewModel, Serializable{

    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}
