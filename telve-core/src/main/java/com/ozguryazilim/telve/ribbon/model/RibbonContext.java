package com.ozguryazilim.telve.ribbon.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Context tab'ı için tanımlı ribbon bilgileri model sınıfı
 * 
 *  anahtar viewId bilgisi. 
 * 
 * @author Hakan Uygun
 *
 */
public class RibbonContext implements Serializable{

	private static final long serialVersionUID = 1L;

	private String view;
	private List<RibbonSection> sections = new ArrayList<RibbonSection>();
	
	public String getView() {
		return view;
	}
	public void setView(String view) {
		this.view = view;
	}
	
	public List<RibbonSection> getSections() {
		return sections;
	}
	public void setSections(List<RibbonSection> sections) {
		this.sections = sections;
	}
	
	
	
}
