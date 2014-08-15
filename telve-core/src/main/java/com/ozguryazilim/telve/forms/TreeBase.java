/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.forms;

import com.ozguryazilim.telve.utils.TreeUtils;
import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.entities.TreeNodeEntityBase;
import com.ozguryazilim.telve.messages.FacesMessages;
import com.ozguryazilim.telve.lookup.LookupTreeModel;
import com.ozguryazilim.telve.view.Pages;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.scope.GroupedConversation;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ağaç tii parametre veri girişleri taban kontrol sınıfı
 *
 * @author Hakan Uygun
 * @param <E> TreeNodeEntityBase'den türemiş bir entity sınıfı
 */
public abstract class TreeBase< E extends TreeNodeEntityBase> implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(TreeBase.class);

    private LookupTreeModel<E> treeModel;
    private E entity;
    private List<E> filteredList;
    private List<E> entityList;

    private String filter;

    @Inject
    private GroupedConversation conversation;

    /**
     * Geriye kullanılacak olan repository'i döndürür.
     *
     * @return
     */
    protected abstract RepositoryBase<E, ?> getRepository();

    public List<E> getEntityList() {
        LOG.debug("super.getEntityList");
        if (entityList == null) {
            entityList = getRepository().findAll();
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
        newE.setParent(entity);
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

        //boolean newRecord = !entity.isPersisted();
        //Path saklamak için ID'nin alınmaya ihtiyacı var o yüzden eğer persist değilse önce bir ID için kaydediyoruz.
        if (!entity.isPersisted()) {
            getRepository().save(entity);
        }
        entity.setPath(TreeUtils.getNodeIdPath(entity));
        getRepository().save(entity);

        if (!getEntityList().contains(entity)) {
            getEntityList().add(entity);
            getTreeModel().addTreeNode(entity);
        }

        LOG.debug("Entity Saved : {} ", entity);

        FacesMessages.info("#{messages['general.message.record.SaveSuccess']}");

        refreshEntityList();

        return null;
    }

    @Transactional
    public Class<? extends ViewConfig> delete() {
        if (entity == null) {
            return Pages.Home.class;
        }

        try {
            getRepository().remove(entity);
            getTreeModel().removeTreeNode(entity);
            //Listeden de çıkaralım
            getEntityList().remove(entity);

        } catch (Exception e) {
            LOG.error("Hata : {}", e);
            FacesMessages.error("#{messages['general.message.record.DeleteFaild']}");
            //facesMessages.add("#{messages['general.message.record.DeleteFaild']}");
            return null;
        }

        

        LOG.debug("Entity Removed : {} ", entity);

        //Mevcut silindi dolayısı ile null verdik
        entity = null;

        FacesMessages.info("#{messages['general.message.record.DeleteSuccess']}");

        refreshEntityList();

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
        treeModel.buildTreeModel(getEntityList());
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

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void selectItem() {
        if (getTreeModel().getSelectedData() != null) {
            entity = (E) getTreeModel().getSelectedData().getData();
        }
    }

}