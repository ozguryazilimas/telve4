package com.ozguryazilim.telve.forms;

import java.util.List;
import org.apache.deltaspike.core.api.config.view.ViewConfig;

/**
 * SubView'lerin runtime'da yetki dışında iş kurallarına göre de filtrelenmesi gerekebiliyor.
 * 
 * Bu filtre interface'ini implemente ederek ( dependent ) bu işlem yapılabilir.
 * 
 * 
 * 
 * @author Hakan Uygun
 */
public interface SubViewFilter {
    
    /**
     * Container ismine bakılarak gönderilen listeden istenilen veriler çıkarılabilir.
     * 
     * @param container container sınıfın view adı
     * @param views sunulacak olan viewId'ler
     */
    void filter( Class<? extends ViewConfig> container, List<String> views );
    
}
