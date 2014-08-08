package com.ozguryazilim.telve.ribbon.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * RibbonBar üzerinde çıkacak olan tab bilgilerini tanımlar.
 * 
 * @author Hakan Uygun
 *
 */
public class RibbonTab implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Her tab'ın unique bir id'si olması lazım ki refere edilebilsinler
	 */
	private String id;
	/**
	 * GUI'de gösterilecek olan caption. messages içersiinde çağrılacak
	 */
	private String label;
	/**
	 * Tab grubunun gösterilmesi için gereken yetki domaini
	 */
	private String permissionDomain;
	
	private String permissionAction;
	
	/**
	 * Başka bir ribbonTab'ı extend ediyorsa onun id'si. miras aldığı değerleri override eder.
	 */
	private String extend;
	/**
	 * Gösterim sırasında sıralam bilgisi. daha düşük değerli önce çıkar.
	 */
	private int order = 100;
	
	/**
	 * Tab içerisinde tanımlı olan sectionlar
	 */
	private List<RibbonSection> sections = new ArrayList<RibbonSection>();
	
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
	public String getExtend() {
		return extend;
	}
	public void setExtend(String extend) {
		this.extend = extend;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public List<RibbonSection> getSections() {
		return sections;
	}
	public void setSections(List<RibbonSection> sections) {
		this.sections = sections;
	}

    @Override
    public String toString() {
        return "RibbonTab{" + "id=" + id + ", label=" + label + '}';
    }

    
	
	
}
