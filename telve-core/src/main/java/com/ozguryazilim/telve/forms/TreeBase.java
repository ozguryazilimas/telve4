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
import com.ozguryazilim.telve.data.TreeRepositoryBase;
import com.ozguryazilim.telve.entities.TreeNodeEntityBase;
import com.ozguryazilim.telve.lookup.LookupTreeModel;
import com.ozguryazilim.telve.lookup.TreeNodeTypeSelector;
import com.ozguryazilim.telve.messages.FacesMessages;
import com.ozguryazilim.telve.utils.TreeUtils;
import com.ozguryazilim.telve.view.Pages;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import javax.enterprise.event.Event;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.scope.GroupedConversation;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ağaç tipi parametre veri girişleri taban kontrol sınıfı
 *
 * @author Hakan Uygun
 * @param <E> TreeNodeEntityBase'den türemiş bir entity sınıfı
 */
public abstract class TreeBase< E extends TreeNodeEntityBase> implements TreeNodeTypeSelector<E>, Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(TreeBase.class);

    private LookupTreeModel<E> treeModel;
    private E entity;
    private List<E> entityList;

    private String filter;
    
    @Inject
    private Event<RefreshBrowserEvent> refreshBrowserEvent;

    @Inject
    private GroupedConversation conversation;

    @Inject
    private Subject idendity;
    
    @Inject
    private AuditLogger auditLogger;
    
    /**
     * Geriye kullanılacak olan repository'i döndürür.
     *
     * @return
     */
    protected abstract TreeRepositoryBase<E> getRepository();

    public List<E> getEntityList() {
        LOG.debug("super.getEntityList");
        if (entityList == null) {
            populateData();
        }
        return entityList;
    }

    /**
     * Yeni bir entity üretir. Bu method alt sınıflar tarafından override edilip
     * ek özellikleri olan bir entity döndürülebilir. Ama aynı şekilde
     * kullanılan repository'de de yapılabilir. Orada yapılması tavsiye edilir.
     *
     * @return
     */
    protected E getNewEntity() {
        try {
            return getRepository().createNew();
        } catch (InstantiationException | IllegalAccessException ex) {
            LOG.error("Error on Instantiation");
            //FIXME: Hata sayfasına yönlendirelim...
            return null;
        }
    }

    public void createNewChild() {
        E newE = getNewEntity();
        newE.setParent(entity != null && entity.isPersisted() ? entity : null);
        entity = newE;
    }

    public void createNewSibling() {
        E newE = getNewEntity();
        newE.setParent(entity != null ? entity.getParent() : null);
        entity = newE;
    }

    public Class<? extends ViewConfig> saveAndNew() {
        Class<? extends ViewConfig> result = save();
        createNewSibling();
        return result;
    }

    public void edit(E e) {
        LOG.debug("Edit edicedik : {}", e);
        entity = e;
    }

    public void delete(E e) {
        entity = e;
        delete();
    }

    @Transactional
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Class<? extends ViewConfig> save() {
        if (entity == null) {
            return Pages.Home.class;
        }

        if (!onBeforeSave()) {
            return null;
        }

        String act = entity.isPersisted() ? AuditLogCommand.ACT_INSERT : AuditLogCommand.ACT_UPDATE;
        
        if (!entity.isPersisted()) {
            //Unique Code olup olmadığını bir kontrol edelim...
            List<E> ls = getRepository().findByCode(entity.getCode());
            if (!ls.isEmpty()) {
                FacesMessages.error("general.message.record.CodeNotUnique");
                return null;
            }
        }

        //boolean newRecord = !entity.isPersisted();
        //Path saklamak için ID'nin alınmaya ihtiyacı var o yüzden eğer persist değilse önce bir ID için kaydediyoruz.
        if (!entity.isPersisted()) {
            getRepository().save(entity);
        }
        entity.setPath(TreeUtils.getNodeIdPath(entity));
        getRepository().save(entity);

        auditLogger.actionLog(entity.getClass().getSimpleName(), entity.getId(), getBizKeyValue(), AuditLogCommand.CAT_PARAM, act, idendity.getPrincipal().toString(), "" );
        
        if (!getEntityList().contains(entity)) {
            getEntityList().add(entity);
            getTreeModel().addItem(entity);
        } else {
            getTreeModel().updateItem(entity);
        }

        if (!onAfterSave()) {
            return null;
        }

        LOG.debug("Entity Saved : {} ", entity);

        FacesMessages.info("general.message.record.SaveSuccess");

        refreshEntityList();
        
        raiseRefreshBrowserEvent( getEntity().getId());

        return null;
    }

    @Transactional
    public Class<? extends ViewConfig> delete() {
        if (entity == null) {
            return Pages.Home.class;
        }

        onBeforeDelete();

        //FIXME: Eğer ağacın alt dalları varsa diye kontrol edilmesi lazım...
        try {
            
            auditLogger.actionLog(entity.getClass().getSimpleName(), entity.getId(), getBizKeyValue(), AuditLogCommand.CAT_PARAM, AuditLogCommand.ACT_DELETE, idendity.getPrincipal().toString(), "" );
            
            getRepository().deleteById(entity.getId());
            //getRepository().remove(entity);
            getTreeModel().removeItem(entity);
            //Listeden de çıkaralım
            getEntityList().remove(entity);

        } catch (Exception e) {
            LOG.error("Hata : {}", e);
            FacesMessages.error("general.message.record.DeleteFaild");
            return null;
        }

        LOG.debug("Entity Removed : {} ", entity);

        onAfterDelete();

        //Mevcut silindi dolayısı ile null verdik
        entity = null;

        FacesMessages.info("general.message.record.DeleteSuccess");

        refreshEntityList();

        raiseRefreshBrowserEvent( getEntity().getId());
        
        return null;
    }

    /**
     * TODO: Burada GroupConversationScope kapatılacak
     *
     * @return
     */
    public Class<? extends ViewConfig> close() {
        conversation.close();
        return Pages.Home.class;
    }

    /**
     * Kayıt işlemlerinden sonra çağırılır.
     *
     * Eğer isteniyor ise listenin tekrardan çelkilmesi içindir.
     *
     */
    public void refreshEntityList() {

    }
    
    protected void raiseRefreshBrowserEvent( Long id ) {
        refreshBrowserEvent.fire(new RefreshBrowserEvent(getRepository().getEntityClass().getName(), id ));
    }

    public E getEntity() {
        if (entity == null) {
            createNewSibling();
        }
        return entity;
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }

    public void initTreeModel() {
        treeModel = new LookupTreeModel<>();
        treeModel.setTypeSelector(this);
        getEntityList();
    }

    public LookupTreeModel<E> getTreeModel() {
        if (treeModel == null) {
            initTreeModel();
        }
        return treeModel;
    }

    public void setTreeModel(LookupTreeModel<E> treeModel) {
        this.treeModel = treeModel;
    }

    public String getNodeCodePath() {
        return TreeUtils.getNodeCodePath(entity);
    }
    
    public String getNodeNamePath() {
        return TreeUtils.getNodeNamePath(entity);
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void selectNode() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        Long pk = Long.valueOf(params.get("nodeId"));
        entity = getRepository().findBy(pk);
        treeModel.setSelectedNodes(params.get("nodeId"));
    }

    @Override
    public String getNodeType(E node) {
        return "default";
    }

    protected void populateData() {
        if (entityList == null) {
            if (Strings.isNullOrEmpty(getTreeModel().getSearchText())) {
                if (isShowAllNodes()) {
                    entityList = getRepository().findNodes();
                } else {
                    entityList = getRepository().findRootNodes();
                }
            } else {
                //entityList = getRepository().lookupQuery(getTreeModel().getSearchText());
                entityList = getRepository().findNodes();
            }
            getTreeModel().setData(entityList);
        }

        getTreeModel().buildResultList();

    }

    public void search() {
        //TODO: Veriyi nezaman boşaltalım?
        //entityList = null;
        getTreeModel().setSearchText(filter);
        populateData();
    }

    public void setEntityList(List<E> entityList) {
        this.entityList = entityList;
    }

    /**
     * Save işleminden hemen önce yapılacak olan işler için çağrılır.
     *
     * @return
     */
    protected boolean onBeforeSave() {
        return true;
    }

    /**
     * Save işleminden hemen sonra yapılacak işler için çağrılır.
     *
     * @return
     */
    protected boolean onAfterSave() {
        return true;
    }

    /**
     * Tüm nodeları mı göstersin yoksa partial mı?
     *
     * @return
     */
    public Boolean isShowAllNodes() {
        return Boolean.TRUE;
    }

    /**
     * Alt sınıfların override etmesi için
     */
    protected void onBeforeDelete() {

    }

    /**
     * Alt sınıfların override etmesi için
     */
    protected void onAfterDelete() {

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

}
