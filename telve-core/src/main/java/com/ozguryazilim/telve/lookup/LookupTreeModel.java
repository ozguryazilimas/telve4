/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.lookup;

import com.ozguryazilim.telve.entities.TreeNodeModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.TreeNode;

/**
 * PrimeFaces TreeModel'i oluşturan taban sınıf.
 * 
 * com.ut.tekir.entities.TreeNode türündeki model sınıfları kullanarak UI için primeface TreeNode modeli üretir.
 * 
 *
 * @author Hakan Uygun
 * @param <T> TreeNodeModel'den türetilmiş bir sınıf olmalı
 */
public class LookupTreeModel<T extends TreeNodeModel> implements LookupModel<T, TreeNode>, Serializable {

    private TreeNode model;
    private TreeNode selectedData;
    private TreeNode[] selectedDatas;
    private Boolean multiSelect = false;
    private Boolean leafSelect = false;
    private String profile;
    private String listener;
    private String searchText;
    private TreeNodeTypeSelector typeSelector;
    private Map<String,String> profileProperties;

    private Map<Long,TreeNode> nodeCache = new HashMap<>();
    
    public TreeNode getRootNode() {
        return model;
    }

    public void clearModel() {
        model = null;
        model = new CheckboxTreeNode("Root", null);
    }

    /**
     * Model nesnesine yeni veri ekler.
     *
     * @param data
     */
    public void buildTreeModel(List<T> data) {
        model = new CheckboxTreeNode("Root", null);
        nodeCache = new HashMap<>();
        for (T n : data) {
            addTreeNode(n);
        }
        setSelectionStrategy( model );
        //Cache temizlensin...
        nodeCache = null;
    }

    /**
     * Verilen veriyi UI modelinde ilgili yere ekler.
     * @param data 
     */
    public void addTreeNode(T data) {
        TreeNode parent = findParent(model, data);
        if (parent == null) {
            parent = model;
        }
        TreeNode node = new CheckboxTreeNode(getNodeType(data), data, parent);
        nodeCache.put(data.getId(), node);
    }

    /**
     * Verilen veriyi UI modelinde ilgili yere ekler.
     * @param parent
     * @param data 
     */
    public void addTreeNodes(TreeNode parent, List<T> data) {
        if (parent == null) {
            parent = model;
        }
        //Verilen listeyi ekler
        for (T n : data) {
            TreeNode node = new CheckboxTreeNode(getNodeType(n), n, parent);
        }
    }
    
    //

    /**
     * Verilen veriyi UI modelinden çıkartır.
     * @param data 
     */
    public void removeTreeNode( T data ){
        TreeNode node = findNode(getRootNode(), data);
        if (node != null) {
            node.getParent().getChildren().remove(node);
        }
    }
    
    /**
     * Geriye Node türünü döndürür. 
     * 
     * Bu method override edilerek farklı node türleri eklenebilir.
     * 
     * @param node
     * @return 
     */
    protected String getNodeType(T node) {
        if( typeSelector != null ){
            return typeSelector.getNodeType(node);
        }
        return CheckboxTreeNode.DEFAULT_TYPE;
    }

    /**
     * Veri için GUI Modelinde parent node bulur.
     * @param parent
     * @param n
     * @return 
     */
    protected TreeNode findParent(TreeNode parent, TreeNodeModel n) {

        return nodeCache.get(n.getParentId());
        /*
        for (TreeNode node : parent.getChildren()) {

            if (((TreeNodeModel) node.getData()).getId().equals(n.getParentId())) {
                return node;
            }

            TreeNode nn = findParent(node, n);
            if (nn != null) {
                return nn;
            }
        }

        return null;
        */
    }
    
    /**
     * Veri için ilgili GUI nodunu bulur.
     * @param parent
     * @param n
     * @return 
     */
    protected TreeNode findNode(TreeNode parent, TreeNodeModel n) {

        for (TreeNode node : parent.getChildren()) {

            if (((TreeNodeModel) node.getData()).getId().equals(n.getId())) {
                return node;
            }

            TreeNode nn = findNode(node, n);
            if (nn != null) {
                return nn;
            }
        }

        return null;
    }

    @Override
    public TreeNode getSelectedData() {
        return selectedData;
    }

    @Override
    public void setSelectedData(TreeNode selectedData) {
        this.selectedData = selectedData;
    }

    @Override
    public TreeNode[] getSelectedDatas() {
        return selectedDatas;
    }

    @Override
    public void setSelectedDatas(TreeNode[] selectedDatas) {
        this.selectedDatas = selectedDatas;
    }

    @Override
    public Boolean getMultiSelect() {
        return multiSelect;
    }

    @Override
    public void setMultiSelect(Boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    public Boolean getLeafSelect() {
        return leafSelect;
    }

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
        return  listener;
    }

    @Override
    public void setListener(String listener) {
        this.listener = listener;
    }

    @Override
    public T getSelectedViewModel() {
        return (T)getSelectedData().getData();
    }

    @Override
    public void setSelectedViewModel(T data) {
        //TODO: Buraya bir şey yapmak lazım mı ki?
    }

    @Override
    public List<T> getSelectedViewModels() {
        List<T> ls = new ArrayList<>();
        
        for( TreeNode node : getSelectedDatas()){
            ls.add( (T)node.getData());
        }
        
        return ls;
    }

    @Override
    public void setSelectedViewModels(List<T> data) {
        //TODO: Yapılacka bişi var mı?
    }

    @Override
    public void setData(List<T> dataList) {
        buildTreeModel(dataList);
    }

    @Override
    public boolean isDataEmpty() {
        return model == null || model.getChildCount() == 0;
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
        return getRootNode();
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
    private void setSelectionStrategy( TreeNode node ) {
        
        if( leafSelect ){
            node.setSelectable( node.isLeaf() );
        } else {
            node.setSelectable(true);
        }
        
        for( TreeNode n : node.getChildren() ){
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

    
    
}
