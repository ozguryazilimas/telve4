/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.user;

import com.ozguryazilim.telve.feature.AbstractFeatureHandler;
import com.ozguryazilim.telve.feature.Feature;
import com.ozguryazilim.telve.feature.Page;
import com.ozguryazilim.telve.feature.PageType;
import com.ozguryazilim.telve.idm.config.IdmPages;
import com.ozguryazilim.telve.idm.entities.User;

/**
 *
 * @author oyas
 */
@Feature(caption = "module.caption.User", permission = "user", forEntity = User.class)
@Page(type = PageType.BROWSE, page = IdmPages.UserBrowse.class)
@Page(type = PageType.EDIT, page = IdmPages.User.class)
@Page(type = PageType.VIEW, page = IdmPages.UserView.class)
@Page(type = PageType.MASTER_VIEW, page = IdmPages.UserMasterView.class)
public class UserFeature extends AbstractFeatureHandler{
    
}
