package com.ozguryazilim.telve.ribbon.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * RibbonTab içerisinde bulunan sectionların verilerini tutar
 * 
 * @author Hakan Uygun
 *
 */
public class RibbonSection implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Her section'ın bir unique id'si olmalı.
	 */
	private String id;
	/**
	 * GUI'de gösterilecek section title2ı messages'dan alınır
	 */
	private String label;
	/**
	 * Seçtion'ın sağa mı sola mı dayalı olduğu left|right değerleri alır
	 */
	private String alignment = "left";
	
	/**
	 * Sectionların öncelik sırası 
	 */
	private int order = 100;
	
	/**
	 * Yetkilendirme
	 */
	private String permissionDomain;
	
	private String permissionAction;
	
	/**
	 * Section içerisinde tanımlı olan action listesi
	 */
	private List<RibbonSectionAction> actions = new ArrayList<RibbonSectionAction>();
	
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
	
	
	public String getAlignment() {
		return alignment;
	}
	public void setAlignment(String alignment) {
		this.alignment = alignment;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public List<RibbonSectionAction> getActions() {
		return actions;
	}
	public void setActions(List<RibbonSectionAction> actions) {
		this.actions = actions;
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

    @Override
    public String toString() {
        return "RibbonSection{" + "id=" + id + ", label=" + label + ", alignment=" + alignment + ", order=" + order + ", permissionDomain=" + permissionDomain + ", permissionAction=" + permissionAction + ", actions=" + actions + '}';
    }
	
	
        
	
	
	
}
