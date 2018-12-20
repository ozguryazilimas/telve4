package com.ozguryazilim.telve.utils;

import org.joda.time.Period;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author haky
 */
public class ScheduleModelTest {
    
    
    /**
     * Test of fromString method, of class ScheduleModel.
     */
    @Test
    public void testScheduleExpressionFromString() {
        System.out.println("fromString");
        String exp = "T=SE,D=0 0 0 * * * *,SDT=20150101T0101,EDT=20150102T0101";
        ScheduleModel expResult = null;
        ScheduleModel result = ScheduleModel.fromString(exp);
        
        assertEquals(ScheduleModel.Type.ScheduleExpression, result.getType());
        assertEquals("0", result.getExpression().getSecond());
        assertEquals(DateUtils.getDateTimeFormatter().parseDateTime("20150101T0101").toDate(), result.getExpression().getStart());
        assertEquals(DateUtils.getDateTimeFormatter().parseDateTime("20150102T0101").toDate(), result.getExpression().getEnd());
    }
    
    
    @Test
    public void testPeriodFromString() {
        System.out.println("fromString");
        String exp = "T=P,D=23g,SDT=20150101T0101";
        
        ScheduleModel result = ScheduleModel.fromString(exp);
        
        Period p = DateUtils.getPeriod("23g");
        
        assertEquals(ScheduleModel.Type.Period, result.getType());
        assertEquals(p, result.getPeriod());
        assertEquals(DateUtils.getDateTimeFormatter().parseDateTime("20150101T0101").toDate(), result.getStartDate());
        Assert.assertNull(result.getEndDate());
    }
    
    
    @Test
    public void testOnceFromString() {
        System.out.println("fromString");
        String exp = "T=O,SDT=20150101T0101";
        
        ScheduleModel result = ScheduleModel.fromString(exp);
        
        assertEquals(ScheduleModel.Type.Once, result.getType());
        assertEquals(DateUtils.getDateTimeFormatter().parseDateTime("20150101T0101").toDate(), result.getStartDate());
        Assert.assertNull(result.getEndDate());
    }
    
}
