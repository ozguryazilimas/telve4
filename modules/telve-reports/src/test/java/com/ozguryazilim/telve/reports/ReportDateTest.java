package com.ozguryazilim.telve.reports;

import com.ozguryazilim.telve.query.filters.DateValueType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * ReportDate model sınıfının doğru çalışıp çalışmadığının testi.
 * @author Hakan Uygun
 */
public class ReportDateTest {
    
    public ReportDateTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getValueType method, of class ReportDate.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        System.out.println("getValueType");
        ReportDate instance = new ReportDate(DateValueType.Today);
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        
        oos.writeObject(instance);
        
        byte[] bytes = os.toByteArray();
        
        
        ByteArrayInputStream is = new ByteArrayInputStream( bytes);
        ObjectInputStream ois = new ObjectInputStream(is);
        
        ReportDate si = (ReportDate) ois.readObject();
        
        Date expResult = (new LocalDate()).toDate();
        Date result = si.getCalculatedValue();
        assertEquals(expResult, result);
        
    }
    
    @Test
    public void testGetToday() {
        System.out.println("getToDay");
        ReportDate instance = new ReportDate(DateValueType.Today);
        Date expResult = (new LocalDate()).toDate();
        
        Date result = instance.getCalculatedValue();
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetTomorrow() {
        System.out.println("getTomorrow");
        ReportDate instance = new ReportDate(DateValueType.Tomorrow);
        Date expResult = (new LocalDate()).plusDays(1).toDate();
        
        Date result = instance.getCalculatedValue();
        assertEquals(expResult, result);
    }
    
    
    @Test
    public void testGetDate() {
        System.out.println("getDate");
        Date expResult = (new LocalDate()).plusDays(1).toDate();
        ReportDate instance = new ReportDate(expResult);
        
        Date result = instance.getCalculatedValue();
        assertEquals(expResult, result);
    }

    
    
}
