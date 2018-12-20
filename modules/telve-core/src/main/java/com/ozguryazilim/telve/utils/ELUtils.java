package com.ozguryazilim.telve.utils;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;

/**
 * EL yorumlama ilgili util fonksiyonları.
 *
 * @author Hakan Uygun
 */
public class ELUtils {

    /**
     * EL kullanarak verilen değeri ilgili yere yazar.
     *
     * @param expression
     * @param newValue
     */
    public static void setObject(String expression, Object newValue) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application app = facesContext.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        ValueExpression valueExp = elFactory.createValueExpression(elContext, expression, Object.class);

        //Check that the input newValue can be cast to the property type
        //expected by the managed bean.
        //Rely on Auto-Unboxing if the managed Bean expects a primitive
        Class bindClass = valueExp.getType(elContext);
        if (bindClass.isPrimitive() || bindClass.isInstance(newValue)) {
            valueExp.setValue(elContext, newValue);
        }
    }

}
