/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
@Feature(caption = "module.caption.AuditLog", icon = "", permission = "auditLog", forEntity = AuditLog.class )
@Page(type = PageType.BROWSE, page = Pages.Admin.AuditLogBrowse.class)
public class AuditLogFeature extends AbstractFeatureHandler{
    
}
