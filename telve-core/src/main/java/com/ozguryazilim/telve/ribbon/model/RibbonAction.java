package com.ozguryazilim.telve.ribbon.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Ribbon bar üzerinde kullanıalcak olan action butonlar için model verisi tutar
 * 
 * @author Hakan  Uygun
 *
 */
public class RibbonAction implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Her action'ın unique bir id'si olması lazım. Böylece refereans verilebilirler 
	 */
	private String id;
	/**
	 * GUI'de kullanılacak olan caption. messages içerisinde çağrılırlar.
	 */
	private String label;
	/**
	 * Ek açıklama bilgisi label ile aynı kurallar geçerlidir.
	 */
	private String hint;
	/**
	 * GUI'de kullanılacak olan imaj ismi. 32 ya da 16 kullanılabilirler. Dolayısı ile iki karşılığı da olması lazım
	 */
	private String img;
	/**
	 * Çağrılacak olan JSF view'u ya da URL. Type'a göre değişir
	 */
	private String url;
	
	/**
	 * Bu actionın edit için kullanacağı view linki.
	 * 
	 * Type ile çok ilişkili : browse için classifer new geldiğinde kullanılır.
	 */
	private String editUrl;
	
	/**
	 * Yetki kontrolü için kullanılacak olan domain
	 * 
	 * domain:action şeklinde verilebilir. eğer sadee domain verildiyse domain:select olarak yorumlanacaktır.
	 */
	private String permissionDomain;
	
	private String permissionAction;

	/**
	 * Action tipi. param|browse|edit|method|dropdown değerlerini alabilir.
	 */
	private String type;
	
        /**
	 * Kullanılacak olan URL'e geçilecek olan parametreler eklenir. Rendering sırasında bunlara SectionAction'dan gelenler de eklenir.
	 */
        private Map<String,String> params = new HashMap<String, String>();
        
        /**
         * Render sırasında kullanılabilecek hintler tanımlanır
         */
        private Map<String,String> hints = new HashMap<String, String>();
	
	/**
	 * Varsayılan şablonlardan farklı bir şey kullanılacaksa onun template bilgisi.
	 * FIXME: Bundan emin değilim.
	 */
	private String template;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getPermissionDomain() {
		return permissionDomain;
	}
	public void setPermissionDomain(String permissionDomain) {
		this.permissionDomain = permissionDomain;
	}
	
	public String getPermissionAction() {
		return permissionAction;
	}
	public void setPermissionAction(String permissionAction) {
		this.permissionAction = permissionAction;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getEditUrl() {
		return editUrl;
	}
	public void setEditUrl(String editUrl) {
		this.editUrl = editUrl;
	}

        public Map<String, String> getParams() {
            return params;
        }

        public void setParams(Map<String, String> params) {
            this.params = params;
        }

        public Map<String, String> getHints() {
            return hints;
        }

        public void setHints(Map<String, String> hints) {
            this.hints = hints;
        }

        
        /**
         * Eğer type dropdown ise url'de tanımlı şablonu değilse empty.xhtml şablonunu döndürür.
         * @return 
         */
        public String dropdownTemplate(){
            if( "dropdown".equals(getType())){
                return getUrl();
            }
            return "/layout/empty.xhtml";
        }

    @Override
    public String toString() {
        return "RibbonAction{" + "id=" + id + ", label=" + label + ", hint=" + hint + ", img=" + img + ", url=" + url + ", editUrl=" + editUrl + ", permissionDomain=" + permissionDomain + ", permissionAction=" + permissionAction + ", type=" + type + ", params=" + params + ", hints=" + hints + ", template=" + template + '}';
    }

    

        

}
