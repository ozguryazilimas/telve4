package com.ozguryazilim.telve.audit;

import com.ozguryazilim.telve.entities.AuditLog;
import com.ozguryazilim.telve.feature.AbstractFeatureHandler;
import com.ozguryazilim.telve.feature.Feature;
import com.ozguryazilim.telve.feature.Page;
import com.ozguryazilim.telve.feature.PageType;
import com.ozguryazilim.telve.view.Pages;

/**
 *
 * @author oyas
 */
@Feature(caption = "module.caption.AuditLog", permission = "auditLog", forEntity = AuditLog.class )
@Page(type = PageType.BROWSE, page = Pages.Admin.AuditLogBrowse.class)
public class AuditLogFeature extends AbstractFeatureHandler{
    
}
