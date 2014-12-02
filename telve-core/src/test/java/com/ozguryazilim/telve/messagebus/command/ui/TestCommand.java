/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command.ui;

import com.ozguryazilim.telve.messagebus.command.AbstractStorableCommand;
import java.math.BigDecimal;
import java.util.Date;

/**
 * StoredCommandRepository testi için komut.
 * 
 * @author Hakan Uygun
 */
public class TestCommand extends AbstractStorableCommand{
   
    private String stringData;
    private Integer integerData;
    private Boolean booleanData;
    private Date dateDate;
    private BigDecimal bigDecimalData; 

    public String getStringData() {
        return stringData;
    }

    public void setStringData(String stringData) {
        this.stringData = stringData;
    }

    public Integer getIntegerData() {
        return integerData;
    }

    public void setIntegerData(Integer integerData) {
        this.integerData = integerData;
    }

    public Boolean getBooleanData() {
        return booleanData;
    }

    public void setBooleanData(Boolean booleanData) {
        this.booleanData = booleanData;
    }

    public Date getDateDate() {
        return dateDate;
    }

    public void setDateDate(Date dateDate) {
        this.dateDate = dateDate;
    }

    public BigDecimal getBigDecimalData() {
        return bigDecimalData;
    }

    public void setBigDecimalData(BigDecimal bigDecimalData) {
        this.bigDecimalData = bigDecimalData;
    }
    
    
}
