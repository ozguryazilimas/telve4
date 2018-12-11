/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.utils;

import java.util.Date;
import javax.ejb.ScheduleExpression;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author haky
 */
public class DateUtilsTest {
    
    
        
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testGetPeriodSimple() {
        Period expected = new Period(0, 0, 0, 2, 3, 0, 0, 0);
        
        //Türkçe
        Period per = DateUtils.getPeriod("2g3s");
        Assert.assertEquals(expected, per);
        
        //Ingilizce
        per = DateUtils.getPeriod("2d3h");
        Assert.assertEquals(expected, per);
        
    }
    
    
    @Test
    public void testGetPeriodFull() {
        Period expected = new Period(1, 2, 3, 4, 5, 6, 0, 0);
        
        //Türkçe
        Period per = DateUtils.getPeriod("1y2a3h4g5s6d");
        Assert.assertEquals(expected, per);
        
        //Ingilizce
        per = DateUtils.getPeriod("1y2m3w4d5h6M");
        Assert.assertEquals(expected, per);
        
    }
    
    @Test
    public void testGetPeriodi18n() {
        Period expected = new Period(1, 0, 3, 0, 0, 0, 0, 0);
        
        //FIXME: Türkçe format olduğunu nasıl keşfetmeli?!
        
        //Türkçe
        Period per = DateUtils.getPeriod("1y0a3h");
        Assert.assertEquals(expected, per);
        
        //Ingilizce
        per = DateUtils.getPeriod("1y3w");
        Assert.assertEquals(expected, per);
        
    }
    
    @Test
    public void testGetPeriodi18n2() {
        Period expected = new Period(0, 0, 0, 0, 1, 5, 0, 0);
        
        //FIXME: Türkçe format olduğunu nasıl keşfetmeli?!
        
        //Türkçe
        Period per = DateUtils.getPeriod("1s5d");
        Assert.assertEquals(expected, per);
        
        //Ingilizce
        per = DateUtils.getPeriod("1h5M");
        Assert.assertEquals(expected, per);
        
    }
    
    
    @Test
    public void testGetDateAfter() {
        
        DateTime dt = new DateTime(2015, 1, 1, 1, 1);
        Date expected = dt.plusHours(1).plusMinutes(5).toDate();
        
        Date result = DateUtils.getDateAfterPeriod("1s5d", dt.toDate());
        Assert.assertEquals(expected, result);
        
        //Ingilizce
        result = DateUtils.getDateAfterPeriod("1h5M", dt.toDate());
        Assert.assertEquals(expected, result);
        
    }
    
    
    @Test
    public void testGetDateBefore() {
        
        DateTime dt = new DateTime(2015, 1, 1, 1, 1);
        Date expected = dt.minusHours(1).minusMinutes(5).toDate();
        
        Date result = DateUtils.getDateBeforePeriod("1s5d", dt.toDate());
        Assert.assertEquals(expected, result);
        
        //Ingilizce
        result = DateUtils.getDateBeforePeriod("1h5M", dt.toDate());
        Assert.assertEquals(expected, result);
        
    }
    
    
    @Test
    public void testScheduleExpression() {
        
        ScheduleExpression expected = new ScheduleExpression();
        
        expected.hour("5");
        expected.month("3");
        
        
        ScheduleExpression result = DateUtils.getScheduleExpression("0 0 5 * * 3 *");
        Assert.assertEquals(expected.getSecond(), result.getSecond());
        Assert.assertEquals(expected.getMinute(), result.getMinute());
        Assert.assertEquals(expected.getHour(), result.getHour());
        Assert.assertEquals(expected.getDayOfMonth(), result.getDayOfMonth());
        Assert.assertEquals(expected.getDayOfWeek(), result.getDayOfWeek());
        Assert.assertEquals(expected.getYear(), result.getYear());
        
        
    }
    
}
