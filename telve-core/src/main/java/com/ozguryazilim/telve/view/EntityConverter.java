/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.view;

import java.lang.reflect.Field;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic Entity Converter.
 *
 * http://good-helper.blogspot.com.tr/2013/10/generic-entity-converter-in-jsf-2-and.html
 *
 * @author Hakan Uygun
 */
@FacesConverter("entityConverter")
public class EntityConverter implements Converter {

    private static final Logger LOG = LoggerFactory.getLogger(EntityConverter.class);

    @Inject
    private EntityManager em;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            String[] split = value.split(":");
            return em.find(Class.forName(split[0]), Long.valueOf(split[1]));
        } catch (NumberFormatException | ClassNotFoundException e) {
            LOG.warn("Convert error : {} {} ", value, e.getMessage());
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
            Class<? extends Object> clazz = value.getClass();
            Field f = findIdField(clazz);
            if (f != null) {
                f.setAccessible(true);
                Long id = (Long) f.get(value);
                return clazz.getCanonicalName() + ":" + id.toString();
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            LOG.warn("Convert error {} ", value.getClass().getCanonicalName());
        }
        return null;
    }

    /**
     * Verilen sınıf ya da üst sınıflarında @Id ile işaretlenmiş field'ı bulup
     * döndürür.
     *
     * @param clazz
     * @return bulamazsa null döner
     */
    protected Field findIdField(Class<? extends Object> clazz) {

        for (Field f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(Id.class)) {
                return f;
            }
        }

        //Buraya geldi ise sınıfın kendisinde yok. Üst sınıfları taramaya başlayalım...
        while (clazz.getSuperclass() != null) {
            clazz = clazz.getSuperclass();
            for (Field f : clazz.getDeclaredFields()) {
                if (f.isAnnotationPresent(Id.class)) {
                    return f;
                }
            }
        }

        return null;
    }

}
