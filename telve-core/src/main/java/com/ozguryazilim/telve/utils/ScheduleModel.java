/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.utils;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.util.Date;
import java.util.Map;
import javax.ejb.ScheduleExpression;
import org.joda.time.DateTime;
import org.joda.time.Period;

/**
 * Farklı Schedule tanımları için model sınıf.
 * 
 * 
 * String hali : 
 * 
 * Birden fazla period olacaksa ; ile ayrılır.
 * Bileşenler , ile ayrılır
 * Her bir bileşen ve verisi = ile ayrılır
 * T : Tip Değerler SE : ScheduleExpression, P:Period, O: Once, S: Short ScheduleExpression
 * D: Data. Tipe göre değişir
 * SDT: Start Date Time ( Once ve Period için zorunlu )
 * EDT: End Date Time ( Eğer verilmezse silinene kadar devam eder. zorunlu değil )
 * 
 * Ex: 
 * T=SE,D=0 0 * * * * * *, SDT=20151010T1213, EDT=20151010T1213
 * T:P,D:1a3g, SDT:20151010T1213, EDT:20151010T1213
 * T:O,SDT:20151010T1213
 * T:S,D:Daily, SDT:20151010T1213, EDT:20151010T1213
 * 
 * 
 * @author Hakan Uygun
 */
public class ScheduleModel {
   
    public enum Type{ ScheduleExpression, Period, Once, ShortSchedule };
    
    private Date startDate;
    private Date endDate;
    private ScheduleExpression expression;
    private Period period;
    private Type type;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ScheduleExpression getExpression() {
        return expression;
    }

    public void setExpression(ScheduleExpression expression) {
        this.expression = expression;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    
    /**
     * Verilen stringi parse edip ScheduleModel döndürür.
     * 
     * FIXME: Format hata kontrolleri yapılmalı
     * 
     * @param exp
     * @return 
     */
    public static ScheduleModel fromString( String exp ){
        
        ScheduleModel sm = new ScheduleModel();
        
        //, ve = ler kullanılarak parçalıyoruz.
        Map<String,String> map = Splitter.on(',').omitEmptyStrings().trimResults().withKeyValueSeparator("=").split(exp);
        
        //Önce tip kontrolü
        String t = map.get("T");
        switch ( t ){
            case "SE" : sm.setType(Type.ScheduleExpression); break;
            case "S" : sm.setType(Type.ShortSchedule); break;
            case "P" : sm.setType(Type.Period); break;
            case "O" : sm.setType(Type.Once); break;
        }
        
        //StartDate  kontrolü
        String sdt = map.get("SDT");
        if( !Strings.isNullOrEmpty(sdt) ){
            sm.setStartDate( DateUtils.getDateTimeFormatter().parseDateTime(sdt).toDate());
        }
        
        //endDate  kontrolü
        String edt = map.get("EDT");
        if( !Strings.isNullOrEmpty(edt) ){
            sm.setEndDate( DateUtils.getDateTimeFormatter().parseDateTime(edt).toDate());
        }
        
        //Asıl veri
        String d = map.get("D");
        switch ( sm.getType() ){
            case ScheduleExpression : 
                ScheduleExpression se = DateUtils.getScheduleExpression(d);
                se.start(sm.getStartDate());
                se.end(sm.getEndDate());
                sm.setExpression(se);
                break;
            case ShortSchedule : 
                switch ( d ){
                    case "Daily" : 
                        //ScheduleExpression se1 = 
                        //se.start(sm.getStartDate());
                        //se.end(sm.getEndDate());
                        //sm.setExpression(se);
                        break;
                    case "Weekly" : 
                        //ScheduleExpression se1 = 
                        //se.start(sm.getStartDate());
                        //se.end(sm.getEndDate());
                        //sm.setExpression(se);
                        break;
                    case "Weekday" : 
                        //ScheduleExpression se1 = 
                        //se.start(sm.getStartDate());
                        //se.end(sm.getEndDate());
                        //sm.setExpression(se);
                        break;
                    case "Monthly" : 
                        //ScheduleExpression se1 = 
                        //se.start(sm.getStartDate());
                        //se.end(sm.getEndDate());
                        //sm.setExpression(se);
                        break;
                    case "Yearly" : 
                        //ScheduleExpression se1 = 
                        //se.start(sm.getStartDate());
                        //se.end(sm.getEndDate());
                        //sm.setExpression(se);
                        break;
                }
                break;
            case Period :
                sm.setPeriod(DateUtils.getPeriod(d));
                break;
            case Once : 
                break;
        }
        
        
       return sm;
    }
    
    public static String getOnceExpression( Date date ){
        return "T=O,SDT="+ DateUtils.getDateTimeFormatter().print(new DateTime(date));
    }
}
