package com.ozguryazilim.telve.ribbon.model;

import java.io.Serializable;

/**
 * RibbonRegistery sırasında resource bilgilerini saklamak ve daha önce işlenip işlenmediğini tutmak için kullanılır.
 * @author Hakan Uygun
 */

public class RibbonResourceModel implements Serializable{

	private static final long serialVersionUID = 1L;

	private String fileDescriptor;
	private boolean handled = false;
	
	public RibbonResourceModel(String fileDescriptor) {
		super();
		this.fileDescriptor = fileDescriptor;
	}
	
	public String getFileDescriptor() {
		return fileDescriptor;
	}
	public void setFileDescriptor(String fileDescriptor) {
		this.fileDescriptor = fileDescriptor;
	}
	public boolean isHandled() {
		return handled;
	}
	public void setHandled(boolean handled) {
		this.handled = handled;
	}

}
