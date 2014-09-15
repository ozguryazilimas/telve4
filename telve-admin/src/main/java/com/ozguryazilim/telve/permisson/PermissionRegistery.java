/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.permisson;

import com.ozguryazilim.telve.api.module.TelveModuleRegistery;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sistemde tanımlı olan hakları saklar.
 * 
 * @author Hakan Uygun
 */
@Named
@ApplicationScoped
public class PermissionRegistery implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(PermissionRegistery.class);
    
    private final Map<String, PermissionGroup> permMap = new HashMap<>();
    
    @PostConstruct
    public void init() {
        LOG.info("DeploymentHandler Resources {}", TelveModuleRegistery.getPermissonFileNames());
        
        for( String fd : TelveModuleRegistery.getPermissonFileNames() ){
            handlePermXml(fd);
        }
    }
    
    public void handlePermXml(String fd) {

        InputStream is = null;
        try {
            LOG.info("Register file : {}", fd );
            is = this.getClass().getResourceAsStream("/"+fd);

            SAXReader reader = new SAXReader();
            Document doc = reader.read(is);
            Element root = doc.getRootElement();
            List<Element> elements = root.elements("permissionGroup");
            for (Element e : elements) {
                registerPermissionGroup(e);
            }

        } catch (DocumentException ex) {
            LOG.error("perm cannot read", ex);
        } finally {
            try {
                if( is != null) { 
                    is.close();
                }
            } catch (IOException ex) {
                LOG.error("perm cannot read", ex);
            }
        }

    }
    
    /**
     * Verilen elementteki grupları ve altındaki permissionları toplar
     * @param e
     */
    private void registerPermissionGroup(Element e) {
        String gn = e.attributeValue("name");
        List<Element> elements = e.elements("permission");
        for (Element a : elements) {
            registerPermission(a, gn);
        }
    }

    /**
     * Verilen elementten ( permission ) actionları toplar ve register eder.
     * @param e
     * @param gn
     */
    private void registerPermission(Element e, String gn) {
        Attribute target = e.attribute("target");
        String t = target.getText();
        List<String> al = new ArrayList<>();

        populateEntityActions(e, al);
        populateActions(e, al);
        populateExculedActions(e, al);

        //Tüm data toplandı şimdi map'e ekleniyor...
        PermissionGroup pg = permMap.get(gn);
        if( pg == null ){
            pg = new PermissionGroup();
            pg.setName(gn);
            permMap.put(gn, pg);
        }

        PermissionDefinition pd = new PermissionDefinition();
        pd.setTarget(t);
        pd.getActions().addAll(al);
        pg.getDefinitions().add(pd);

        //LOG.debug("Registered Permission = {} {} {}", gn, t, al);
    }

    /**
     * Verilen elementteki ( permission ) entityAction elementi varmı bakar varsa hakları listeye ekler
     * @param e
     * @param als
     */
    private void populateEntityActions(Element e, List<String> als) {
        Element element = e.element("entityActions");
        if (element != null) {
            als.add(ActionConsts.SELECT_ACTION);
            als.add(ActionConsts.INSERT_ACTION);
            als.add(ActionConsts.UPDATE_ACTION);
            als.add(ActionConsts.DELETE_ACTION);
            als.add(ActionConsts.EXPORT_ACTION);
        }

    }

    /**
     * verilen elementteki ( permission ) action elementelerini listeye ekler
     * @param e
     * @param als
     */
    private void populateActions(Element e, List<String> als) {
        List<Element> elements = e.elements("action");
        for (Element a : elements) {
            als.add(a.attributeValue("name"));
        }
    }

    /**
     * verilen elementteki ( permission ) exclude elemetlerini listeden çıkartır
     * @param e
     * @param als
     */
    private void populateExculedActions(Element e, List<String> als) {
        List<Element> elements = e.elements("exclude");
        for (Element a : elements) {
            als.remove(a.attributeValue("name"));
        }
    }

    public Map<String, PermissionGroup> getPermMap() {
        return permMap;
    }

    /**
     * CDI Bean instance döndürür
     *
     * @return
     */
    public static PermissionRegistery instance() {
        return BeanProvider.getContextualReference(PermissionRegistery.class, true);
    }
}
