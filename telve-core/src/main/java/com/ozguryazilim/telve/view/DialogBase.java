package com.ozguryazilim.telve.view;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.primefaces.context.RequestContext;

/**
 * Dialog classları için base class.
 * @author muhammedf
 *
 */
public abstract class DialogBase {
	
	@Inject
	private ViewConfigResolver viewConfigResolver;

	/**
	 * Dialog açılmadan evvel yapılacak işler.
	 */
	public void beforeOpenDialog() {

	}

	/**
	 * Dialog penceresini açar.
	 */
	public void openDialog() {
		beforeOpenDialog();
		Map<String, Object> options = new HashMap<>();
		decorateDialog(options);
		RequestContext.getCurrentInstance().openDialog(getDialogName(), options, null);
	}

	/**
	 * Dialog opsiyonlarını ayarlamak için.
	 * @param options
	 */
	protected void decorateDialog(Map<String, Object> options) {
		options.put("modal", true);
		options.put("resizable", false);
		options.put("width", "780");
		options.put("height", "430");
		options.put("contentHeight", 450);
		options.put("position", "center top");
	}

	/**
	 * Dialog penceresini kapatır. 
	 * {@link #closeDialog()} ve {@link #cancelDialog()} içerisinden çağrılacak.
	 */
	protected void closeDialogWindow() {
		RequestContext.getCurrentInstance().closeDialog(null);
	}

	
	/**
	 * Değişiklikleri onayla.
	 */
	public abstract void closeDialog();

	/**
     * Bir şey yapmadan çık.
     */
	public void cancelDialog() {
		closeDialogWindow();
	}

	/**
	 * {@link #openDialog()} için sayfa ismi döndürür. (dizinle beraber)
	 * @return
	 */
	public String getDialogName() {
		String viewId = viewConfigResolver.getViewConfigDescriptor(getDialogViewConfig()).getViewId();
		return viewId.substring(0, viewId.indexOf(".xhtml"));
	}

	/**
	 * Dialog içerisinde açılacak olan sayfayı döndürür.
	 * @return
	 */
	public abstract Class<? extends ViewConfig> getDialogViewConfig();
}
