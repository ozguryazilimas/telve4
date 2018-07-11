package com.ozguryazilim.telve.query.filters.migration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.DatabaseException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

public class UserSavedFiltersRemoveValue implements CustomTaskChange {

    private String className;
    private String filter;

    @Override
    public void execute(Database dtbs) throws CustomChangeException {
        JdbcConnection connection = (JdbcConnection) dtbs.getConnection();

        try {
            PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM KAHVE");
            ResultSet list = selectStatement.executeQuery();

            Map<String, String> map = new HashMap<>();

            while (list.next()) {
                if (list.getString("KV_KEY").contains(className)) {
                    String key = list.getString("KV_KEY");
                    String val = list.getString("KV_VAL");
                    map.put(key, val);
                }
            }

            map.forEach((k, v) -> {
                try {
                    System.out.println(k + " new");
                    String newVal = v.replaceAll("(\"" + filter + "\":\".*?)(::.*?)(\")", "$1$3");
                    PreparedStatement updateQuery = connection.prepareStatement(
                            "UPDATE KAHVE SET KV_VAL = " + "'" + newVal + "'"
                            + "WHERE KV_KEY = " + "'" + k + "'");
                    updateQuery.executeQuery();
                } catch (DatabaseException | SQLException ex) {
                    Logger.getLogger(UserSavedFiltersRemoveValue.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

        } catch (DatabaseException | SQLException ex) {
            Logger.getLogger(UserSavedFiltersRemoveValue.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getConfirmationMessage() {
        return "migrated data successfully";
    }

    @Override
    public void setUp() throws SetupException {
    }

    @Override
    public void setFileOpener(ResourceAccessor ra) {
    }

    @Override
    public ValidationErrors validate(Database dtbs) {
        return null;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
