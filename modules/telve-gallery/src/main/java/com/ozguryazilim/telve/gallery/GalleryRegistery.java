package com.ozguryazilim.telve.gallery;

import java.util.ArrayList;
import java.util.List;

/**
 * Image gallery registery.
 * 
 * Farklı modüller farklı galeriler register edebilsinler diye.
 * 
 * Ayrıca aynı isimli yetki kontrolleri de tanımlanmalı.
 * 
 * @author Hakan Uygun
 */
public class GalleryRegistery {
    
    
    private static final List<String> galleries = new ArrayList<>();
    
    
    public static void register( String gallery ){
        galleries.add(gallery);
    }

    public static List<String> getGalleries() {
        return galleries;
    }
    
    
}
