package com.ozguryazilim.telve.reports;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author haky
 */
public class ReportManagerTest {
    
    public ReportManagerTest() {
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
     * Test of getFolderList method, of class ReportManager.
     */
    //@Test
    public void testGetFolderList() {
        System.out.println("getFolderList");
        ReportManager instance = new ReportManager();
        List<ReportFolder> expResult = null;
        List<ReportFolder> result = instance.getFolderList();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findOrCreateFolder method, of class ReportManager.
     */
    //@Test
    public void testFindOrCreateFolder_String() {
        System.out.println("findOrCreateFolder");
        String path = "";
        ReportManager instance = new ReportManager();
        ReportFolder expResult = null;
        ReportFolder result = instance.findOrCreateFolder(path);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findOrCreateFolder method, of class ReportManager.
     */
    @Test
    public void testFindOrCreateFolder_String_String() {
        System.out.println("findOrCreateFolder");
        String path = "/folder1/folder2/folder3";
        String type = "ABC";
        ReportManager instance = new ReportManager();
        ReportFolder expResult = new ReportFolder("/folder1/folder2", "/folder1/folder2/folder3", "folder3", "ABC");
        ReportFolder result = instance.findOrCreateFolder(path, type);
        System.out.println(result);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetParentFolders() {
        System.out.println("testGetParentFolders");
        String path = "/folder1/folder2/folder3";
        String report = "ABC";
        ReportManager instance = new ReportManager();
        instance.addReport(path, report);
        
        //List<ReportFolder> expResult = new ReportFolder("/folder1/folder2", "/folder1/folder2/folder3", "folder3", "ABC");
        List<ReportFolder> result = instance.getParentFolderList( path );
        System.out.println(result);
        assertEquals(2, result.size());
    }
    
    /**
     * Test of addReport method, of class ReportManager.
     */
    @Test
    public void testAddReport() {
        System.out.println("addReport");
        String path = "/folder1/folder2/folder3/";
        String report = "Deneme";
        ReportManager instance = new ReportManager();
        instance.addReport(path, report);
        
    }

    /**
     * Test of getFolder method, of class ReportManager.
     */
    //@Test
    public void testGetFolder() {
        System.out.println("getFolder");
        String path = "";
        ReportManager instance = new ReportManager();
        ReportFolder expResult = null;
        ReportFolder result = instance.getFolder(path);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getReports method, of class ReportManager.
     */
    //@Test
    public void testGetReports() {
        System.out.println("getReports");
        String path = "";
        ReportManager instance = new ReportManager();
        List<String> expResult = null;
        List<String> result = instance.getReports(path);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findSubfolder method, of class ReportManager.
     */
    //@Test
    public void testFindSubfolder() {
        System.out.println("findSubfolder");
        ReportFolder folder = null;
        String folderName = "";
        ReportManager instance = new ReportManager();
        ReportFolder expResult = null;
        ReportFolder result = instance.findSubfolder(folder, folderName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    /**
     * Normal path kontrolü
     */
    @Test
    public void testGetParentPath() {
        System.out.println("testGetParentPath");
        String path = "/folder1/folder2/folder3";
        
        ReportManager instance = new ReportManager();
        
        String expResult = "/folder1/folder2";
        String result = instance.getParentPath(path);
        assertEquals(expResult, result);
        
    }
    
    /**
     * Sonu / ile biten path kontrol
     */
    @Test
    public void testGetParentPath2() {
        System.out.println("testGetParentPath");
        String path = "/folder1/folder2/folder3/";
        
        ReportManager instance = new ReportManager();
        
        String expResult = "/folder1/folder2";
        String result = instance.getParentPath(path);
        assertEquals(expResult, result);
        
    }
    
    /**
     * baında / olmayan
     */
    @Test
    public void testGetParentPath3() {
        System.out.println("testGetParentPath");
        String path = "folder1/folder2/folder3/";
        
        ReportManager instance = new ReportManager();
        
        String expResult = "/folder1/folder2";
        String result = instance.getParentPath(path);
        assertEquals(expResult, result);
        
    }
    
    /**
     * Tek folder
     */
    @Test
    public void testGetParentPath4() {
        System.out.println("testGetParentPath");
        String path = "/folder1";
        
        ReportManager instance = new ReportManager();
        
        String expResult = "#";
        String result = instance.getParentPath(path);
        assertEquals(expResult, result);
        
    }
    
    /**
     * Hiç folder
     */
    @Test
    public void testGetParentPath5() {
        System.out.println("testGetParentPath");
        String path = "/";
        
        ReportManager instance = new ReportManager();
        
        String expResult = "#";
        String result = instance.getParentPath(path);
        assertEquals(expResult, result);
        
    }
    
    /**
     * Normal path kontrolü
     */
    @Test
    public void testGetFolderName() {
        System.out.println("testGetFolderName");
        String path = "/folder1/folder2/folder3";
        
        ReportManager instance = new ReportManager();
        
        String expResult = "folder3";
        String result = instance.getFolderName(path);
        assertEquals(expResult, result);
        
    }
    
    /**
     * Sonu / ile biten
     */
    @Test
    public void testGetFolderName2() {
        System.out.println("testGetFolderName");
        String path = "/folder1/folder2/folder3/";
        
        ReportManager instance = new ReportManager();
        
        String expResult = "folder3";
        String result = instance.getFolderName(path);
        assertEquals(expResult, result);
        
    }
    
    /**
     * başında / olmayan
     */
    @Test
    public void testGetFolderName3() {
        System.out.println("testGetFolderName");
        String path = "folder1/folder2/folder3/";
        
        ReportManager instance = new ReportManager();
        
        String expResult = "folder3";
        String result = instance.getFolderName(path);
        assertEquals(expResult, result);
        
    }
    
    /**
     * Tek folder
     */
    @Test
    public void testGetFolderName4() {
        System.out.println("testGetFolderName");
        String path = "/folder1";
        
        ReportManager instance = new ReportManager();
        
        String expResult = "folder1";
        String result = instance.getFolderName(path);
        assertEquals(expResult, result);
        
    }
    
    /**
     * Hiç folder
     */
    @Test
    public void testGetFolderName5() {
        System.out.println("testGetFolderName");
        String path = "/";
        
        ReportManager instance = new ReportManager();
        
        String expResult = "#";
        String result = instance.getFolderName(path);
        assertEquals(expResult, result);
        
    }
}
