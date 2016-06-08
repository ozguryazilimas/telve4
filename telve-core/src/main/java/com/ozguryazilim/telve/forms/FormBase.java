/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.forms;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.annotations.BizKey;
import com.ozguryazilim.telve.audit.AuditLogCommand;
import com.ozguryazilim.telve.audit.AuditLogger;
import com.ozguryazilim.telve.auth.ActiveUserLookup;
import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.entities.EntityBase;
import com.ozguryazilim.telve.messages.FacesMessages;
import com.ozguryazilim.telve.view.PageTitleResolver;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import org.apache.deltaspike.core.api.config.ConfigResolver;
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

    private Boolean needCreateNew = false;
    
    @Inject
    private GroupedConversation conversation;

    @Inject
    private ViewConfigResolver viewConfigResolver;

    @Inject
    @Any
    private Identity identity;

    @Inject
    private ActiveUserLookup userLookup;
    
    @Inject
    private AuditLogger auditLogger;
    
    @Inject
    private PageTitleResolver pageTitleResolver;

    @Inject
    private Event<SubViewSelectEvent> subViewEvent;
    
    @Inject
    private Event<RefreshBrowserEvent> refreshBrowserEvent;
    
    @Inject
    private FacesContext facesContext;

    
    private List<String> subViewList = new ArrayList<String>();
    private Map<String,List<String>> subViews = new HashMap<>();
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
        List<SubView> ls = SubViewRegistery.getSubViews(getContainerViewPage().getName());
        if (ls != null) {
            for (SubView sv : ls) {
                //Eğer kullanıcının yetkisi varsa listeye ekleniyor.
                if( "true".equals(ConfigResolver.getPropertyValue("permission.exclude." + sv.permission(), "false"))){
                    continue;
                }
                
                if (identity.hasPermission(sv.permission(), "select")) {
                    subViewList.add(viewConfigResolver.getViewConfigDescriptor(sv.viewPage()).getViewId());
                    
                    //Grup tanımına göre subviewları dolduruyoruz.
                    List<String> vs = subViews.get(sv.group());
                    if( vs == null ){
                        vs = new ArrayList<>();
                        subViews.put(sv.group(), vs);
                    }
                    
                    vs.add(viewConfigResolver.getViewConfigDescriptor(sv.viewPage()).getViewId());
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
    
    public void refresh(){
        PK eid = this.id;
        entity = null;
        setId(eid);
    }

    /**
     * Yeni veri girişi için formu hazırlar.
     * 
     * Veri girişi hazırlandıktan sonra editPage'e yönlendirir.
     * 
     * @return 
     */
    public Class<? extends ViewConfig> create() {
        createNew();
        return getEditPage();
    }
    
    @Transactional
    public Class<? extends ViewConfig> save() {
        if (entity == null) {
            //FIXME: Duruma göre hata ya da aynı sayfa
            return DefaultErrorView.class;
        }

        try {
            if( !onBeforeSave() ) return null;

            String act = entity.getId() == null ? AuditLogCommand.ACT_INSERT : AuditLogCommand.ACT_UPDATE;
            
            entity = getRepository().saveAndFlush(entity);

            auditLogger.actionLog(entity.getClass().getSimpleName(), entity.getId(), getBizKeyValue(), AuditLogCommand.CAT_ENTITY, act, userLookup.getActiveUser().getLoginName(), "" );
            
            //Save'den sonra elde sakladığımız id'yi değiştirelim ki bir sonraki request için ortalık karışmasın ( bakınız setId )
            this.id = (PK) entity.getId();
        
            onAfterSave();
        } catch (EntityExistsException e) {
            LOG.error("Hata : Not Unique", e);
            FacesMessages.warn("#{messages['general.message.record.NotUnique']}");
            return null;
        } catch ( Exception e ){
            //FIXME: Asıl detay hatanın bulunması lazım.
            LOG.error("Hata : Constraint Violation", e);
            FacesMessages.warn("#{messages['general.message.record.ConstraintViolation']}");
            return null;
        }

        LOG.debug("Entity Saved : {0} ", entity);
        FacesMessages.info( "general.message.record.SaveSuccess");

        raiseRefreshBrowserEvent( getEntity().getId());

        return getContainerViewPage();
    }

    /**
     * Entity üzerinde @BizKey annotation'ını bulunana field değerini döner.
     * 
     * İstenilir ise form için override edilebilir.
     * 
     * @return 
     */
    protected String getBizKeyValue(){
        
        String result = "";
        
        Field[] fields = entity.getClass().getDeclaredFields();
        
        for( Field f : fields ){
            if( f.isAnnotationPresent(BizKey.class) ){
                try {
                    f.setAccessible(true);
                    result += f.get(entity).toString();
                } catch (IllegalArgumentException ex) {
                    LOG.debug("BizKey not found", ex);
                } catch (IllegalAccessException ex) {
                    LOG.debug("BizKey not found", ex);
                }
            }
        }
        
        return result;
    }
    
    /**
     * Entity kaydedilmeden hemen önce atl sınıflar birşey yapmak isterlerse bu
     * methodu override edebilirler...
     * @return 
     */
    public boolean onBeforeSave() {
        //Alt sınıflar için 
        return true;
    }

    /**
     * Entity kaydedildikten hemen sonra atl sınıflar birşey yapmak isterlerse
     * bu methodu override edebilirler...
     * @return 
     */
    public boolean onAfterSave() {
        //Alt sınıflar için 
        return true;
    }
    
    /**
     * Entity silinmeden hemen önce atl sınıflar birşey yapmak isterlerse bu
     * methodu override edebilirler...
     * @return 
     */
    protected boolean onBeforeDelete() {
        //Alt sınıflar için 
        return true;
    }

    /**
     * Entity silindikten hemen sonra atl sınıflar birşey yapmak isterlerse
     * bu methodu override edebilirler...
     * @return 
     */
    protected boolean onAfterDelete() {
        //Alt sınıflar için 
        return true;
    }

    public void reattachRequiredEntities() {
        //TODO: Bu ne işe yarıyordu. Belgelemek lazım.
    }

    protected void raiseRefreshBrowserEvent( Long id ) {
        refreshBrowserEvent.fire(new RefreshBrowserEvent(getRepository().getEntityClass().getName(), id ));
    }

    @Transactional
    public Class<? extends ViewConfig> delete() {
        if (entity == null) {
            //FIXME: Duruma göre aynı sayfa ya da hata
            return DefaultErrorView.class;
        }

        Long oldid = entity.getId();
        try {
            
            if( !onBeforeDelete() ) return null;
            
            auditLogger.actionLog(entity.getClass().getSimpleName(), entity.getId(), getBizKeyValue(), AuditLogCommand.CAT_ENTITY, AuditLogCommand.ACT_DELETE, userLookup.getActiveUser().getLoginName(), "" );
            //getRepository().deleteById(entity.getId());
            getRepository().remove(entity);
            
            onAfterDelete();
        } catch (Exception e) {
            LOG.debug("Hata : {}", e);
            FacesMessages.error("#{messages['general.message.record.DeleteFaild']}");
            return null;
        }

        LOG.debug("Entity Removed : {0} ", entity);
        entity = null;
        FacesMessages.info("general.message.record.DeleteSuccess");

        raiseRefreshBrowserEvent( oldid );

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
    public boolean onAfterClone() {
        //Alt sınıflarda kullanılır.
        return true;
    }

    /**
     * Entity yüklendikten hemen sonra atl sınıflar birşey yapmak isterlerse bu
     * methodu override edebilirler...
     */
    public boolean onAfterLoad() {
        //Alt sınıflar için 
        return true;
    }

    public Class<? extends ViewConfig> saveAndNew() {
        save();
        createNew();
        //Aynı sayfada klamak lazım.
        return null;
    }

    
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
        subViewEvent.fire(new SubViewSelectEvent(selectedSubView));
    }

    /**
     * Seçili olan SubView'in ViewID'sini döndürür.
     *
     * @return
     */
    public String getSubViewId() {
        //Eğer requestEdilen bir subview varsa onu bir işleme alalım.
        checkRequestedSubView();
        
        //Eğer seçili subView varsa onu döndürelim
        if ( !Strings.isNullOrEmpty(selectedSubView) ) {
            return selectedSubView;
        }

        //Default subview dönecek
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

    /**
     * Geriye SubView grup listesini döndürür.
     * @return 
     */
    public List<String> getSubViewGroups(){
        List<String> grps = new ArrayList( subViews.keySet());
        //Main dışındakileri veriyoruz.
        grps.remove("main");
        return grps;
    }
    
    /**
     * İsmi verilen gruptaki subview listesini döndürür.
     * 
     * @param grp
     * @return 
     */
    public List<String> getSubViews( String grp ){
        return subViews.get(grp);
    }
    
    public PK getId() {
        return id;
    }

    public void setId(PK id) {
        if (entity != null && id != -1 && id.equals(this.id) && !getNeedCreateNew()) {
            
            if( isRequestNeedRefresh()){
                refresh();
            }
            
            return;
        } //Zaten bu conv için entitymiz var elimizde...

        LOG.debug("ID ile setleniyor. ID : {} ", id);

        if ( id == null || id == 0 || id == -1 || ( getNeedCreateNew() && id <= 0 )) {
            createNew();
        } else {
            entity = getRepository().findBy(id);

            if (entity == null) {
                FacesMessages.error("general.message.record.NotFound");
                createNew();
            } else {
                //Entity başarıyla yüklendi. Alt sınıflar bu aşamada birşey yapmak isterlerse postLoad() methodunu override edebilirler...
                onAfterLoad();
            }
        }
        this.id = ( id == -1 ? (PK)new Long(0) : id );
        selectedSubView = "";
        needCreateNew = false;
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

    /**
     * Bu method altsınıflar tarafından override edilerek kullanılır.
     * Yeni bir entity oluşturma ihtiyacı olup olmadığını kontrol etmek için kullanılır.
     * Eğer varsa setNeedCreateNew ile değişken setlenir.
     */
    protected void checkNeedCreateNew(){
        
    }
    
    protected Boolean getNeedCreateNew() {
        return needCreateNew;
    }

    protected void setNeedCreateNew(Boolean needCreateNew) {
        this.needCreateNew = needCreateNew;
    }

    /**
     * Eğer sayfaya gelen request edit ya da container View'den gelmiyorsa verileri tazeleyelim.
     * 
     * Bu fonksiyon conversation açık olduğu için browse'dan tekrar gelindiğinde verilerin tazelenmemesini engelleyecek.
     * 
     * @return 
     */
    private boolean isRequestNeedRefresh() {
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        
        String ref = request.getHeader("Referer");
        if( !Strings.isNullOrEmpty(ref)){
            //TODO: Burada aslında BrowsePage'in ismine bakmak daha makul sanırım.
            //Ya da edit/view page'ler dışında mı diye de bakılabilir.

            String editView = viewConfigResolver.getViewConfigDescriptor(getEditPage()).getViewId();
            String containerView = viewConfigResolver.getViewConfigDescriptor(getContainerViewPage()).getViewId();

            editView = editView.replace(".xhtml", ".jsf");
            containerView = containerView.replace(".xhtml", ".jsf");
            
            return !(ref.contains(editView) || ref.contains(containerView));
        }
        return false;
    }
    
    /**
     * Request olarak gelen subView parametresine bakarak selectedSubView setler
     */
    private void checkRequestedSubView(){
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        String subView = request.getParameter("subView");

        //Eğer bir parametre gelmemiş ise çıkalım
        if(Strings.isNullOrEmpty(subView)) return;
        
        //Gelen parametre tanıdığımız ( ve yetkimiz olan ) bir subView ise setliyoruz. sonda .xhtml yazılmasına gerek kalmasın
        for( String sv : subViewList ){
            if( sv.startsWith(subView)){
                //Eğer zaten seçili olan o değilse setleyelim.
                if( !sv.equals(selectedSubView)){
                    setSelectedSubView(sv);
                }
            }
        }
        
    }
    
    
}
