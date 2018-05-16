/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.forms;

import com.ozguryazilim.telve.annotations.BizKey;
import com.ozguryazilim.telve.audit.AuditLogCommand;
import com.ozguryazilim.telve.audit.AuditLogger;
import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.data.ParamRepositoryBase;
import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.entities.EntityBase;
import com.ozguryazilim.telve.entities.ParamEntityBase;
import com.ozguryazilim.telve.messages.FacesMessages;
import com.ozguryazilim.telve.messages.Messages;
import com.ozguryazilim.telve.view.Pages;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import javax.inject.Inject;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.scope.GroupedConversation;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parametre tipi ekranlar için taban sınıf.
 *
 * @author Hakan Uygun
 * @param <E> İşlenecek olan Entity sınıfı
 * @param <PK> PK kolon sınıfı
 */
public abstract class ParamBase<E extends EntityBase, PK extends Serializable> implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(ParamBase.class);

    private E entity;
    private List<E> filteredList;
    private List<E> entityList;

    @Inject
    private GroupedConversation conversation;
    
    @Inject
    private Identity identity;
    
    @Inject
    private AuditLogger auditLogger;

    public List<E> getEntityList() {
        LOG.debug("super.getEntityList");
        if (entityList == null) {
            if( getRepository() instanceof ParamRepositoryBase  ){
                entityList = ((ParamRepositoryBase)getRepository()).findAllSorted();
            } else {
                entityList = getRepository().findAll();
            }
        }
        return entityList;
    }

    /**
     * Geriye kullanılacak olan repository'i döndürür.
     *
     * @return
     */
    protected abstract RepositoryBase<E, ?> getRepository();

    /**
     * TODO: Aslında bu methodu Repository'e taşımak lazım.
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

    public void createNew() {
        entity = getNewEntity();
    }

    public Class<? extends ViewConfig> saveAndNew() {
        Class<? extends ViewConfig> result = save();
        createNew();
        return result;
    }

    public void edit(E e) {

        LOG.debug("Edit edicedik : {}", e);

        onBeforeLoad();

        entity = e;
        onAfterLoad();
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

        try {
            if (!onBeforeSave()) {
                return null;
            }

            String act =
                entity.isPersisted() ? AuditLogCommand.ACT_UPDATE : AuditLogCommand.ACT_INSERT;

            entity = getRepository().merge(entity);

            auditLog(act);

            //Eğer elimizdeki listede yoksa ekleyelim
            if (!getEntityList().contains(entity)) {
                getEntityList().add(entity);
            } else {
                //Varsa replace edelim. Çünkü veri tabanından yeni nesne geldi.
                int ix = getEntityList().indexOf(entity);
                getEntityList().set(ix, entity);
            }

            onAfterSave();
        } catch (Exception e) {
            //FIXME: Asıl detay hatanın bulunması lazım.
            if (e.getCause() instanceof ConstraintViolationException) {
                LOG.error("Error : Constraint Violation ", e);
                ConstraintViolationException cause = (ConstraintViolationException) e.getCause();

                String fieldName = Messages
                    .getMessage("constraintName." + cause.getConstraintName());
                String errorMsg = Messages.getMessageFromData(Messages.getCurrentLocale(),
                    "general.message.exception.ConstraintViolationException$%&" + fieldName);
                FacesMessages
                    .error(errorMsg,
                        "general.message.exception.ConstraintViolationExceptionDetail");

                throw new RuntimeException();
            } else {
                LOG.error("Error : other error ", e);
                FacesMessages
                    .error("general.message.exception.OtherException",
                        "general.message.exception.OtherExceptionDetail");
                throw new RuntimeException();

            }
        }

        LOG.debug("Entity Saved : {} ", entity);

        FacesMessages.info("general.message.record.SaveSuccess");

        refreshEntityList();

        return null;
    }

    @Transactional
    public Class<? extends ViewConfig> delete() {
        if (entity == null) {
            return Pages.Home.class;
        }

        if (!onBeforeDelete()) {
            return null;
        }

        try {
            
            auditLog(AuditLogCommand.ACT_DELETE);
            
            //getRepository().deleteById(entity.getId());
            getRepository().remove(entity);
            
        } catch (Exception e) {
            LOG.error("Hata : {}", e);
            FacesMessages.error("general.message.record.DeleteFaild");
            return null;
        }

        //Listeden de çıkaralım
        getEntityList().remove(entity);

        onAfterDelete();

        LOG.debug("Entity Removed : {} ", entity);

        //Mevcut silindi dolayısı ile null verdik
        entity = null;

        FacesMessages.info("general.message.record.DeleteSuccess");

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
            createNew();
        }
        return entity;
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }

    public List<E> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<E> filteredList) {
        this.filteredList = filteredList;
    }

    public boolean onBeforeSave() {
        return true;
    }

    public boolean onAfterSave() {
        return true;
    }

    public boolean onBeforeLoad() {
        return true;
    }

    public boolean onAfterLoad() {
        return true;
    }

    public boolean onBeforeDelete() {
        return true;
    }

    public boolean onAfterDelete() {
        return true;
    }
    

    /**
     * Inject edilmiş auditLogger instance.
     * 
     * Alt sınıflarda kullanılabilmesi için
     * 
     * @return 
     */
    protected AuditLogger getAuditLogger() {
        return auditLogger;
    }
    
    /**
     * Kayıt işlemlerin eilişkin auditLog gönderir.
     * @param action 
     */
    protected void auditLog( String action ){
        auditLogger.actionLog(entity.getClass().getSimpleName(), entity.getId(), getBizKeyValue(), getAuditLogCategory(), action, identity.getLoginName(), "" );
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
        
        Field[] fields = FieldUtils.getFieldsWithAnnotation(entity.getClass(), BizKey.class);
        
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
     * Geriye AuditLog kategorisini döndürür.
     * 
     * Default : AuditLogCommand.CAT_PARAM;
     * @return 
     */
    protected String getAuditLogCategory(){
        return AuditLogCommand.CAT_PARAM;
    }
}
