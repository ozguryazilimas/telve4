/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.forms;

import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.entities.EntityBase;
import com.ozguryazilim.telve.messages.FacesMessages;
import com.ozguryazilim.telve.view.PageTitleResolver;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.view.DefaultErrorView;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.core.api.scope.GroupedConversation;
import org.apache.deltaspike.core.util.ProxyUtils;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.picketlink.Identity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * View ve Edit Form controller sınıfları için taban oluşturur.
 *
 * @author Hakan Uygun
 * @param <E> Entity sınıfı
 * @param <PK> PK sınıfı
 */
public abstract class FormBase<E extends EntityBase, PK extends Long> implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(FormBase.class);

    private E entity;

    private PK id;

    @Inject
    private GroupedConversation conversation;

    @Inject
    private ViewConfigResolver viewConfigResolver;

    @Inject
    @Any
    private Identity identity;

    @Inject
    private PageTitleResolver pageTitleResolver;

    private List<String> subViewList = new ArrayList<String>();
    private String selectedSubView;

    @PostConstruct
    public void init() {
        initSubViews();
    }

    /**
     * SubView'leri toparlar ve yetkiye göre sınırlar.
     */
    protected void initSubViews() {
        //Önce mevcut ekran için registery'den subview'lar alınıyor.

        String viewId = viewConfigResolver.getViewConfigDescriptor(getContainerViewPage()).getViewId();
        List<SubView> ls = SubViewRegistery.getSubViews(viewId);
        if (ls != null) {
            for (SubView sv : ls) {

                //Eğer kullanıcının yetkisi varsa listeye ekleniyor.
                if (identity.hasPermission(sv.permission(), "select")) {
                    subViewList.add(viewConfigResolver.getViewConfigDescriptor(sv.viewPage()).getViewId());
                }
            }
        } 
    }

    /**
     * Geriye kullanılacak olan repository'i döndürür.
     *
     * @return
     */
    protected abstract RepositoryBase<E, ?> getRepository();

    /**
     * Edit formuna gider.
     *
     * TODO: Yetki kontrolü
     *
     * @return
     */
    public Class<? extends ViewConfig> edit() {
        return getEditPage();
    }

    public Class<? extends ViewConfig> close() {
        
        Class<? extends ViewConfig> result = getBrowsePage();
        conversation.close();

        //FIXME: Eger edit formunda ise view'a mı dönse ama o zaman da conversation kapatılmamalı.
        return result;
    }

    @Transactional
    public Class<? extends ViewConfig> save() {
        if (entity == null) {
            //FIXME: Duruma göre hata ya da aynı sayfa
            return DefaultErrorView.class;
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

        return getContainerViewPage();
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

    @Transactional
    public Class<? extends ViewConfig> delete() {
        if (entity == null) {
            //FIXME: Duruma göre aynı sayfa ya da hata
            return DefaultErrorView.class;
        }

        //try {
        getRepository().remove(entity);
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

        return getBrowsePage();
    }

    public E getEntity() {
        if (entity == null) {
            createNew();
        }
        return entity;
    }

    public void setEntity(E entity) {
        this.entity = entity;
        LOG.debug("Entity Setlendi : {0}", entity);
    }

    /**
     * Aslında bu methodu Repository'e taşımak lazım.
     *
     * @return
     */
    protected E getNewEntity(){
        try {
            return getRepository().createNew();
        } catch (InstantiationException | IllegalAccessException ex) {
            LOG.error("Error on Instantiation");
            //FIXME: Hata sayfasına yönlendirelim...
            return null;
        }
    }

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
        save();
        createNew();
        //Aynı sayfada klamak lazım.
        return null;
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
    public String getSelectedSubView() {
        return selectedSubView;
    }

    public void setSelectedSubView(String selectedSubView) {
        this.selectedSubView = selectedSubView;
    }

    /**
     * Seçili olan SubView'in ViewID'sini döndürür.
     *
     * @return
     */
    public String getSubViewId() {
        if (selectedSubView != null) {
            return selectedSubView;
        }

        return getMasterSubViewId();
    }

    /**
     * View ekranında ilk gösterilecek olan sub view id sini döndürür. Alt
     * sınıflar tarafından ezilip sayfa id sinin döndürülmesi gerekir.
     *
     * @return Geriye gösterilecek olan view ID döner.
     */
    public String getMasterSubViewId() {
        return viewConfigResolver.getViewConfigDescriptor(getMasterViewPage()).getViewId();
    }

    public PK getId() {
        return id;
    }

    public void setId(PK id) {
        if (entity != null && id.equals(this.id)) {
            return;
        } //Zaten bu conv için entitymiz var elimizde...

        LOG.debug("ID ile setleniyor. ID : {} ", id);

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

    /**
     * Geriye FormEdit annotation'ı ile tanımlanmış BrowsePage'i döndürür.
     *
     * @return
     */
    public Class<? extends ViewConfig> getBrowsePage() {
        return ((FormEdit)(ProxyUtils.getUnproxiedClass(this.getClass()).getAnnotation(FormEdit.class))).browsePage();
        //return this.getClass().getAnnotation(FormEdit.class).browsePage();
    }

    /**
     * Geriye FormEdit annotation'ı ile tanımlanmış EditPage'i döndürür.
     *
     * @return
     */
    public Class<? extends ViewConfig> getEditPage() {
        return ((FormEdit)(ProxyUtils.getUnproxiedClass(this.getClass()).getAnnotation(FormEdit.class))).editPage();
        //return this.getClass().getAnnotation(FormEdit.class).editPage();
    }

    /**
     * Geriye FormEdit annotation'ı ile tanımlanmış EditPage'i döndürür.
     *
     * @return
     */
    public Class<? extends ViewConfig> getContainerViewPage() {
        return ((FormEdit)(ProxyUtils.getUnproxiedClass(this.getClass()).getAnnotation(FormEdit.class))).viewContainerPage();
        //return this.getClass().getAnnotation(FormEdit.class).viewContainerPage();
    }

    /**
     * Geriye FormEdit annotation'ı ile tanımlanmış EditPage'i döndürür.
     *
     * @return
     */
    public Class<? extends ViewConfig> getMasterViewPage() {
        return ((FormEdit)(ProxyUtils.getUnproxiedClass(this.getClass()).getAnnotation(FormEdit.class))).masterViewPage();
        //return this.getClass().getAnnotation(FormEdit.class).masterViewPage();
    }

    public List<String> getSubViewList() {
        return subViewList;
    }

    public String getPageTitle(String viewId) {
        return pageTitleResolver.getPageTitle(viewId);
    }
}
