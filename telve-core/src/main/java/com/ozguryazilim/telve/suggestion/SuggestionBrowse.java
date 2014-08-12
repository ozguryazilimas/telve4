/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.suggestion;

import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.entities.SuggestionItem;
import com.ozguryazilim.telve.entities.SuggestionItem_;
import com.ozguryazilim.telve.forms.Browse;
import com.ozguryazilim.telve.forms.BrowseBase;
import com.ozguryazilim.telve.query.QueryDefinition;
import com.ozguryazilim.telve.query.StringFilter;
import com.ozguryazilim.telve.query.TextColumn;
import com.ozguryazilim.telve.view.Pages;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.view.DefaultErrorView;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

/**
 * Suggestion Item Browse win controller.
 * 
 * Bu browse'un edit/view kısmı olmadığı için editPage ve viewContainerPage'i DefaultErrorView'e
 * 
 * @author Hakan Uygun
 */
@Browse(browsePage = Pages.Admin.SuggestionBrowse.class,editPage = DefaultErrorView.class, viewContainerPage = DefaultErrorView.class )
public class SuggestionBrowse extends BrowseBase<SuggestionItem, SuggestionItem>{

    @Inject
    private SuggestionRepository repository;
    
    private SuggestionItem suggestionItem;
    
    @Override
    protected void buildQueryDefinition(QueryDefinition<SuggestionItem, SuggestionItem> queryDefinition) {
        
        queryDefinition
                .addFilter(new StringFilter<>(SuggestionItem_.group, "general.label.Group"), true, true)
                .addFilter(new StringFilter<>(SuggestionItem_.key, "general.label.Key"), true );
        
        queryDefinition.addColumn(new TextColumn<>(SuggestionItem_.group, "general.label.Group"),true);
        queryDefinition.addColumn(new TextColumn<>(SuggestionItem_.data, "general.label.Data"),true);
        queryDefinition.addColumn(new TextColumn<>(SuggestionItem_.info, "general.label.Info"),true);
        queryDefinition.addColumn(new TextColumn<>(SuggestionItem_.key, "general.label.Key"),true);
        queryDefinition.addColumn(new TextColumn<>(SuggestionItem_.active, "general.label.Active"),true);
        
    }   

    @Override
    protected RepositoryBase<SuggestionItem,SuggestionItem> getRepository() {
        return repository;
    }

    public SuggestionItem getSuggestionItem() {
        return suggestionItem;
    }

    public void setSuggestionItem(SuggestionItem suggestionItem) {
        this.suggestionItem = suggestionItem;
    }
    
    
    public void newSuggestion(){
        suggestionItem = new SuggestionItem();
        suggestionItem.setActive(Boolean.TRUE);
    }
    
    public void editSuggestion(){
        suggestionItem = selectedItem;
    }
    
    @Transactional
    public void deleteSuggestion(){
        if( selectedItem != null ){
            repository.remove(suggestionItem);
            search();
        }
    }
    
    public void closeDialog(){
        //Normal bir kapanış olduğuna göre ya save işlemi yapılacak demek.
        if( suggestionItem != null){
            
            repository.save(suggestionItem);
            
            search();
        }
    }
    
    public void cancelDialog(){
        //Aslınd ayapacak bir şey yok.
    }
}
