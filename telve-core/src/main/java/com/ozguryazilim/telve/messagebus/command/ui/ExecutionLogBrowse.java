/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command.ui;

import com.ozguryazilim.telve.commands.ExecutionLogRepository;
import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.entities.ExecutionLog;
import com.ozguryazilim.telve.entities.ExecutionLog_;
import com.ozguryazilim.telve.forms.Browse;
import com.ozguryazilim.telve.forms.BrowseBase;
import com.ozguryazilim.telve.query.QueryDefinition;
import com.ozguryazilim.telve.query.columns.DateTimeColumn;
import com.ozguryazilim.telve.query.columns.TextColumn;
import com.ozguryazilim.telve.view.Pages;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.view.DefaultErrorView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ExecutionLog tarayıcısı
 * @author Hakan Uygun
 */
@Browse(browsePage = Pages.Admin.ExecutionLogBrowse.class, editPage = DefaultErrorView.class, viewContainerPage = DefaultErrorView.class )
public class ExecutionLogBrowse extends BrowseBase<ExecutionLog, ExecutionLog>{
    private static final Logger LOG = LoggerFactory.getLogger(ExecutionLogBrowse.class);

    @Inject
    private ExecutionLogRepository repository;
    
    @Override
    protected void buildQueryDefinition(QueryDefinition<ExecutionLog, ExecutionLog> queryDefinition) {
        //TODO: Filtreler eklenecek
        queryDefinition.addColumn(new DateTimeColumn<>(ExecutionLog_.date, "general.label.Date"),true);
        queryDefinition.addColumn(new TextColumn<>(ExecutionLog_.severity, "general.label.Severity"),true);
        queryDefinition.addColumn(new TextColumn<>(ExecutionLog_.logger, "general.label.Logger"),true);
        queryDefinition.addColumn(new TextColumn<>(ExecutionLog_.message, "general.label.Log"),true);
    }

    @Override
    protected RepositoryBase<ExecutionLog, ExecutionLog> getRepository() {
        return repository;
    }
}
