package com.ozguryazilim.telve.uploader.ui;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FileUpload için genel kullanım Dialog Controller.
 * 
 * @author Hakan Uygun
 */
@Named
@WindowScoped
public class FileUploadDialog implements Serializable{
    
    private static final Logger LOG = LoggerFactory.getLogger(FileUploadDialog.class);
    
    private String ownerKey;
    
    private FileUploadHandler handler;
    private String endPoint;
    private Long chunkSize;
    
    private Long maxFileSize;
    private Integer maxNumberOfFiles;
    private String allowedFileTypes;
    //zip dosyalari decompress edilsin mi?
    private boolean decompress;
    
    @PostConstruct
    public void init(){
        
        //Configde varsa onu kullanır yoksa yeni hazırlar.
        endPoint = ConfigResolver.getProjectStageAwarePropertyValue("tus.endpoint", buildEndPoint());
        LOG.debug("Selected EndPoint : {}", endPoint);
        chunkSize = Long.parseLong(ConfigResolver.getProjectStageAwarePropertyValue("tus.chunkSize", "2097152"));
    
        maxFileSize = Long.parseLong(ConfigResolver.getProjectStageAwarePropertyValue("tus.maxUploadSize", "1073741824"));
        maxNumberOfFiles = null;
        allowedFileTypes = null;
        decompress = "true".equals(ConfigResolver.getPropertyValue("auto.decompress.zip.files.default.value", "true"));
    }
    
    private String buildEndPoint(){
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        
        //ReversProxy ardındaysa bunlar lazım
        String protocol = externalContext.getRequestHeaderMap().get("x-forwarded-proto");
        String serverName = externalContext.getRequestHeaderMap().get("x-forwarded-host");
        
        if (null == protocol) {
          protocol = externalContext.getRequestScheme();
        }    
  
        if (null == serverName)  {
            serverName = externalContext.getRequestHeaderMap().get("Host");
            if( null == serverName ){
                serverName = externalContext.getRequestServerName() + ":" + externalContext.getRequestServerPort();
            }
        }
        
        String result = protocol + "://" + serverName + externalContext.getRequestContextPath() + "/tus";
        LOG.debug("Builded TUS end point : {}", result);
        
        
        return result;
    }
    
    public void openImageDialog( FileUploadHandler handler ){
        maxFileSize = 1048576L; //1MB
        maxNumberOfFiles = 1;
        allowedFileTypes = "['image/jpeg', 'image/png']";
        openDialog(handler, ownerKey);
    }
    
    public void openDialog( FileUploadHandler handler, Long maxFileSize, Integer maxNumberOfFiles, String allowedFileTypes ){
        this.maxFileSize = maxFileSize;
        this.maxNumberOfFiles = maxNumberOfFiles;
        this.allowedFileTypes = allowedFileTypes;
        openDialog(handler, ownerKey);
    }
    
    public void openDialog( FileUploadHandler handler, String ownerKey ){
        
        this.ownerKey = ownerKey;
        this.handler = handler;

        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        //options.put("draggable", false);  
        options.put("resizable", false);
        options.put("contentHeight", 480);

        PrimeFaces.current().dialog().openDynamic("/dialogs/fileUploadDialog", options, null);
        
    }
    
    public void closeDialog(){
        handler = null;
        ownerKey = null;
        maxFileSize = null;
        maxNumberOfFiles = null;
        allowedFileTypes = null;
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    
    public void fileUploaded(){
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String uri = params.get("uri");
        LOG.debug("File Upload Handler : {} {}", handler, ownerKey);
        if( handler != null ){
            handler.handleFileUpload(uri, decompress);
        }
    }

    public String getEndPoint() {
        return endPoint;
    }

    public Long getChunkSize() {
        return chunkSize;
    }

    public Long getMaxFileSize() {
        return maxFileSize;
    }

    public Integer getMaxNumberOfFiles() {
        return maxNumberOfFiles;
    }

    public String getAllowedFileTypes() {
        return allowedFileTypes;
    }

    public boolean isDecompress() {
        return decompress;
    }

    public void setDecompress(boolean decompress) {
        this.decompress = decompress;
    }
}
