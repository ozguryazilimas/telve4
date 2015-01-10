/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.contact;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * Farklı filtrelere göre ilgili kişi çözümlemesi yapar.
 * 
 * cs=user;id=*;name=!hakan;role=doctor,nurse;org=/1/2|1/3
 * cs=person;id=12345;||cs=person;tckn=12345*;wi=em,mp,p,f,a,im
 * 
 * CS türüne göre register edilen bileşenler olur ve filtreleyip geri sonuç döndürme işi onların olur. Hangi alanlara göre filtre uygulanacağını da alt bileşenler söyler.
 *
 * Sorgu dışında doğrudan yazım olabilir. O zaman cs=contact olur.
 * cs=contact;id=bişi;firstname=Hakan;lastname=Uygun;email=ccc@example.com;mobile=+9052222222||.... aynından bir daha
 * @author Hakan Uygun
 */
@ApplicationScoped
@Named
public class ContactResolver {
    
    /**
     * Verilen sorguyu kullanarak Contact listesi çözümler.
     * 
     * Sorgu formatı : 
     * 
     * cs=contactSourceName;key=value;key=value||cs=contactSourceName;key=value;key=value
     * 
     * şeklindedir. key,value değerlerinin parse edilmesinden ContactSource çözümleyici sorumludur.
     * 
     * Ex: 
     * cs=contact;id=bişi;firstname=Hakan;lastname=Uygun;email=ccc@example.com;mobile=+9052222222||.... 
     * 
     * @param query
     * @return 
     */
    public List<Contact> resolveContacts( String query ){
        
        List<Contact> result = new ArrayList<>();
                
        List<Map<String,String>> filters = parseQuery(query);
        
        //Parse edilen sorguya göre çözümleme yapıyor
        for( Map<String,String> m : filters ){
            String cs = m.get("cs");
            if( !Strings.isNullOrEmpty(cs)){
                
                if( "contact".equals(cs)){
                    //doğrudan contact nesnesi oluşturuyoruz.
                    result.add(createContact(m));
                } else {
                    //Eğer cs boşsa yapacak bişi yok
                    AbstractContactSource acs = ContactSourceRegistery.getContactSource(cs);
                    if( acs != null ){
                        acs.resolve(m, result);
                    }
                }
            }
        }
        
        return result;
    }
    
    public List<Map<String,String>> parseQuery( String query ){
        List<Map<String,String>> res = new ArrayList<>();
        
        //Önce ana gruplara ayırıyoruz.
        List<String> ls = Splitter.on("||").splitToList(query);
        
        //Şimdi alt maplare böleceğiz
        for( String s : ls ){
            Map<String,String> m = Splitter.on(';').omitEmptyStrings().trimResults().withKeyValueSeparator('=').split(s);
            res.add(m);
        }
        
        return res;
    }
    
    /**
     * ContactSource olarak "contact" verilen değerleri contact'a çevirir.
     * @param params
     * @return 
     */
    public Contact createContact( Map<String,String> params ){
        Contact c = new Contact();
        
        c.setId(params.get("id"));
        c.setFirstname(params.get("firstname"));
        c.setLastname(params.get("lastname"));
        c.setAddress(params.get("address"));
        c.setEmail(params.get("email"));
        c.setJabber(params.get("jabber"));
        c.setPhone(params.get("phone"));
        c.setMobile(params.get("mobile"));
        c.setFax(params.get("fax"));
        
        return c;
    }
}
