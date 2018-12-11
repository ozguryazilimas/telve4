/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.suggestion;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.entities.SuggestionItem;
import com.ozguryazilim.telve.lookup.LookupSelectTuple;
import com.ozguryazilim.telve.utils.ELUtils;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 * Sistem bazında sunulan hatırlatma stringleri için lookup sınıfı.
 *
 * Standart lookup base'den üretilmediler. Hem standart lookup bileşenlerinde
 * kullanılmıyor hem de ihtiyaç yok.
 *
 * @author Hakan Uygun
 */
@Named
@SessionScoped
public class SuggestionControl implements Serializable {

    @Inject
    private SuggestionRepository repository;

    private String listener;

    private SuggestionItem suggestionItem;

    private String data;
    private String info;
    private String group;
    private String key;

    /**
     * Geriye öneri listesi döndürür.
     *
     * key alanı boşsa ya da nullsa sadece gruba göre arama yapar.
     *
     * @param group
     * @param key
     * @return
     */
    public List<SuggestionItem> suggestions(String group, String key) {
        if (Strings.isNullOrEmpty(key)) {
            return repository.findByGroup(group);
        } else {
            return repository.findByGroupAndKey(group, key);
        }
    }

    /**
     * Yeni veri giriş dialoğunu açar.
     *
     * @param group
     * @param key
     * @param listener
     */
    public void openDialog(String group, String key, String listener) {

        this.group = group;
        this.key = key;
        this.listener = listener;
        this.data = "";
        this.info = "";

        RequestContext.getCurrentInstance().openDialog("/admin/suggestionPopup");
    }

    public void closeDialog() {
        //TODO burayı biraz daha adam etmek lazım.

        LookupSelectTuple sl = null;

        suggestionItem = new SuggestionItem();
        suggestionItem.setActive(Boolean.TRUE);
        suggestionItem.setGroup(group);
        suggestionItem.setKey(key);
        suggestionItem.setData(data);
        suggestionItem.setInfo(info);

        repository.save(suggestionItem);

        sl = getLookupSelectTuple();

        RequestContext.getCurrentInstance().closeDialog(sl);

    }

    /**
     * Dialogu hiç bir şey seçmeden kapatır.
     */
    public void cancelDialog() {
        RequestContext.getCurrentInstance().closeDialog(null);
    }

    public void onSelect(SelectEvent event) {
        LookupSelectTuple sl = (LookupSelectTuple) event.getObject();
        if (sl == null) {
            return;
        }
        
        if( sl.getExpression().isEmpty() ) return;
        
        //EL üzerinden değeri yazacağız
        ELUtils.setObject(sl.getExpression(), ((SuggestionItem)sl.getValue()).getData());
    }

    public String getListener() {
        return listener;
    }

    public SuggestionItem getSuggestionItem() {
        return suggestionItem;
    }

    public String getData() {
        return data;
    }

    public String getInfo() {
        return info;
    }

    public String getGroup() {
        return group;
    }

    public String getKey() {
        return key;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setKey(String key) {
        this.key = key;
    }

    
    
    /**
     * GUI'den seçilen bilgileri kullanarak seçim paketi oluşturur.
     *
     * @return
     */
    protected LookupSelectTuple getLookupSelectTuple() {
        LookupSelectTuple sl;

        String expression = "";
        if (!Strings.isNullOrEmpty(this.getListener())) {
            expression = "#{" + this.getListener() + "}";
        }

        sl = new LookupSelectTuple(expression, this.getSuggestionItem());

        return sl;
    }

}
