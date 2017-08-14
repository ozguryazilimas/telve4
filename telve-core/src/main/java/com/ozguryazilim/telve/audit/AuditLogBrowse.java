/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.audit;

import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.entities.AuditLog;
import com.ozguryazilim.telve.entities.AuditLog_;
import com.ozguryazilim.telve.forms.Browse;
import com.ozguryazilim.telve.forms.BrowseBase;
import com.ozguryazilim.telve.query.QueryDefinition;
import com.ozguryazilim.telve.query.columns.DateTimeColumn;
import com.ozguryazilim.telve.query.columns.TextColumn;
import com.ozguryazilim.telve.query.filters.DateTimeFilter;
import com.ozguryazilim.telve.query.filters.FilterOperand;
import com.ozguryazilim.telve.query.filters.StringFilter;
import com.ozguryazilim.telve.query.filters.StringListFilter;
import java.util.List;
import javax.inject.Inject;

/**
 * AuditLog izleme tarama ekranı.
 * 
 * @author Hakan Uygun
 */
@Browse( feature = AuditLogFeature.class)
public class AuditLogBrowse extends BrowseBase<AuditLog, AuditLog>{

    @Inject
    private AuditLogRepository repository;
    
    @Override
    protected void buildQueryDefinition(QueryDefinition<AuditLog, AuditLog> queryDefinition) {
        
        DateTimeFilter dtf = new DateTimeFilter<>(AuditLog_.date, "general.label.DateTime");
        
        dtf.setOperand(FilterOperand.All);
        
        List<String> catValues = repository.findDistinctCategories();
        List<String> actValues = repository.findDistinctActions();
        List<String> domValues = repository.findDistinctDomains();
        
        queryDefinition.addFilter(new StringFilter<>(AuditLog_.bizPK, "general.label.Key"))
                .addFilter(new StringListFilter<>(AuditLog_.domain, domValues, "general.label.Domain",""))
                .addFilter(new StringListFilter<>(AuditLog_.category, catValues, "general.label.Category", ""))
                .addFilter(new StringListFilter<>(AuditLog_.action, actValues, "general.label.Action", ""))
                .addFilter(new StringFilter<>(AuditLog_.user, "general.label.User"))
                .addFilter(dtf);
        
        queryDefinition.addColumn(new DateTimeColumn<>(AuditLog_.date, "general.label.DateTime"),true);
        queryDefinition.addColumn(new TextColumn<>(AuditLog_.domain, "general.label.Domain"),true);
        queryDefinition.addColumn(new TextColumn<>(AuditLog_.bizPK, "general.label.BizKey"),true);
        queryDefinition.addColumn(new TextColumn<>(AuditLog_.user, "general.label.User"),true);
        queryDefinition.addColumn(new TextColumn<>(AuditLog_.action, "general.label.Action"),true);
        queryDefinition.addColumn(new TextColumn<>(AuditLog_.category, "general.label.Category"),true);
        //queryDefinition.addColumn(new TextColumn<>(AuditLog_.message, "general.label.Message"),true);
        
    }

    @Override
    protected RepositoryBase<AuditLog, AuditLog> getRepository() {
        return repository;
    }
    
}
