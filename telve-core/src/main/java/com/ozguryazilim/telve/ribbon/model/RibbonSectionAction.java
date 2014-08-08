package com.ozguryazilim.telve.ribbon.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Bir section içerisinde action'a olan referans bilgisini tutar.
 * 
 * @author Hakan Uygun
 *
 */
public class RibbonSectionAction implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Ribbon action'a referans
	 */
	private RibbonAction action;
	/**
	 * Küçük mü büyük mü görüneceği
	 */
	private String type = "large";
	
	/**
	 * Kendine bağlı action'ın ne tür davranacağı. Browse'mu yoksa new'u olacak. 
	 * 
	 * Şimdilik normal|new olabiliyor. İleride artabilir.
	 * 
	 */
	private String classifier = "normal";
	
        /**
         * Standart action'ın tanım yapılırken ek parametreye kavuşması için 
         * Çoğunlukla farklı clasifier'larla birlikte kullanılır. Örneğin new için...
         */
        private Map<String,String> params = new HashMap<String, String>();
        
	public RibbonAction getAction() {
		return action;
	}
	public void setAction(RibbonAction action) {
		this.action = action;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getClassifier() {
		return classifier;
	}
	public void setClassifier(String classifier) {
		this.classifier = classifier;
	}

        public Map<String, String> getParams() {
            return params;
        }

        public void setParams(Map<String, String> params) {
            this.params = params;
        }

    @Override
    public String toString() {
        return "RibbonSectionAction{" + "action=" + action + ", type=" + type + ", classifier=" + classifier + ", params=" + params + '}';
    }

        
}
