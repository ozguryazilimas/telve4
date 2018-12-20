package com.ozguryazilim.telve.suggestion;

import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.entities.SuggestionItem;
import com.ozguryazilim.telve.entities.SuggestionItem_;
import com.ozguryazilim.telve.forms.Browse;
import com.ozguryazilim.telve.forms.BrowseBase;
import com.ozguryazilim.telve.query.QueryDefinition;
import com.ozguryazilim.telve.query.columns.BooleanColumn;
import com.ozguryazilim.telve.query.columns.MessageColumn;
import com.ozguryazilim.telve.query.filters.StringFilter;
import com.ozguryazilim.telve.query.filters.StringListFilter;
import com.ozguryazilim.telve.query.columns.TextColumn;
import javax.inject.Inject;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

/**
 * Suggestion Item Browse win controller.
 * 
 * Bu browse'un edit/view kısmı olmadığı için editPage ve viewContainerPage'i DefaultErrorView'e
 * 
 * @author Hakan Uygun
 */
@Browse( feature = SuggestionFeature.class )
public class SuggestionBrowse extends BrowseBase<SuggestionItem, SuggestionItem>{

    @Inject
    private SuggestionRepository repository;
    
    private SuggestionItem suggestionItem;
    
    @Override
    protected void buildQueryDefinition(QueryDefinition<SuggestionItem, SuggestionItem> queryDefinition) {
        
        queryDefinition
                .addFilter(new StringListFilter<>(SuggestionItem_.group, SuggestionGroupRegistery.intance().getGroupNames(), "general.label.Group", "suggestionGroup.label."))
                .addFilter(new StringFilter<>(SuggestionItem_.key, "general.label.Key"));
        
        queryDefinition.addColumn(new MessageColumn<>(SuggestionItem_.group, "general.label.Group", "suggestionGroup.label." ),true);
        queryDefinition.addColumn(new TextColumn<>(SuggestionItem_.data, "general.label.Data"),true);
        queryDefinition.addColumn(new TextColumn<>(SuggestionItem_.info, "general.label.Info"),true);
        queryDefinition.addColumn(new TextColumn<>(SuggestionItem_.key, "general.label.Key"),true);
        queryDefinition.addColumn(new BooleanColumn<>(SuggestionItem_.active, "general.label.Active", "general.boolean.yesno."),true);
        
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
            repository.remove(selectedItem);
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
