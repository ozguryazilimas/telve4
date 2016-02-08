/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.lookup;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.ozguryazilim.telve.entities.TreeNodeModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import org.primefaces.model.TreeNode;

/**
 * TreeModel'i oluşturan taban sınıf.
 *
 * com.ut.tekir.entities.TreeNode türündeki model sınıfları kullanarak UI için gerekli listeyi üretir.
 * 
 *
 *
 * @author Hakan Uygun
 * @param <T> TreeNodeModel'den türetilmiş bir sınıf olmalı
 */
public class LookupTreeModel<T extends TreeNodeModel> implements LookupModel<T, T>, Serializable {

    private Boolean multiSelect = false;
    private Boolean leafSelect = false;
    private String profile;
    private String listener;
    private String searchText;
    private Boolean fullPathResult = false;
    private TreeNodeTypeSelector typeSelector;
    private Map<String, String> profileProperties;

    //Seçilen nodeların id'leri virgüllerle ayrılmış olarak gelir.
    private String selectedNodes;
    
    private List<T> allItems = new ArrayList<>();
    private List<T> resultItems = new ArrayList<>();
    private Map<Long, T> resultIdMap = new HashMap<>();
    private Map<Long, T> idMap = new HashMap<>();
    private Map<String, Long> pathMap = new HashMap<>();
    

    public void clearModel() {
        allItems.clear();
        resultItems.clear();
        idMap.clear();
        resultIdMap.clear();
        pathMap.clear();
    }

    /**
     * Geriye Node türünü döndürür.
     *
     * Bu method override edilerek farklı node türleri eklenebilir.
     *
     * @param node
     * @return
     */
    public String getNodeType(T node) {
        if (typeSelector != null) {
            return typeSelector.getNodeType(node);
        }
        return "default";
    }

    
    @Override
    public Boolean getMultiSelect() {
        return multiSelect;
    }

