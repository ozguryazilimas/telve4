/*
 * Copyleft 2007-2010 Uygun Teknoloji
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 * http://www.gnu.org/licenses/lgpl.html
 * 
 * www.uygunteknoloji.com.tr
 */
package com.ozguryazilim.telve.admin.ui;

import com.ozguryazilim.telve.permisson.ActionConsts;
import com.ozguryazilim.telve.permisson.PermissionDefinition;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hakların GUI için uygun hale getirilmiş modeli
 *
 * @author Hakan Uygun
 */
public class PermissionUIModel implements Serializable {

    
    private static final long serialVersionUID = 1L;
    private PermissionDefinition definition;
    private Boolean select = Boolean.FALSE;
    private Boolean insert = Boolean.FALSE;
    private Boolean update = Boolean.FALSE;
    private Boolean delete = Boolean.FALSE;
    private Boolean export = Boolean.FALSE;
    private Boolean exec = Boolean.FALSE;
    private Boolean other = Boolean.FALSE;
    private Map<String, Boolean> otherActions = new HashMap<>();

    public PermissionDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(PermissionDefinition definition) {
        this.definition = definition;
        buildOtherActions();
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public Boolean getExec() {
        return exec;
    }

    public void setExec(Boolean exec) {
        this.exec = exec;
    }

    public Boolean getInsert() {
        return insert;
    }

    public void setInsert(Boolean insert) {
        this.insert = insert;
    }

    public Boolean getExport() {
        return export;
    }

    public void setExport(Boolean export) {
        this.export = export;
    }

    public Boolean getOther() {
        return other;
    }

    public void setOther(Boolean other) {
        this.other = other;
    }

    public Boolean getSelect() {
        return select;
    }

    public void setSelect(Boolean select) {
        this.select = select;
    }

    public Boolean getUpdate() {
        return update;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }

    public Boolean hasPermission(String action) {
        if (null != action) switch (action) {
            case ActionConsts.SELECT_ACTION:
                return select;
            case ActionConsts.INSERT_ACTION:
                return insert;
            case ActionConsts.UPDATE_ACTION:
                return update;
            case ActionConsts.DELETE_ACTION:
                return delete;
            case ActionConsts.EXPORT_ACTION:
                return export;
            case ActionConsts.EXEC_ACTION:
                return exec;
            default:
                if (otherActions.containsKey(action)) {
                    return otherActions.get(action);
                }
                return false;
        }
        return false;
    }

    public void grantPermission(String action) {
        if (null != action) switch (action) {
            case ActionConsts.SELECT_ACTION:
                setSelect(Boolean.TRUE); break;
            case ActionConsts.INSERT_ACTION:
                setInsert(Boolean.TRUE); break;
            case ActionConsts.UPDATE_ACTION:
                setUpdate(Boolean.TRUE); break;
            case ActionConsts.DELETE_ACTION:
                setDelete(Boolean.TRUE); break;
            case ActionConsts.EXPORT_ACTION:
                setExport(Boolean.TRUE); break;
            case ActionConsts.EXEC_ACTION:
                setExec(Boolean.TRUE); break;
            default:
                otherActions.put(action, Boolean.TRUE);
                
        }
    }
    
    /**
     * Standart hakların dışında kalanlar için bir map oluşturur.
     */
    private void buildOtherActions() {
        for (String s : definition.getActions()) {
            if (!ActionConsts.SELECT_ACTION.equals(s)
                    && !ActionConsts.INSERT_ACTION.equals(s)
                    && !ActionConsts.UPDATE_ACTION.equals(s)
                    && !ActionConsts.DELETE_ACTION.equals(s)
                    && !ActionConsts.EXPORT_ACTION.equals(s)
                    && !ActionConsts.EXEC_ACTION.equals(s)) {
                otherActions.put(s, Boolean.FALSE);
            }
        }
    }

    public Map<String, Boolean> getOtherActions() {
        return otherActions;
    }

    public void setOtherActions(Map<String, Boolean> otherActions) {
        this.otherActions = otherActions;
    }

    public void setOtherAction(String action, Boolean val) {
        otherActions.put(action, val);
    }

    public Boolean getOtherAction(String action) {
        return otherActions.containsKey(action) ? otherActions.get(action) : false;
    }

    public List<String> otherActionList() {
        return new ArrayList<>(otherActions.keySet());
    }
    
    /**
     * Hak değerlerinin hepsini false yapar.
     */
    public void clearValues(){
        setDelete(false);
        setExec(false);
        setInsert(false);
        setUpdate(false);
        setSelect(false);
        setExport(false);
        //TODO: Ohter Actionları da boşaltmak lazım
    }
}
