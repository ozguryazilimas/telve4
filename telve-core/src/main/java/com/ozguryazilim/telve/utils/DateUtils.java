/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.utils;

import com.google.common.base.Splitter;
import java.util.Date;
import java.util.List;
import javax.ejb.ScheduleExpression;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * Tarih Saat ile ilgili çeşitli yardımcı fonksiyornlar.
 * 
 * Period, Schedule v.b. araçları barındırır.
 * 
 * @author Hakan Uygun
 */
public class DateUtils {
    
    private static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd'T'HHmm");
    
    private static final PeriodFormatter FMT_EN = new PeriodFormatterBuilder()
            .printZeroRarelyFirst()
            .appendYears()
            .appendSuffix("y", "y")
            .printZeroRarelyLast()
            .appendMonths()
            .appendSuffix("m","m")
            .appendWeeks()
            .appendSuffix("w", "w")
            .appendDays()
            .appendSuffix("d", "d")
            .appendHours()
            .appendSuffix("h", "h")
            .appendMinutes()
            .appendSuffix("M", "M")
            .toFormatter();

    
    private static final PeriodFormatter FMT_TR = new PeriodFormatterBuilder()
            .printZeroRarelyFirst()
            .appendYears()
            .appendSuffix("y", "y")
            .printZeroRarelyLast()
            .appendMonths()
            .appendSuffix("a", "a")
            .appendWeeks()
            .appendSuffix("h", "h")
            .appendDays()
            .appendSuffix("g", "g")
            .appendHours()
            .appendSuffix("s", "s")
            .appendMinutes()
            .appendSuffix("d", "d")
            .toFormatter();
    
    /**
     * Verilen stringden Period üretir.
     * 
     * String formatı 2g1s / 2d1h ( 2 gün 1 saat ) şeklinde ingilizce ya da türkçe olabilir.
     * 
     * @param perStr
     * @return 
     */
    public static Period getPeriod( String perStr ){
        org.joda.time.Period pr;
        
        if( perStr.contains("a") || perStr.contains("g")  || perStr.contains("s") ){
            pr = FMT_TR.parsePeriod(perStr).toPeriod();
        } else {
            pr = FMT_EN.parsePeriod(perStr).toPeriod();
        }
        
        return pr;
    }
    
    /**
     * Verilen period string kullanarak verilen tarihi üzerinden bir sonraki tarihi bulur.
     * @param perStr
     * @param curDate
     * @return 
     */
    public static Date getDateAfterPeriod(String perStr, Date curDate) {

        Period pr = getPeriod(perStr);
        
        DateTime dt = new DateTime(curDate);

        return dt.plus(pr).toDate();
    }
    
    /**
     * Verilen period stringini kullanarak verilen tarih üzerinden bir sonrakini bulur
     * @param perStr
     * @param curDate
     * @return 
     */
    public static Date getDateBeforePeriod(String perStr, Date curDate) {

        Period pr = getPeriod(perStr);
        
        DateTime dt = new DateTime(curDate);

        return dt.minus(pr).toDate();
    }
    

    /**
     * Verilen cron stringi kullanarak Timer servis için ScheduleExpression üretir.
     * Sırasıyla saniye, dakika, saat, gün, haftanın günü, ay, yıl  değerleri alır
     * 
     * Her beş dakikada bir
     * Ex: 0 5 * * * * * *
     * 
     * @param cron
     * @return 
     */
    public static ScheduleExpression getScheduleExpression( String cron ){
        
        List<String> ls = Splitter.on(' ').trimResults().omitEmptyStrings().splitToList(cron);
        
        ScheduleExpression result = new ScheduleExpression();
        result.second(ls.get(0));
        result.minute(ls.get(1));
        result.hour(ls.get(2));
        result.dayOfMonth(ls.get(3));
        result.dayOfWeek(ls.get(4));
        result.month(ls.get(5));
        result.year(ls.get(6));
        
        return result;
    }
    
    public static DateTimeFormatter getDateTimeFormatter(){
        return formatter;
    }
}
