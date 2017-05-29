package com.ozguryazilim.telve.jcr;

import org.apache.deltaspike.jsf.api.config.view.Folder;
import org.apache.deltaspike.jsf.api.config.view.View;

import com.ozguryazilim.telve.view.Pages;


@Folder(name="./")
public interface JcrPages extends Pages {

	@Folder
	interface Dialogs extends JcrPages {
		
		@View
		class ImageUploadDialog implements JcrPages {}
	}
	
}
