/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.user;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.auth.AbstractIdentityHomeExtender;
import com.ozguryazilim.telve.auth.UserModel;
import com.ozguryazilim.telve.auth.UserModelRegistery;
import com.ozguryazilim.telve.config.TelveConfigResolver;
import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.forms.ParamBase;
import com.ozguryazilim.telve.forms.ParamEdit;
import com.ozguryazilim.telve.idm.entities.User;
import com.ozguryazilim.telve.sequence.SequenceManager;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kullanıcı tanımlama ekranı.
 * 
 * @author Hakan Uygun
 */
@ParamEdit
public class UserHome extends ParamBase<User, Long>{
    
    private static final Logger LOG = LoggerFactory.getLogger(UserHome.class);

    @Inject
    private ViewConfigResolver viewConfigResolver;
    
    @Inject
    private UserRepository repository;

    @Inject
    private TelveConfigResolver telveConfigResolver;

    @Inject
    private SequenceManager sequenceManager;

    private String password;
    
    private List<String> fragments;
    private List<AbstractIdentityHomeExtender> extenders;

    @Override
    public boolean onBeforeSave() {
        if (Strings.isNullOrEmpty(getEntity().getCode())) {
            String cc = telveConfigResolver.getProperty("serial.core.param.User");
            if (cc != null) {
                getEntity().setCode(sequenceManager.getNewSerialNumber(cc, 6));
            }
        }
        
        if( !Strings.isNullOrEmpty(password)){
            //FIXME: Burada password hash ve salt işlemleri yapılacak
            getEntity().setPasswordEncodedHash(password);
            getEntity().setPasswordSalt(password);
            
        }
        
        return true;
    }
    

    /**
     * Geriye ek model UI fragmentlerinin listesi döner.
     *
     * @return
     */
    public List<String> getUIFragments() {

        if (fragments == null) {
            populateFragments();
        }

        LOG.info("UI Fragments : {}", fragments);

        return fragments;
    }

    /**
     * UI için gerekli fragment listesini hazırlar.
     */
    protected void populateFragments() {
        fragments = new ArrayList<>();

        for (UserModel m : UserModelRegistery.getUserModelMap().values()) {
            fragments.add(viewConfigResolver.getViewConfigDescriptor(m.fragment()).getViewId());
        }
    }

    /**
     * Geriye extender CDI bileşen listesini döndürür.
     *
     * @return
     */
    public List<AbstractIdentityHomeExtender> getExtenders() {
        if (extenders == null) {
            extenders = UserModelRegistery.getExtenders();
        }
        return extenders;
    }
    
    /**
     * Geriye kullanıcı tiplerini döndürür.
     *
     * @return
     */
    public List<String> getUserTypes() {
        return UserModelRegistery.getUserTypes();
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    protected RepositoryBase<User, ?> getRepository() {
        return repository;
    }    
}
