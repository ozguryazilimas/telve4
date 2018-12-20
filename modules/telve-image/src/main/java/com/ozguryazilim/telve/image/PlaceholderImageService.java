package com.ozguryazilim.telve.image;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.apache.deltaspike.core.util.ProxyUtils;

/**
 * Aranılan imaj Storelarda yoksa yerine kullanılabilecek imaj servisi.
 * 
 * @author Hakan Uygun
 */
@ApplicationScoped
public class PlaceholderImageService {
    
    @Inject
    @Any
    private Instance<PlaceholderImageProvider> instance;
    
    /**
     * Bütün provider'ların application context'e olmasını bekliyoruz. Dolayısı
     * ile her seferinde lookup yapmaya gerek yok.
     */
    private Map<String, PlaceholderImageProvider> providers = new HashMap<>();
    
    private PlaceholderImageProvider defaultProvider = new DefaultPlaceholderImageProvider();
    
    @PostConstruct
    public void init() {
        //Sistemdeki providerları bir cachleyelim
        for (PlaceholderImageProvider provider : instance) {
            providers.put((ProxyUtils.getUnproxiedClass(provider.getClass())).getSimpleName(), provider);
        }
    }
    
    
    protected PlaceholderImageProvider getProvider( String placeholder ){
        
        PlaceholderImageProvider proivder = providers.get(placeholder);
        if( proivder != null ){
            return proivder;
        }
        
        return defaultProvider;
    }
    
    public String getImageId( String businessKey, String placeholder ){
        //FIXME: Burada provider bulunup ondan doğru id alınması lazım.
        PlaceholderImageProvider provider = getProvider( getProviderName(placeholder));
        return (ProxyUtils.getUnproxiedClass(provider.getClass())).getSimpleName() + "::" + provider.getImageId( businessKey, placeholder);
        
    }
    
    public InputStream getImage( String imageId ){
        String[] ss = imageId.split("::");
        
        //Eğer id içerisinde doğru şey yoksa hiç aranmayalım.
        if( ss.length > 1 ){
            PlaceholderImageProvider provider = providers.get(ss[0]);
            if( provider == null ){
                return defaultProvider.getImage(ss[1]);
            }
            
            InputStream result = provider.getImage(ss[1]);
            if( result == null ){
                return defaultProvider.getImage(ss[1]);
            }
            return result;
        } 
        
        return defaultProvider.getImage("");
        
    }
    
    /**
     * Gelen placeholder bilgisini hint kısmında ayırıp sadece isim kısmını tespit eder.
     * 
     * @param placeholder
     * @return 
     */
    protected String getProviderName( String placeholder ){
        String[] ss = placeholder.split("-");
        return ss[0];
    }
}
