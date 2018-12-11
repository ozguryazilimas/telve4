/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.suggestion;

import com.ozguryazilim.telve.entities.SuggestionItem;
import com.ozguryazilim.telve.feature.AbstractFeatureHandler;
import com.ozguryazilim.telve.feature.Feature;
import com.ozguryazilim.telve.feature.Page;
import com.ozguryazilim.telve.feature.PageType;
import com.ozguryazilim.telve.view.Pages;

/**
 *
 * @author oyas
 */
@Feature(caption = "module.caption.Suggestion", permission = "suggestion", forEntity = SuggestionItem.class)
@Page(type = PageType.BROWSE, page = Pages.Admin.SuggestionBrowse.class)
public class SuggestionFeature extends AbstractFeatureHandler{
    
}
