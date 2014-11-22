/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.mutfak.kahve;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Kavhe kapsamında gelen key value bilgilerini veri tabanına işlemlerini yapar.
 *
 * @author Hakan Uygun
 */
public class KahveStore {

    private final Connection connection;

    public KahveStore(DataSource dataSource) throws SQLException, NamingException {

        connection = dataSource.getConnection();
    }

    private static KahveStore instance;

    public static void createInstance(String ds) throws NamingException {
        InitialContext ic = new InitialContext();
        DataSource dataSource = (DataSource) ic.lookup(ds);
        try {
            instance = new KahveStore(dataSource);
        } catch (SQLException ex) {
            Logger.getLogger(KahveStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void createInstance(DataSource dataSource) {
        try {
            instance = new KahveStore(dataSource);
        } catch (SQLException | NamingException ex) {
            Logger.getLogger(KahveStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static KahveStore getInstance() {
        return instance;
    }

    public void save(String s, KahveEntry o) throws SQLException {
        KahveEntry ke = load(s);

        if (ke == null) {
            connection.createStatement().executeUpdate(
                    String.format("insert into KAHVE ( KV_KEY, KV_VAL ) values ( '%s', '%s' )", s, o.getValue()));
        } else {
            connection.createStatement().executeUpdate(
                    String.format("update KAHVE set KV_VAL = '%s' where KV_KEY = '%s'", o.getValue(), s));
        }
    }

    public void delete(String s) throws SQLException {
        connection.createStatement().executeUpdate(
                String.format("delete KAHVE where KV_KEY = '%s'", s));
    }

    public KahveEntry load(String s) {

        try {
            ResultSet rs = connection.createStatement().executeQuery("select KV_VAL from KAHVE where KV_KEY = '" + s + "'");

            try {
                if (!rs.next()) {
                    return null;
                }
                return new KahveEntry(rs.getString(1));
            } finally {
                rs.close();
            }

        } catch (SQLException ex) {
            Logger.getLogger(KahveStore.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

}
