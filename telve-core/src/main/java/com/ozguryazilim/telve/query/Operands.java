/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.query;

import com.ozguryazilim.telve.query.filters.FilterOperand;
import java.util.ArrayList;
import java.util.List;

/**
 * Filtre Operandları için util sınıf.
 *
 * @author Hakan Uygun
 */
public class Operands {

    private static List<FilterOperand> reportOperands;
    private static List<FilterOperand> stringOperands;
    private static List<FilterOperand> enumOperands;
    private static List<FilterOperand> dateOperands;
    private static List<FilterOperand> entityOperands;
    private static List<FilterOperand> numberOperands;
    private static List<FilterOperand> equalOperands;
    private static List<FilterOperand> treeEntityOperands;

    /**
     * Public contructor gizleniyor
     */
    protected Operands() {
    }

    /**
     * Geriye ön tanımlı string operatorlerini döndürür.
     *
     * @return
     */
    public static List<FilterOperand> getStringOperands() {

        if (stringOperands == null) {
            stringOperands = new ArrayList<FilterOperand>();
            stringOperands.add(FilterOperand.All);
            stringOperands.add(FilterOperand.Equal);
            stringOperands.add(FilterOperand.NotEqual);
            stringOperands.add(FilterOperand.Contains);
            stringOperands.add(FilterOperand.NotContains);
            stringOperands.add(FilterOperand.BeginsWith);
            stringOperands.add(FilterOperand.EndsWith);
        }

        return stringOperands;
    }

    public static List<FilterOperand> getEnumOperands() {
        if (enumOperands == null) {
            enumOperands = new ArrayList<FilterOperand>();
            enumOperands.add(FilterOperand.All);
            enumOperands.add(FilterOperand.Equal);
            enumOperands.add(FilterOperand.NotEqual);
        }
        return enumOperands;
    }

    public static List<FilterOperand> getDateOperands() {
        if (dateOperands == null) {
            dateOperands = new ArrayList<FilterOperand>();
            dateOperands.add(FilterOperand.All);
            dateOperands.add(FilterOperand.Equal);
            dateOperands.add(FilterOperand.NotEqual);
            dateOperands.add(FilterOperand.Greater);
            dateOperands.add(FilterOperand.GreaterOrEqual);
            dateOperands.add(FilterOperand.Lesser);
            dateOperands.add(FilterOperand.LesserOrEqual);
            dateOperands.add(FilterOperand.In);
            dateOperands.add(FilterOperand.Between);
        }
        return dateOperands;
    }

    public static List<FilterOperand> getNumberOperands() {
        if (numberOperands == null) {
            numberOperands = new ArrayList<FilterOperand>();
            numberOperands.add(FilterOperand.All);
            numberOperands.add(FilterOperand.Equal);
            numberOperands.add(FilterOperand.NotEqual);
            numberOperands.add(FilterOperand.Greater);
            numberOperands.add(FilterOperand.GreaterOrEqual);
            numberOperands.add(FilterOperand.Lesser);
            numberOperands.add(FilterOperand.LesserOrEqual);
            numberOperands.add(FilterOperand.Between);
        }
        return numberOperands;
    }

    public static List<FilterOperand> getEntityOperands() {
        if (entityOperands == null) {
            entityOperands = new ArrayList<FilterOperand>();
            entityOperands.add(FilterOperand.All);
            entityOperands.add(FilterOperand.None);
            entityOperands.add(FilterOperand.Equal);
            entityOperands.add(FilterOperand.NotEqual);
        }
        return entityOperands;
    }
    
    public static List<FilterOperand> getTreeEntityOperands() {
        if (treeEntityOperands == null) {
            treeEntityOperands = new ArrayList<FilterOperand>();
            treeEntityOperands.add(FilterOperand.All);
            treeEntityOperands.add(FilterOperand.None);
            treeEntityOperands.add(FilterOperand.Equal);
            treeEntityOperands.add(FilterOperand.NotEqual);
            treeEntityOperands.add(FilterOperand.Under);
        }
        return treeEntityOperands;
    }

    public static List<FilterOperand> getReportOperands() {
        if (reportOperands == null) {
            reportOperands = new ArrayList<FilterOperand>();
            reportOperands.add(FilterOperand.All);
            reportOperands.add(FilterOperand.Equal);
        }
        return reportOperands;
    }

    public static List<FilterOperand> getEqualOperands() {
        if (equalOperands == null) {
            equalOperands = new ArrayList<FilterOperand>();
            equalOperands.add(FilterOperand.Equal);
        }
        return equalOperands;
    }
}
