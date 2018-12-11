/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.bpm.handlers;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.bpm.TaskInfo;
import java.io.Serializable;

/**
 * Task UI'da task ile ilgili alınabilecek aksionlar için Model sınıf
 * @author Hakan Uygun
 */
public class TaskResultCommand implements Serializable{

    
    public static final TaskResultCommand ACCEPT = new TaskResultCommand("ACCEPT", "fa fa-check", "btn-success");
    public static final TaskResultCommand REJECT = new TaskResultCommand("REJECT", "fa fa-close", "btn-danger");
    public static final TaskResultCommand COMPLETE = new TaskResultCommand("COMPLETE", "fa fa-check", "btn-success");
    
    private String result;
    private String icon;
    private String style;
    private String widgetId;
    private Boolean needConfirmation = Boolean.FALSE;
    

    /**
     * UI tarafında normal bir command oluşturur. 
     * 
     * AbstractHumanHandler#close methodunu result değeri ile çağırır.
     * 
     * @param result
     * @param icon
     * @param style 
     */
    public TaskResultCommand(String result, String icon, String style) {
        this.result = result;
        this.icon = icon;
        this.style = style;
        this.widgetId = null;
    }
    
    /**
     * UI tarafında normal bir command oluşturur. 
     * 
     * AbstractHumanHandler#close methodunu result değeri ile çağırır.
     * 
     * @param result
     * @param icon
     * @param style 
     * @param needConfirm eğer komut confirmasyon istiyor ise true gönderilir.
     */
    public TaskResultCommand(String result, String icon, String style, Boolean needConfirm ) {
        this.result = result;
        this.icon = icon;
        this.style = style;
        this.widgetId = null;
        this.needConfirmation = needConfirm;
    }
    
    /**
     * UI tarafında verilen widgetID'li popup'ı çağıran bir düğme oluşturur..
     * @param result
     * @param icon
     * @param style
     * @param widgetId 
     */
    public TaskResultCommand(String result, String icon, String style, String widgetId) {
        this.result = result;
        this.icon = icon;
        this.style = style;
        this.widgetId = widgetId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
    
    /**
     * Varsayılan davranış task değişkenlerinden RESULT'a result değerini koymak.
     * 
     * Farklı işlemler yapılacak ise bu method override edilmeli.
     * 
     * @param task 
     */
    public void execute( TaskInfo task ){
        task.getVariables().put("RESULT", result);
    }
    
    public Boolean getIsPopup(){
        return !Strings.isNullOrEmpty(widgetId);
    }

    public String getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(String widgetId) {
        this.widgetId = widgetId;
    }

    /**
     * Geriye bu düğmenin confirmation isteyip istemediği bilgisini döner.
     * @return 
     */
    public Boolean getNeedConfirmation() {
        return needConfirmation;
    }

    public void setNeedConfirmation(Boolean needConfirmation) {
        this.needConfirmation = needConfirmation;
    }

}
