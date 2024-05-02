package com.ozguryazilim.telve.idm.ldapSync.groupsync;

import com.ozguryazilim.telve.idm.ldapSync.LdapSyncCommand;
import javax.naming.directory.SearchResult;

public class LdapGroupSyncCommand extends LdapSyncCommand {

    private SearchResult groupSearchResult;


    public LdapGroupSyncCommand(LdapSyncCommand ldapSyncCommand,
                                SearchResult groupSearchResult) {
        super(ldapSyncCommand.getSyncGroupsAndAssignUsers(),
                ldapSyncCommand.getCreateMissingGroups(),
                ldapSyncCommand.getSyncRolesAndAssignUsers(),
                ldapSyncCommand.getActivateDeactivatedGroups());
        this.groupSearchResult = groupSearchResult;
    }

    public SearchResult getGroupSearchResult() {
        return groupSearchResult;
    }

    public void setGroupSearchResult(SearchResult groupSearchResult) {
        this.groupSearchResult = groupSearchResult;
    }
}
