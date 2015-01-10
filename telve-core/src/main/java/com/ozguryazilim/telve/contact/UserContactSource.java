/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.contact;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.model.basic.User;
import org.picketlink.idm.query.Condition;
import org.picketlink.idm.query.IdentityQueryBuilder;

/**
 * User bilgisi üzerinden contact resolve eder.
 * @author Hakan Uygun
 */
@ContactSource(name = "user")
public class UserContactSource extends AbstractContactSource{

    private static final String ID = "ID";
    private static final String USERNAME = "username";
    private static final String ROLE = "role";
    private static final String USER_TYPE = "userType";
    
    
    @Inject
    private IdentityManager identityManager;
    
    /**
     * Kabul edilen parametreler : 
     * 
     * ID, username, role, usertype
     * 
     * @param params
     * @param result 
     */
    @Override
    public void resolve(Map<String, String> params, List<Contact> result) {
        
        IdentityQueryBuilder iqb = identityManager.getQueryBuilder();
        
        List<Condition> conditions = new ArrayList<>();
        
        //Gelen parametrelere bakarak kullanıcı sorgusu yapacağız
        String p = params.get(ID);
        if( !Strings.isNullOrEmpty(p)){
            //Burada user lookup üzerinden sorgu çekilecek olmadı IdentityManager'dan
            Condition c = iqb.equal(User.ID, p);
            conditions.add(c);
        }
        
        
        p = params.get(USERNAME);
        if( !Strings.isNullOrEmpty(p)){
            //Burada user lookup üzerinden sorgu çekilecek olmadı IdentityManager'dan
            Condition c = iqb.equal(User.LOGIN_NAME, p);
            conditions.add(c);
        }
        
        p = params.get(ROLE);
        if( !Strings.isNullOrEmpty(p)){
            //Burada user lookup üzerinden sorgu çekilecek olmadı IdentityManager'dan
        }
        
        //Sorgu ile kullanıcı listesi alınıyor
        List<User> users = iqb.createIdentityQuery(User.class).where(conditions.toArray(new Condition[]{})).getResultList();
        
        //FIXME: Picketlink Ek alanları için bir genişleme düşünülmeli. Bakınız UserModelExtentions...
        //Kullanıcı bilgisinden contact oluşturulup result'a ekleniyor.
        for( User u : users ){
            Contact c = new Contact();
            c.setId(u.getId());
            c.setFirstname(u.getFirstName());
            c.setLastname(u.getLastName());
            c.setEmail(u.getEmail());
            result.add(c);
        }
    }
    
}
