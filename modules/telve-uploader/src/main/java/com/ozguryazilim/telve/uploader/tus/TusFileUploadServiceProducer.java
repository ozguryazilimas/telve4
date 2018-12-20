package com.ozguryazilim.telve.uploader.tus;

import java.io.File;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import me.desair.tus.server.TusFileUploadService;
import org.apache.commons.io.FileUtils;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TusFileUploadService producer
 * @author Hakan Uygun
 */
@ApplicationScoped
public class TusFileUploadServiceProducer {
    
    private static final Logger LOG = LoggerFactory.getLogger(TusFileUploadServiceProducer.class);
    
    @Inject 
    private ServletContext context;
    
    /**
     * Application Scope için TusFileUploadService CDI bean oluşturur.
     * 
     * Config bilgileri :
     *  FIXME: config yazılacak
     * @return 
     */
    @Produces
    @ApplicationScoped
    public TusFileUploadService getFileUploadService(){
        //FIXME: Burada configden çeşitli bilgiler alınarak bu service init edilecek
    
        String uploadURI = context.getContextPath() + "/tus";
        
        String storagePath = FileUtils.getTempDirectoryPath() + File.separator + "tus";
        storagePath = ConfigResolver.getProjectStageAwarePropertyValue("tus.storagePath", storagePath);
        
        Long expirationPeriod = Long.parseLong(ConfigResolver.getProjectStageAwarePropertyValue("tus.expirationPeriod", "300000"));
        Long maxUploadSize = Long.parseLong(ConfigResolver.getProjectStageAwarePropertyValue("tus.maxUploadSize", "1073741824"));
        Long chunkSize = Long.parseLong(ConfigResolver.getProjectStageAwarePropertyValue("tus.chunkSize", "2097152"));
        
        TusFileUploadService result = new TusFileUploadService()
                .withStoragePath(storagePath)
                .withMaxUploadSize(maxUploadSize)
                .withUploadURI(uploadURI)
                .withUploadExpirationPeriod(expirationPeriod);
                //.withThreadLocalCache(true);
        
                
        
        LOG.info("TUS Service inited");
        LOG.info("TUS= Extentions : {}; Upload URI: {}; Storage Path: {}; Expiration Period: {}; Max Upload Size : {}; Chunk Size : {}", result.getEnabledFeatures(), uploadURI, storagePath, expirationPeriod, maxUploadSize, chunkSize);
                
        return result;
    }
    
}
