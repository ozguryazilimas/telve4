/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.bpm.handlers.ProcessHandler;
import com.ozguryazilim.telve.bpm.handlers.ProcessHandlerRegistery;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Camunda BPM Engine Startup.
 * 
 * Sistemde tanımlanmış olan süreçleri ( bpmn ) toparlayıp BPMNEngine'e deploy eder.
 * 
 * BPMN dosyaları ProcessHandler'lar da tanımlanmış processId'ler için configürasyon tanımlarında aranır.
 * 
 * Bu arama sırasında bpmn.{processId}={bpmnFile} keyleri aranır.
 * Eğer bir process handler'ın süreci başka bir jar ile değiştirilmek istenir ise configrasyon dosyasının önceliği düzenlenmelidir. Bakınız deltaSpike ConfigResolver.
 * 
 * Her uygulama ayağa kalktığında tekrar tekrar deployment yapmaması için bpmn dosyaların digestini ( şu anda md5 ) alarak KAHVE üzerinde saklar. 
 * Ardından deployment zamanında bu değerleri karşılaştırarak deployment yapıp yapmayacağına karar verir.
 * 
 * Dikkat! : Startup aşamasında kahve api'sine erişilemediğinden KAHVE tablosuna native sorgularla erişiliyor.
 * 
 * @author Hakan Uygun
 */
@Startup
@Singleton
@DependsOn("DefaultEjbProcessApplication")
public class CamundaStartup {
    
    private static final Logger LOG = LoggerFactory.getLogger(CamundaStartup.class);
    
    @Inject
    private RepositoryService repositoryService;
    
    @Inject
    private RuntimeService runtimeService;
    
    @Inject 
    private EntityManager em;

    /**
     * Starup sırasında configurasyonda tanımlı bpmn dosyaları bulup yükler.
     */
    @PostConstruct
    public void init(){
        
        for( ProcessHandler ph : ProcessHandlerRegistery.getHandlers() ){
            LOG.debug("Check change for BPMN : {}", ph.processId());
        
            String bpmnName = ConfigResolver.getProjectStageAwarePropertyValue("bpmn." + ph.processId() );
            
            if( Strings.isNullOrEmpty(bpmnName)){
                LOG.error("BPMN file configuration not found for Process {}", ph.processId() );
                continue;
            }
            
            try {
                String digest = getBpmnDigest( bpmnName );
                String storedDigest = getStoredDigest( bpmnName );
                
                //Eğer saklanmış digest ile yeni hesaplanan uyuşmuyor ise değişiklik var demektir. Deploy edelim...
                if( !digest.equals(storedDigest)){
                    //bpmnEngine'e deploy
                    this.repositoryService.createDeployment().addClasspathResource(bpmnName).deploy();
                    //Değeri bir sonraki sefer için saklayalım
                    setStoredDigest(bpmnName, digest, Strings.isNullOrEmpty(storedDigest));
                    LOG.info("BPMN deployed {} for Process {}", bpmnName, ph.processId());
                }
                
            } catch (IOException ex) {
                LOG.error("BPMN loading error", ex);
            }
        }
    }
    
    /**
     * Geriye ismi verilen bpmn resource için md5 hash döner.
     * 
     * @param bpmnName
     * @return
     * @throws IOException 
     */
    private String getBpmnDigest( String bpmnName ) throws IOException{
        InputStream is = this.getClass().getResourceAsStream("/" + bpmnName);
        if( is == null ){
            throw new IOException( "BPMN file not found : " + bpmnName);
        }
        return DigestUtils.md5Hex(is);
    }
    
    /**
     * Verilen bpmn ismi için kahve tablosunda saklanmış digest değerini döner.
     * 
     * Startup aşamasında Kahve apisine erişilemediği için ( henüz init olmadı )
     * Native sorgu ile alıyoruz.
     * 
     * @param bpmnName
     * @return 
     */
    private String getStoredDigest( String bpmnName ){
        
        try{
            Object digest = em.createNativeQuery("select KV_VAL from KAHVE where KV_KEY = :key")
                    .setParameter("key", "bpmn." + bpmnName)
                    .getSingleResult();
            return (String)digest;
        } catch ( Exception e ){
            //Aslında yapacak bişi yok. Veri tabanında gerçekten olmaya bilir.
        }
        
        return "";
    }
    
    /**
     * Verilen bpmn için KAHVE üzerindeki digest değerini günceller.
     * 
     * Startup aşamasında Kahve apisine erişilemediği için ( henüz init olmadı )
     * Native sorgu ile yapıyoruz.
     * 
     * Yeni bir deployment ise isNew true olmalı ki insert yapalım.
     * 
     * @param bpmnName
     * @param digest 
     */
    private void setStoredDigest( String bpmnName, String digest, Boolean isNew ){
        
        if( isNew ){
            em.createNativeQuery("insert into KAHVE ( KV_KEY, KV_VAL ) values ( :key, :val )")
                    .setParameter("key", "bpmn." + bpmnName)
                    .setParameter("val", digest)
                    .executeUpdate();
        } else {
            em.createNativeQuery("update KAHVE set KV_VAL = :val where KV_KEY = :key")
                    .setParameter("key", "bpmn." + bpmnName)
                    .setParameter("val", digest)
                    .executeUpdate();
        }
    }
    
}