    @Override
    public void setMultiSelect(Boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    @Override
    public Boolean getLeafSelect() {
        return leafSelect;
    }

    @Override
    public void setLeafSelect(Boolean leafSelect) {
        this.leafSelect = leafSelect;
    }

    @Override
    public String getProfile() {
        return profile;
    }

    @Override
    public void setProfile(String profile) {
        this.profile = profile;
    }

    @Override
    public String getSearchText() {
        return searchText;
    }

    @Override
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public String getListener() {
        return listener;
    }

    @Override
    public void setListener(String listener) {
        this.listener = listener;
    }

    @Override
    public T getSelectedViewModel() {
        return getSelectedItems().isEmpty() ? null : getSelectedItems().get(0);
    }

    @Override
    public void setSelectedViewModel(T data) {
        //TODO: Buraya bir şey yapmak lazım mı ki?
    }

    @Override
    public List<T> getSelectedViewModels() {
        return getSelectedItems();
    }

    @Override
    public void setSelectedViewModels(List<T> data) {
        //TODO: Yapılacka bişi var mı?
    }

    @Override
    public void setData(List<T> dataList) {
        allItems.clear();
        allItems.addAll(dataList);
        
        idMap.clear();
        resultIdMap.clear();
        pathMap.clear();
        for( T e : allItems ){
            idMap.put(e.getId(), e);
            pathMap.put(e.getPath(), e.getId());
        }
    }

    @Override
    public boolean isDataEmpty() {
        return allItems.isEmpty();
    }

    @Override
    public void clearData() {
        clearModel();
    }

    @Override
    public String getModelType() {
        return "Tree";
    }

    @Override
    public Object dataModel() {
        return resultItems;
    }

    public TreeNodeTypeSelector getTypeSelector() {
        return typeSelector;
    }

    public void setTypeSelector(TreeNodeTypeSelector typeSelector) {
        this.typeSelector = typeSelector;
    }

    /**
     * LeafSelect olup olmamasına göre seçim stratejisini belirler.
     *
     * @param node
     */
    private void setSelectionStrategy(TreeNode node) {

        if (leafSelect) {
            node.setSelectable(node.isLeaf());
        } else {
            node.setSelectable(true);
        }

        for (TreeNode n : node.getChildren()) {
            setSelectionStrategy(n);
        }

    }

    @Override
    public Map<String, String> getProfileProperties() {
        return profileProperties;
    }

    @Override
    public void setProfileProperties(Map<String, String> profileProperties) {
        this.profileProperties = profileProperties;
    }

    
    protected List<T> getSelectedItems(){
        List<T> ls = new ArrayList<>();
        
        if( !Strings.isNullOrEmpty(selectedNodes)){
            List<String> ids = Splitter.on(',').omitEmptyStrings().trimResults().splitToList(selectedNodes);
            for( String s : ids ){
                ls.add(idMap.get( Long.valueOf(s)));
            }
        }
        
        return ls;
    }

    @Override
    public T getSelectedData() {
        return getSelectedItems().isEmpty() ? null : getSelectedItems().get(0);
    }

    @Override
    public void setSelectedData(T data) {
        //
    }

    @Override
    public T[] getSelectedDatas() {
        return (T[]) getSelectedItems().toArray();
    }

    @Override
    public void setSelectedDatas(T[] data) {
        //
    }

    
    public void addItem( T item ){
        allItems.add(item);
        idMap.put(item.getId(), item);
        resultIdMap.put( item.getId(), item);
        pathMap.put(item.getPath(), item.getId());
        resultItems.add(item);
    }
    
    public void removeItem( T item ){
        allItems.remove(item);
        idMap.remove(item.getId());
        resultIdMap.remove(item.getId());
        pathMap.remove(item.getPath());
        resultItems.remove(item);
    }
    
    public void buildResultList(){
        resultItems.clear();
        resultIdMap.clear();
        
        if (Strings.isNullOrEmpty(getSearchText())) {
            resultItems.addAll(allItems);
            resultIdMap.putAll(idMap);
        } else {
            
            for( T ent : allItems ){
                //Case Insensitive arama TODO: Locale kısmını config'e almak lazım... ( Türkçe Locale ile )
                if( ent.getCaption().toLowerCase(new Locale("tr")).contains(getSearchText().toLowerCase(new Locale("tr"))) ){
                    addFilteredItem(ent);
                    //Eger sadeye en son node seçilebiliyorsa bulunanların detaylarını da ekleyelim...
                    if( getLeafSelect() ){
                        //burda iki kez eklenme olabilir. contains ile konrol ederek ekleniyor
                        for( T it : findChildNodes(ent) ){
                            if( !resultItems.contains(it) ){
                                resultItems.add( it );
                                resultIdMap.put(it.getId(), it);
                            }
                        }
                    }
                }
            }
        }
    }
    
    protected void addFilteredItem( T item ){
        //Hali hazırda eklenmemişse ekleyelim...
        if( !resultItems.contains(item) ){
            resultItems.add(item);
            resultIdMap.put(item.getId(), item);
            if( fullPathResult ){
                addFilteredItemPath(item.getParentId());
            }
        }
    }
    
    protected void addFilteredItemPath( Long pk ){
        if( pk == null || pk == 0 ) return;
        
        T ent = idMap.get(pk);
        if( ent != null ){
            if( !resultItems.contains(ent) ){
                resultItems.add( ent );
                resultIdMap.put(ent.getId(), ent);
            }
            addFilteredItemPath(ent.getParentId());
        }
    }

    public List<T> getResultItems() {
        return resultItems;
    }

    public void setResultItems(List<T> resultItems) {
        this.resultItems = resultItems;
    }

    public String getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(String selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    /**
     * Verilen node'un parent nodunu döndürür. Yoksa null döner.
     * @param node
     * @return 
     */
    public T findParent( T node ){
        return node == null ? null : resultIdMap.get(node.getParentId());
    }

    /**
     * Verilen node'un childlarını döndürür.
     * @param node
     * @return 
     */
    public List<T> findChildren( T node ){
        //TODO: Aslında burayı eldeki listeden sağlasak süper olur.
        if( node == null ){
            return Collections.EMPTY_LIST;
        }
        return node.getChildren();
    }
    
    /**
     * Verilen node'un tüm parent node listesini döndürür.
     * @param node
     * @return 
     */
    public List<T> findParentNodes( T node ){
        List<T> ls = new ArrayList<>();
        
        String path = node.getPath();
        
        for( Map.Entry<String,Long> ent : pathMap.entrySet() ){
            //Eğer node'un path'i key ile başlıyorsa ve kendisi değilse
            if( path.startsWith( ent.getKey() ) && !Objects.equals(ent.getValue(), node.getId()) ){
                ls.add(idMap.get(ent.getValue()));
            }
        }
        
        return ls;
    }
    
    /**
     * Verilen node'un tüm child nodelarını döndürür.
     * @param node
     * @return 
     */
    public List<T> findChildNodes( T node ){
        List<T> ls = new ArrayList<>();
        
        String path = node.getPath();
        
        for( Map.Entry<String,Long> ent : pathMap.entrySet() ){
            //Eğer key'un path'i node ile başlıyorsa ve kendisi değilse
            if( ent.getKey().startsWith( path ) && !Objects.equals(ent.getValue(), node.getId())){
                ls.add(idMap.get(ent.getValue()));
            }
        }
        
        return ls;
    }
    
    /**
     * Verilen node'un dalında ki tüm node'ları döndürür.
     * @param node
     * @return 
     */
    public List<T> findBranchNodes( T node ){
        
        List<T> ls = new ArrayList<>();
        
        String path = node.getPath();
        
        for( Map.Entry<String,Long> ent : pathMap.entrySet() ){
            //Eğer path ile key'i eşleşiyorsa aynı daldır.
            if( ent.getKey().startsWith( path ) || path.startsWith( ent.getKey())){
                ls.add(idMap.get(ent.getValue()));
            }
        }
        
        return ls;
    }

    @Override
    public void clearSelectedAllDatas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clearSelections() {
        this.selectedNodes = "";
    }

    /**
     * Arama sonuçlarının arama kriterine uygumasa da parent nodelerının da sonuca eklenip eklenmeyeceği bilgisi
     * @return 
     */
    public Boolean getFullPathResult() {
        return fullPathResult;
    }

    /**
     * Arama sonuçlarının arama kriterine uygumasa da parent nodelerının da sonuca eklenip eklenmeyeceği bilgisi
     * @param fullPathResult 
     */
    public void setFullPathResult(Boolean fullPathResult) {
        this.fullPathResult = fullPathResult;
    }
    
    
}
