package com.ozguryazilim.telve.query;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Named;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.api.scope.WindowScoped;

/**
 * Column Convertarda kulanmak için ColumnStore
 * 
 * Ayraç olarak label key'leri kullanıyor.
 * 
 * @author Hakan Uygun
 *
 */
@Named
@WindowScoped
public class ColumnStore implements Serializable{

	private Map<String, Column> columns = new HashMap<String, Column>(); 
	
	public String put( Column col ){
		columns.put(col.getLabelKey(), col);
		return col.getLabelKey();
	}
	
	public Column get( String key ){
		return columns.get(key);
	}
	
        /**
         * CDI Bean instance'i döner.
         * @return 
         */
	public static ColumnStore instance(){
            return BeanProvider.getContextualReference(ColumnStore.class, true);
	}
}
