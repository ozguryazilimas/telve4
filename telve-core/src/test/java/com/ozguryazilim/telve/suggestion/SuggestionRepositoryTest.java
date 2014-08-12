/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.suggestion;

import com.ozguryazilim.telve.entities.SuggestionItem;
import java.util.List;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import junit.framework.Assert;
import org.apache.deltaspike.data.api.QueryInvocationException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

/**
 * SuggestionRepository Birim Testi.
 * 
 * Methodların sırayla çalışması gerekiyor.
 *
 * @author Hakan Uygun
 */
@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SuggestionRepositoryTest {

    @Deployment
    public static WebArchive deployment() {

        WebArchive archive = ShrinkWrap.create(WebArchive.class)
                .addClass(SuggestionRepository.class)
                .addClass(SuggestionRepositoryTest.class)
                .addPackage("com.ozguryazilim.telve.query")
                .addAsWebInfResource("apache-deltaspike.properties",
                        ArchivePaths.create("classes/META-INF/apache-deltaspike.properties"))
                .addAsWebInfResource("test-persistence.xml",
                        ArchivePaths.create("classes/META-INF/persistence.xml"))
                .addAsWebInfResource("test-ds.xml",
                        ArchivePaths.create("test-ds.xml"))
                .addAsWebInfResource("test-beans.xml",
                        ArchivePaths.create("beans.xml"))
                .addAsWebInfResource("test-orm.xml",
                        ArchivePaths.create("classes/META-INF/orm.xml"));

        //Model paketini ekle
        archive.addAsLibraries(
                Maven.resolver().loadPomFromFile("pom.xml").resolve(
                        "com.ozguryazilim.mutfak:telve-core-model"
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
                        "org.apache.deltaspike.modules:deltaspike-data-module-impl")
                .withTransitivity()
                .asFile());

        System.out.println(archive);

        return archive;
    }

    @Produces
    @PersistenceContext(name = "test") @Default
    private EntityManager entityManager;

    
    @Inject
    SuggestionRepository repository;
    
    /**
     * Veri Kayıt Testi.
     * 
     * Senaryo : 
     *  3 Adet kayıt sırayla yapılır. Id dolu geldiğinde kayıt gerçekleşmiş demektir.
     *  
     */
    @Test   
    public void test1(){
        SuggestionItem si = new SuggestionItem();
        si.setActive(Boolean.TRUE);
        si.setData("Veri 1");
        si.setGroup("Group1");
        si.setInfo("Açıklama 1");
        si.setKey("Anahtar1");
        si = repository.save(si);
        
        Assert.assertNotNull(si.getId());
        
        si = new SuggestionItem();
        si.setActive(Boolean.TRUE);
        si.setData("Veri 1");
        si.setGroup("Group1");
        si.setInfo("Açıklama 1");
        si.setKey("Anahtar2");
        si = repository.save(si);
        
        Assert.assertNotNull(si.getId());
        
        si = new SuggestionItem();
        si.setActive(Boolean.TRUE);
        si.setData("Veri 2");
        si.setGroup("Group2");
        si.setInfo("Açıklama 2");
        si.setKey("Anahtar2");
        si = repository.save(si);
        
        Assert.assertNotNull(si.getId());
        
    }
    
    /**
     * Zorunlu alan kontrolü.
     * Senaryo : Data alanı @NatNull bir alan ve boş gönderildiğinde hata vermeli
     */
    @Test(expected = QueryInvocationException.class)
    public void test2(){
        SuggestionItem si = new SuggestionItem();
        si.setActive(Boolean.TRUE);
        
        si.setGroup("Group 1");
        si.setInfo("Açıklama 3");
        si.setKey("Anahtar3");
        si = repository.save(si);
        
        Assert.assertNotNull(si.getId());
        
    }
    
    /**
     * Tüm Listeyi Çekme Testi.
     * 
     * Yukarıdaki methodlardan sonra veri tabanında 2 kayıt olmalı.
     */
    @Test
    public void test3() {

        List<SuggestionItem> ls = repository.findAll();
        System.out.println("Kayıtlar : " + ls);
        
        Assert.assertEquals(3, ls.size());
    }

    /**
     * Veri Değiştirme Testi.
     * 
     * 2 Numaralı kayıdı bulup açıklamasını değiştireceğiz. Donra tekrar sorgu çekip doğrusunu bulmaya çalışacağız.
     * 
     */
    @Test
    public void test4() {

        SuggestionItem si = repository.findBy(2l);
        si.setInfo("Düzeltilmiş Açıklama");
        repository.save(si);
        
        si = repository.findBy(2l);
        
        Assert.assertEquals("Düzeltilmiş Açıklama", si.getInfo());
    }
    
    /**
     * Group değeri ile arama testi.
     * 
     */
    @Test
    public void test5() {

        //Group 1 ile 2 adet kayıt var
        List<SuggestionItem> ls = repository.findByGroup("Group1");
        Assert.assertEquals(2, ls.size());

        //Group 2 ile 1 adet kayıt var
        ls = repository.findByGroup("Group2");
        Assert.assertEquals(1, ls.size());
    }
    
    /**
     * Group ve key değeri ile arama testi.
     * 
     */
    @Test
    public void test6() {

        //Group1 Anahtar1 ile 1 adet kayıt var
        List<SuggestionItem> ls = repository.findByGroupAndKey("Group1", "Anahtar1");
        Assert.assertEquals(1, ls.size());

        //Group2 Anahtar2 ile 1 adet kayıt var
        ls = repository.findByGroupAndKey("Group2", "Anahtar2");
        Assert.assertEquals(1, ls.size());
        
        //Group2 Anahtar1 ile 0 adet kayıt var
        ls = repository.findByGroupAndKey("Group2", "Anahtar1");
        Assert.assertEquals(0, ls.size());
    }
    
    /**
     * Browse criteria testi
     */
    @Test
    public void test8() {

        List<SuggestionItem> ls =repository.browseCriteria().getResultList();
        
        //Group1 Anahtar1 ile 1 adet kayıt var
        
        Assert.assertEquals(3, ls.size());

        
    }
}
