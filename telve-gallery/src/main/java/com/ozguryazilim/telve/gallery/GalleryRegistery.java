/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
