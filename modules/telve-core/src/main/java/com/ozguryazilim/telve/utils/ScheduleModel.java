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
                    case "Hourly" :
                        ScheduleExpression se1 = DateUtils.getHourlyScheduleExpression(sm.getStartDate()); 
                        se1.start(sm.getStartDate());
                        se1.end(sm.getEndDate());
                        sm.setExpression(se1);
                        break;
                    case "Daily" : 
                        ScheduleExpression se2 = DateUtils.getDailyScheduleExpression(sm.getStartDate()); 
                        se2.start(sm.getStartDate());
                        se2.end(sm.getEndDate());
                        sm.setExpression(se2);
                        break;
                    case "Weekly" : 
                        ScheduleExpression se3 = DateUtils.getWeeklyScheduleExpression(sm.getStartDate()); 
                        se3.start(sm.getStartDate());
                        se3.end(sm.getEndDate());
                        sm.setExpression(se3);
                        break;
                    case "Monthly" : 
                        ScheduleExpression se4 = DateUtils.getMonthlyScheduleExpression(sm.getStartDate()); 
                        se4.start(sm.getStartDate());
                        se4.end(sm.getEndDate());
                        sm.setExpression(se4);
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
    
    public static String getPeriodExpression( String period, Date startDate ){
        StringBuilder sb = new StringBuilder();
        sb.append("T=P,D=").append(period);
        if( startDate != null ){
            sb.append(",SDT=").append(DateUtils.getDateTimeFormatter().print(new DateTime(startDate)));
        }
        return sb.toString();
    }
    
    public static String getScheduledExpression( String schedule, Date startDate, Date endDate ){
        StringBuilder sb = new StringBuilder();
        
        sb.append("T=SE,");
        sb.append("D=").append(schedule);
        if( startDate != null ){
            sb.append(",SDT=").append(DateUtils.getDateTimeFormatter().print(new DateTime(startDate)));
        }
        if( endDate != null ){
            sb.append(",EDT=").append(DateUtils.getDateTimeFormatter().print(new DateTime(endDate)));
        }
        
        return sb.toString();
    }
    
    public static String getShortScheduleExpression( String schedule, Date startDate, Date endDate ){
        StringBuilder sb = new StringBuilder();
        
        sb.append("T=S,");
        sb.append("D=").append(schedule);
        if( startDate != null ){
            sb.append(",SDT=").append(DateUtils.getDateTimeFormatter().print(new DateTime(startDate)));
        }
        if( endDate != null ){
            sb.append(",EDT=").append(DateUtils.getDateTimeFormatter().print(new DateTime(endDate)));
        }
        
        return sb.toString();
    }
    
    /**
     * İnsan için schedule çevrimi
     * @param exp
     * @return 
     */
    public static String convertForHuman( String exp ){
        StringBuilder sb = new StringBuilder();
        
        Map<String,String> map = Splitter.on(',').omitEmptyStrings().trimResults().withKeyValueSeparator("=").split(exp);
        
        //Önce tip kontrolü
        String t = map.get("T");
        switch ( t ){
            case "SE" : sb.append(map.get("D")); break;
            case "S" : sb.append(map.get("D")); break;
            case "P" : sb.append(map.get("D")); break;
            case "O" : sb.append("Bir Kez"); break;
        }
        
        return sb.toString();
    }
}
