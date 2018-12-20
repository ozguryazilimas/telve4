package com.ozguryazilim.telve.image.ui;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.entities.FeaturePointer;
import com.ozguryazilim.telve.image.ImageService;
import com.ozguryazilim.telve.uploader.ui.FileUploadDialog;
import com.ozguryazilim.telve.uploader.ui.FileUploadHandler;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import me.desair.tus.server.TusFileUploadService;
import me.desair.tus.server.exception.TusException;
import me.desair.tus.server.upload.UploadInfo;
import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.imgscalr.Scalr;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FeatureImage ekleme, silme v.b. için Editor dialog kontrol sınıfı
 * 
 * @author Hakan Uygun
 */
@Named
@WindowScoped
public class SimpleImageEditor implements Serializable, FileUploadHandler{

    private static final Logger LOG = LoggerFactory.getLogger(SimpleImageEditor.class);
    
    @Inject
    private ImageService featureImageService;
    
    @Inject
    private FileUploadDialog fileUploadDialog;
    
    @Inject
    private TusFileUploadService fileUploadService;
    
    private FeaturePointer featurePointer;
    private String placeholder;
    private String imageId;
    
    public void openDialog( FeaturePointer featurePointer, String placeholder ){
        
        this.featurePointer = featurePointer;
        this.placeholder = placeholder;
        this.imageId = null;
        
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        //options.put("draggable", false);  
        options.put("resizable", false);
        options.put("contentHeight", 450);

        PrimeFaces.current().dialog().openDynamic("/dialogs/simpleImageEditor", options, null);
        
    }
    
    public void closeDialog(){
        featurePointer = null;
        placeholder = null;
        imageId = null;
        PrimeFaces.current().dialog().closeDynamic(null);
    }

    public void uploadFile(){
        this.imageId = null;
        fileUploadDialog.openImageDialog(this);
    }
    
    @Override
    public void handleFileUpload(String uri) {
        LOG.info("File Uploaded {}", uri);
        try {
            UploadInfo uploadInfo =  fileUploadService.getUploadInfo(uri);
            LOG.info("File Info {}", uploadInfo);
            String mime = uploadInfo.getFileMimeType();
            featureImageService.saveImage(featurePointer, fileUploadService.getUploadedBytes(uri), mime, uploadInfo.getFileName());
            //Burada otomatik thumbnail çıkarmamak lazım. Bunu da config'den ya da editor parametrelerinden v.s. almak lazım.
            saveThumbnail( uri );
            fileUploadService.deleteUpload(uri);
        } catch (IOException | TusException ex) {
            LOG.error("File Info not found", ex);
        }
    }

    protected void saveThumbnail( String uri ) throws IOException, TusException{
        
        //FIXME: aslında daha doğru bir ImageIO ile bu işi çözmek lazım.
        //Örnek : https://haraldk.github.io/TwelveMonkeys/
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try{
            BufferedImage src = ImageIO.read(fileUploadService.getUploadedBytes(uri));
            BufferedImage scaledImg = Scalr.resize(src,120);
            ImageIO.write(scaledImg, "jpeg", os);
        
            src.flush();
            scaledImg.flush();
            
            ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
            featureImageService.saveImage(featurePointer, "THUMBNAIL", is, "image/jpeg", "thumbnail.jpeg");
        
        } catch ( Exception e ){
            
            //Bazı JPEG formatlarında arıza çıkabiliyor.
            BufferedImage src = ImageIO.read(fileUploadService.getUploadedBytes(uri));
            BufferedImage scaledImg = Scalr.resize(src,120);
            ImageIO.write(scaledImg, "png", os);
        
            src.flush();
            scaledImg.flush();
            
            ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
            featureImageService.saveImage(featurePointer, "THUMBNAIL", is, "image/png", "thumbnail.png");
        }
        
        
    }
    
    public String getImageId(){
        if( Strings.isNullOrEmpty(imageId)){
            imageId = featureImageService.getImageId(featurePointer, placeholder);
        }
        return imageId;
    }
    
    public void delete(){
        this.imageId = null;
        featureImageService.deleteImage(featurePointer);
    }
    
}
