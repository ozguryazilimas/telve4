/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve;

import java.io.Serializable;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.deltaspike.core.api.config.view.ViewConfig;

import com.ozguryazilim.telve.view.DialogBase;
import com.ozguryazilim.telve.view.Pages;

/**
 * About Dialogu için UI controller.
 * 
 * @author Hakan Uygun
 */
@SessionScoped
@Named
public class AboutController extends DialogBase implements Serializable {

	@Override
	public void decorateDialog(Map<String, Object> options) {
		options.put("modal", true);
		// options.put("draggable", false);
		options.put("resizable", false);
		options.put("contentHeight", 450);
	}

	@Override
	public void closeDialog() {
		closeDialogWindow();
	}

	@Override
	public Class<? extends ViewConfig> getDialogViewConfig() {
		return Pages.Layout.AboutPopup.class;
	}

}
