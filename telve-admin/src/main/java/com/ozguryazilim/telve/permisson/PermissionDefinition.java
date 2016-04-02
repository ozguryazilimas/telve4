/*
 * Copyleft 2007-2010 Uygun Teknoloji
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 * http://www.gnu.org/licenses/lgpl.html
 * 
 * www.uygunteknoloji.com.tr
 */

package com.ozguryazilim.telve.permisson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Permission Tanımlarını tutar
 * @author Hakan Uygun
 */
public class PermissionDefinition implements Serializable{
    
    private static final long serialVersionUID = 1L;
    private String target;
    private List<String> actions = new ArrayList<>();
    private Boolean selectAction = null;
    private Boolean insertAction = null;
    private Boolean updateAction = null;
    private Boolean deleteAction = null;
    private Boolean exportAction = null;
    private Boolean execAction = null;
    private Boolean otherAction = null;


    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PermissionDefinition other = (PermissionDefinition) obj;
        if ((this.target == null) ? (other.target != null) : !this.target.equals(other.target)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + (this.target != null ? this.target.hashCode() : 0);
        return hash;
    }


    /**
     * Görme okuma hakkı var mı
     * @return
     */
    public Boolean hasSelectAction(){
        if( selectAction == null ){
            selectAction = Boolean.FALSE;
            for( String s : actions ){
                if( ActionConsts.SELECT_ACTION.equals(s)){
                    selectAction = Boolean.TRUE;
                    break;
                }
            }
        }
        return selectAction;
    }

    /**
     * Yeni kayıt hakkı var mı
     * @return
     */
    public Boolean hasInsertAction(){
        if( insertAction == null ){
            insertAction = Boolean.FALSE;
            for( String s : actions ){
                if( ActionConsts.INSERT_ACTION.equals(s)){
                    insertAction = Boolean.TRUE;
                    break;
                }
            }
        }
        return insertAction;
    }

    /**
     * Güncelleme hakkı var mı
     * @return
     */
    public Boolean hasUpdateAction(){
        if( updateAction == null ){
            updateAction = Boolean.FALSE;
            for( String s : actions ){
                if( ActionConsts.UPDATE_ACTION.equals(s)){
                    updateAction = Boolean.TRUE;
                    break;
                }
            }
        }
        return updateAction;
    }

    /**
     * Silme hakkı var mı
     * @return
     */
    public Boolean hasDeleteAction(){
        if( deleteAction == null ){
            deleteAction = Boolean.FALSE;
            for( String s : actions ){
                if( ActionConsts.DELETE_ACTION.equals(s)){
                    deleteAction = Boolean.TRUE;
                    break;
                }
            }
        }
        return deleteAction;
    }

    /**
     * Export hakkı var mı
     * @return
     */
    public Boolean hasExportAction(){
        if( exportAction == null ){
            exportAction = Boolean.FALSE;
            for( String s : actions ){
                if( ActionConsts.EXPORT_ACTION.equals(s)){
                    exportAction = Boolean.TRUE;
                    break;
                }
            }
        }
        return exportAction;
    }
    
    /**
     * Çalıştırma hakkı var mı
     * @return
     */
    public Boolean hasExecAction(){
        if( execAction == null ){
            execAction = Boolean.FALSE;
            for( String s : actions ){
                if( ActionConsts.EXEC_ACTION.equals(s)){
                    execAction = Boolean.TRUE;
                    break;
                }
            }
        }
        return execAction;
    }

    /**
     * Standart haklar dışında hak içeriyorsa true döner
     * @return
     */
    public Boolean hasOtherAction(){
        if( otherAction == null ){
            otherAction = Boolean.FALSE;
            for( String s : actions ){
                if( !ActionConsts.EXEC_ACTION.equals(s) &&
                    !ActionConsts.SELECT_ACTION.equals(s) &&
                    !ActionConsts.INSERT_ACTION.equals(s) &&
                    !ActionConsts.UPDATE_ACTION.equals(s) &&
                    !ActionConsts.EXPORT_ACTION.equals(s) &&
                    !ActionConsts.DELETE_ACTION.equals(s)){
                    otherAction = Boolean.TRUE;
                    break;
                }
            }
        }
        return otherAction;
    }
}
