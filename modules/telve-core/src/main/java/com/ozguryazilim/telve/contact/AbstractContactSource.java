/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.contact;

import java.util.List;
import java.util.Map;

/**
 * ContactSource tanımlarının taban alacağı temel sınıf.
 * 
 * @author Hakan Uygun
 */
public abstract class AbstractContactSource {
   
    /**
     * Verilen parametreleri kullanarak bulduğu Contact'ları result içerisine ekler.
     * @param params
     * @param result 
     */
    public abstract void resolve( Map<String,String> params, List<Contact> result );
}
