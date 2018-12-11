package com.ozguryazilim.telve.entities;

import java.util.List;

/**
 * Tree Modelleri için temel arayüz
 * 
 * Popup ve Editorlerde yardımcı araçlar için...
 * 
 * @author Hakan Uygun
 * @param <E>
 *
 */
public interface TreeNodeModel< E extends TreeNodeModel > extends ViewModel{


	String getCode();
	/**
	 * Node Parent ID'si geri döner. Eger Node parent ise geriye 0 döner.
	 * @return
	 */
	Long getParentId();
	/**
	 * Node üzerinde gösterilecek bilgi geri döner.
	 * @return
	 */
	String getCaption();
	
	/**
	 * Varsa alt nodeları döner yoksa boş liste döner
	 * @return
	 */
	List<E> getChildren();
	
	/**
	 * Parent nodu döndürür. Eğer yoksa null döner
	 * @return
	 */
	E getParent();
	
	/**
	 * Parent node u setler
	 * @param parent
	 */
	void setParent(E parent);
	
	/**
	 * Node'un id pathi döner.
	 * @return
	 */
	String getPath();
	/**
	 * Node'un id pathini setler..
	 * @param path
	 */
	void setPath(String path);
}
