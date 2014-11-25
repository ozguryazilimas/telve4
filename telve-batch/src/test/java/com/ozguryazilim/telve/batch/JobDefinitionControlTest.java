/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.batch;

import com.google.common.io.Files;
import com.google.common.reflect.ClassPath;
import com.ozguryazilim.telve.batch.artifacts.BatchLogCleanerBatchlet;
import com.ozguryazilim.telve.entities.JobDefinition;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.sf.corn.cps.CPScanner;
import net.sf.corn.cps.ResourceFilter;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author haky
 */
@RunWith(Arquillian.class)
@Dependent
public class JobDefinitionControlTest {
    
    
    @Deployment
    public static WebArchive deployment() {

        WebArchive archive = ShrinkWrap.create(WebArchive.class)
                .addClass(BatchProducer.class)
                .addClass(JobDefinitionControl.class)
                .addClass(BatchLogCleanerBatchlet.class)
                .addClass(JobRegistery.class)
                .addClass(JobDefinitionRepository.class)
                .addClass(BatchScheduler.class)
                .addClass(BatchLogger.class)
                .addClass(BatchStarter.class)
                .addClass(BatchStateListener.class)
                .addClass(BatchLoggerRepository.class)
                .addPackage("com.ozguryazilim.telve.query")
                .addPackage("com.ozguryazilim.telve.data")
                .addAsWebInfResource("apache-deltaspike.properties",
                        ArchivePaths.create("classes/META-INF/apache-deltaspike.properties"))
                .addAsWebInfResource("test-persistence.xml",
                        ArchivePaths.create("classes/META-INF/persistence.xml"))
                .addAsWebInfResource("test-ds.xml",
                        ArchivePaths.create("test-ds.xml"))
                .addAsWebInfResource("test-beans.xml",
                        ArchivePaths.create("beans.xml"))
                .addAsWebInfResource("test-orm.xml",
                        ArchivePaths.create("classes/META-INF/orm.xml"))
                .addAsWebInfResource("META-INF/batch-jobs/BatchLogCleaner.xml",
                        ArchivePaths.create("classes/META-INF/batch-jobs/BatchLogCleaner.xml"));

        //Model paketini ekle
        archive.addAsLibraries(
                Maven.resolver().loadPomFromFile("pom.xml").resolve(
                        "com.ozguryazilim.mutfak:telve-batch-model:4.0.0-SNAPSHOT"
                )
                .withTransitivity()
                .asFile());
        
        //Bağımlılıkları ekle
        archive.addAsLibraries(
                Maven.resolver().loadPomFromFile("pom.xml").resolve(
                        "org.apache.deltaspike.core:deltaspike-core-api",
                        "org.apache.deltaspike.core:deltaspike-core-impl",
                        "org.apache.deltaspike.modules:deltaspike-partial-bean-module-api",
                        "org.apache.deltaspike.modules:deltaspike-partial-bean-module-impl",
                        "org.apache.deltaspike.modules:deltaspike-jpa-module-api",
                        "org.apache.deltaspike.modules:deltaspike-jpa-module-impl",
                        "org.apache.deltaspike.modules:deltaspike-data-module-api",
                        "org.apache.deltaspike.modules:deltaspike-data-module-impl",
                        //"com.ozguryazilim.mutfak:telve-core:4.0.0-SNAPSHOT",
                        "net.sf.corn:corn-cps",
                        "com.google.guava:guava")
                .withTransitivity()
                .asFile());


        return archive;
    }
    
    
    @Before
    public void beforeTest(){
        JobRegistery.register("BatchLogCleaner");
    }
    
    @Produces
    @PersistenceContext(name = "test")
    @Default
    private EntityManager entityManager;
    
    @Inject
    private JobDefinitionControl jdc;
    
    @Inject
    private BatchStarter batchStarter;
    
    /**
     * Test of getJobNameList method, of class JobDefinitionControl.
     */
    @Test
    public void testGetJobNameList() {
        System.out.println("getJobNameList");
        List<String> names = jdc.getJobNameList(); 
        System.out.println( names );
        Assert.assertFalse(names.isEmpty());
    }
    
    
    @Test
    public void testCreateNewJobDefinition() throws InstantiationException, IllegalAccessException {
        System.out.println("CreateNewJobDefinition");
        JobDefinition jd = jdc.createNewJobDefinition("BatchLogCleaner");
        System.out.println( jd );
        //Assert.assertFalse(names.isEmpty());
        Assert.assertEquals("BatchLogCleaner", jd.getJobName());
        Assert.assertEquals("NONE", jd.getSchedule());
        Assert.assertEquals("YeniBatchLogCleaner", jd.getName());
    }
    
    @Test
    public void testSaveJobDefinition() throws InstantiationException, IllegalAccessException, InterruptedException {
        System.out.println("SaveJobDefinition");
        JobDefinition jd = jdc.createNewJobDefinition("BatchLogCleaner");
        System.out.println( jd );
        
        jd.setActive(Boolean.TRUE);
        jd.getProperties().setProperty("expireInterval", "15");
        jd.getProperties().setProperty("executionLog", "TRUE");
        jd.getProperties().setProperty("workLog", "TRUE");
        
        jdc.saveJobDefinition(jd);
        Thread.sleep(25000l);
    }
    
    @Test
    public void testStartJob() throws InterruptedException, IOException {
        System.out.println("StartJob");
        Properties props = new Properties();
        props.setProperty("expireInterval", "15");
        props.setProperty("executionLog", "TRUE");
        props.setProperty("workLog", "TRUE");
        
        System.out.println( props );
        
        batchStarter.start("BatchLogCleaner", props, "Test-Suit");
        
        //JobOperator operator = BatchRuntime.getJobOperator();
        //System.out.println(operator.getJobNames());
        //operator.start("BatchLogCleaner", props);
        //System.out.println(operator.getJobNames());
        Thread.sleep(5000l);
    }
    
    
    //@Test
    public void testGuavaClasspathSearch() throws IOException{
        System.out.println("Guava Search Test");
        ClassPath classpath = ClassPath.from(Thread.currentThread().getContextClassLoader()); // scans the class path used by classloader
        for (ClassPath.ResourceInfo resInfo : classpath.getResources()) {
            if( Files.getFileExtension(resInfo.getResourceName()).equals("xml") ){
                System.out.println(resInfo.getResourceName());
            }
            
        }
    }
    
    //@Test
    public void testCPSClasspathSearch() throws IOException{
        System.out.println("CPS Search Test");
        List<URL> resources = CPScanner.scanResources(new ResourceFilter().resourceName("*.xml"));
        for( URL u : resources ){
            System.out.println(u);
        }
    }
    
}
