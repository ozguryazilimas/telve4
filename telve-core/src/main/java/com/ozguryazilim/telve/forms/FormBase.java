/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.forms;

import com.ozguryazilim.telve.messages.FacesMessages;
import java.io.Serializable;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.scope.GroupedConversation;
import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * View ve Edit Form controller sınıfları için taban oluşturur.
 *
 * @author Hakan Uygun
 * @param <E> Entity sınıfı
 * @param <PK> PK sınıfı
 */
public abstract class FormBase<E, PK extends Long> implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(FormBase.class);

    private E entity;

    private String selectedSubView;
    
    private PK id;

    @Inject
    private GroupedConversation conversation;
    
    /**
     * Geriye kullanılacak olan repository'i döndürür.
     *
     * @return
     */
    protected abstract AbstractEntityRepository<E, PK> getRepository();
    
    public Class<? extends ViewConfig> edit() {
        //FIXME: Edit View dönmeli
        return null;
    }

    public Class<? extends ViewConfig> close() {
        conversation.close();

        //FIXME: Duruma göre browse ya da view dönmeli
        return null;
    }

    public Class<? extends ViewConfig> save() {
        if (entity == null) {
            //FIXME: Duruma göre hata ya da aynı sayfa
            return null;
        }

        //try {
            beforeSave();

            getRepository().saveAndFlush(entity);
            
            postSave();
        //} catch (EntityExistsException e) {
        //    log.debug("Hata : #0", e);
        //    facesMessages.add("#{messages['general.message.record.NotUnique']}");
        //    return BaseConsts.FAIL;
        //}

        LOG.debug("Entity Saved : {0} ", entity);
        FacesMessages.info("#{messages['general.message.record.SaveSuccess']}");

        raiseRefreshBrowserEvent();
        
        //FIXME: Duruma göre view ya da browse
        return null;
    }

    /**
     * Entity kaydedilmeden hemen önce atl sınıflar birşey yapmak isterlerse bu
     * methodu override edebilirler...
     */
    public void beforeSave() {
        //Alt sınıflar için 
    }

    /**
     * Entity kaydedildikten hemen sonra atl sınıflar birşey yapmak isterlerse
     * bu methodu override edebilirler...
     */
    public void postSave() {
        //Alt sınıflar için 
    }

    public void reattachRequiredEntities() {
        //TODO: Bu ne işe yarıyordu. Belgelemek lazım.
    }

    protected void raiseRefreshBrowserEvent() {
        //FIXME: Yeni nesil event işlerine bakılacak
        //events.raiseTransactionSuccessEvent("refreshBrowser:" + getEntityClass().getName());
        //log.debug("mesaj : refreshBrowser:" + getEntityClass().getName());
    }

    
    public Class<? extends ViewConfig> delete() {
        if (entity == null) {
            //FIXME: Duruma göre aynı sayfa ya da hata
            return null;
        }

        //try {
            getRepository().removeAndFlush(entity);
        //} catch (Exception e) {
        //    log.debug("Hata : #0", e);
        //    facesMessages.add("#{messages['general.message.record.DeleteFaild']}");
        //    return BaseConsts.FAIL;
        //}

        LOG.debug("Entity Removed : {0} ", entity);
        entity = null;
        FacesMessages.info("#{messages['general.message.record.DeleteSuccess']}");

        raiseRefreshBrowserEvent();

        conversation.close();
        //FIXME: Browse'a geri dönmeli
        return null;
    }


    public E getEntity() {
        if( entity == null ){
            createNew();
        }
        return entity;
    }

    public void setEntity(E entity) {
        this.entity = entity;
        LOG.debug("Entity Setlendi : {0}", entity);
    }

    /**
     * TODO: Aslında bu methodu Repository'e taşımak lazım.
     *
     * @return
     */
    protected abstract E getNewEntity();

    public void createNew() {
        entity = getNewEntity();
        LOG.debug("evet yeni bi tane şey ettik...");

    }

    /**
     * Alt sınıfların clonlama sonra yapacağı iişler için override edilmesi
     * gerekir.
     */
    public void postClone() {
        //Alt sınıflarda kullanılır.
    }

    /**
     * Entity yüklendikten hemen sonra atl sınıflar birşey yapmak isterlerse bu
     * methodu override edebilirler...
     */
    public void postLoad() {
        //Alt sınıflar için 
    }

    public Class<? extends ViewConfig> saveAndNew() {
        Class<? extends ViewConfig> s = save();
        createNew();
        return null;
    }

    public void edit(E e) {
        LOG.debug("Edit edicedik : {}", e);
        entity = e;
    }

    public void delete(E e) {
        entity = e;
        delete();
    }

    /* FIXME: eid=0 ile yeni entity?
     public Long getId() {
     return id;
     }

     public void setId(Long id) {

     if (entity != null) {
     return;
     } //Zaten bu conv için entitymiz var elimizde...

     this.id = id;
     log.debug("ID ile setleniyor. ID : {0} ", id);

     if (id == null || id == 0) {
     createNew();
     } else {
     entity = getEntityManager().find(getEntityClass(), id);

     if (entity == null) {
     facesMessages.add("İstenilen kayıt bulunamadı. Lütfen kontrol edip tekarar deneyiniz.");
     createNew();
     } else {
     //Entity başarıyla yüklendi. Alt sınıflar bu aşamada birşey yapmak isterlerse postLoad() methodunu override edebilirler...
     postLoad();
     }
     }
     }*/
    /**
     * Verilen kaynak id'li olan veriden bir kopya çıkartır.
     *
     * @param sid
     */
    /* FIXME: Clonlama becerisine bir bakalım...
     @SuppressWarnings("unchecked")
     public void setSid(Long sid) {
     //Yani eid 0 verilmiş ve yeni entity oluşturulmuş demek.
     if (sid != null && sid != 0 && (entity == null || entityId() == null || entityId() == 0)) {

     E src = entityManager.find(getEntityClass(), sid);
     //Şimdide üzerine kopya alalım
     entity = (E) EntityCloner.cloneEntity(src);
     postClone();
     }
     this.sid = sid;
     }
     */
    /* FIXME: SubView meselesini çözmek lazım
    public SubView getSelectedSubView() {
        return selectedSubView;
    }

    public void setSelectedSubView(SubView selectedSubView) {
        this.selectedSubView = selectedSubView;
    }

    public String getSubViewId() {
        if (selectedSubView != null) {
            return selectedSubView.view();
        }

        return getMasterSubViewId();
    }
    */

    /**
     * View ekranında ilk gösterilecek olan sub view id sini döndürür. Alt
     * sınıflar tarafından ezilip sayfa id sinin döndürülmesi gerekir.
     */
    public String getMasterSubViewId() {
        //FIXME: Bunu annotation üzerinden yapacağız...
        return null;
    }

    public PK getId() {
        return id;
    }

    public void setId(PK id) {
        if (entity != null) {
            return;
        } //Zaten bu conv için entitymiz var elimizde...

        this.id = id;
        LOG.debug("ID ile setleniyor. ID : {0} ", id);

        if (id == null || id == 0) {
            createNew();
        } else {
            entity = getRepository().findBy(id);

            if (entity == null) {
                FacesMessages.error("İstenilen kayıt bulunamadı. Lütfen kontrol edip tekarar deneyiniz.");
                createNew();
            } else {
            	//Entity başarıyla yüklendi. Alt sınıflar bu aşamada birşey yapmak isterlerse postLoad() methodunu override edebilirler...
            	postLoad();
            }
        }
        this.id = id;
    }

    
}
