package com.ozguryazilim.telve.utils;

import com.ozguryazilim.telve.entities.TreeNodeModel;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Ağaç işleri için bazı yardımcı fonksiyonlar...
 * 
 * @author Hakan Uygun
 *
 */
public class TreeUtils {

	/**
	 * Public constructor gizleniyor
	 */
	protected TreeUtils(){}
	
	/**
	 * Verilen nodun idlerini path olarak döndürür.
	 * 
	 * @param node
	 * @return
	 */
	public static String getNodeIdPath( TreeNodeModel node ){

		StringBuilder sb = new StringBuilder();
		TreeNodeModel parent = node.getParent();
		
		while ( parent != null ){
			sb.insert(0, "/" + parent.getId());
			parent = parent.getParent();
		}
		
		sb.append("/").append(node.getId());
		
		return sb.toString();
	}
	
	/**
	 * Guide gösterebilmek için verilen nodun ve parentlarının kodlarını path olarak dondurur
	 *
	 * @param node
	 * @return
	 */
	public static String getNodeCodePath( TreeNodeModel node ){

		if(node==null) return "";
		StringBuilder sb = new StringBuilder();
		TreeNodeModel parent = node.getParent();
		
		while ( parent != null ){
			sb.insert(0, " » " + parent.getCode());
			parent = parent.getParent();
		}
		
		sb.append(" » ").append(node.getCode() == null ? "" : node.getCode());
		
		return sb.toString();
	}
        
        /**
	 * Guide gösterebilmek için verilen nodun ve parentlarının kodlarını path olarak dondurur
	 *
	 * @param node
	 * @return
	 */
	public static String getNodeNamePath( TreeNodeModel node ){

		if(node==null) return "";
		StringBuilder sb = new StringBuilder();
		TreeNodeModel parent = node.getParent();
		
		while ( parent != null ){
			sb.insert(0, " » " + parent.getCaption());
			parent = parent.getParent();
		}
		
		sb.append(" » ").append(node.getCaption()== null ? "" : node.getCaption());
		
		return sb.toString();
	}
	
	/**
	 * Verilen Node'un parent listesini sırasıyla döner
	 * @param node
	 * @return
	 */
	public static List<TreeNodeModel> getParentList( TreeNodeModel node ){
    	List<TreeNodeModel> ls = new ArrayList<>();
    	
    	TreeNodeModel parent = node.getParent();
    	
    	while( parent != null ){
    		ls.add(0, parent);
    		parent = parent.getParent();
    	}
    	
    	return ls;
    }
	
}
