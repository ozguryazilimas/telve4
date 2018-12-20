package com.ozguryazilim.telve.forms;

/**
 * FormEdit ve ParamEdit sayfalarında bir entity kaydedildiğinde ya da silindiğinde fırlatılır.
 *
 * Doğrudan veri tabanı ile değil, ViewControl sınıfları ile ilişkili bir eventtir. Dolatıyısı ile her türlü veri değişiminde fırlatılmaz.
 * 
 * @author Hakan Uygun
 */
public class EntityChangeEvent {

    private Object entity;
    private EntityChangeAction action;

    public EntityChangeEvent(Object entity, EntityChangeAction action) {
        this.entity = entity;
        this.action = action;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public EntityChangeAction getAction() {
        return action;
    }

    public void setAction(EntityChangeAction action) {
        this.action = action;
    }

    
    
}
