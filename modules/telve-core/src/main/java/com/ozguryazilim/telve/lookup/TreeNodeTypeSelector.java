package com.ozguryazilim.telve.lookup;

/**
 * TreeNode'larının hangi node type'da olacağını belirleyen api.
 * 
 * @author Hakan Uygun
 * @param <T> 
 */
public interface TreeNodeTypeSelector<T> {
    /**
     * Gelen Node'a göre geriye NodeType stringi döndürülür.
     * @param node
     * @return 
     */
    String getNodeType(T node);
}
