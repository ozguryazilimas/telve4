package com.ozguryazilim.telve.ribbon;

import com.ozguryazilim.telve.api.module.TelveModuleRegistery;
import com.ozguryazilim.telve.ribbon.model.RibbonAction;
import com.ozguryazilim.telve.ribbon.model.RibbonContext;
import com.ozguryazilim.telve.ribbon.model.RibbonResourceModel;
import com.ozguryazilim.telve.ribbon.model.RibbonTab;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Sistemde tanımlı olan RibbonTab, RibbonSection ve RibbonAction'ların kaydını
 * tutar.
 *
 * @author Hakan Uygun
 *
 */
@Named
@ApplicationScoped
public class RibbonRegistery implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(RibbonRegistery.class);

    /**
     * Sistemde kayıtlı RibbonActionları id'sine göre tutar
     */
    private Map<String, RibbonAction> actionMap = new HashMap<>();

    /**
     * Sistemde kayıtlı olan tabların listesini id'ye göre tutar
     */
    private Map<String, RibbonTab> tabMap = new HashMap<>();

    /**
     * Context menüler için olan tanımları tutar : anahtar viewId
     */
    private Map<String, RibbonContext> contextMap = new HashMap<>();

    /**
     * Reosurce listesini tutar
     */
    private Map<String, RibbonResourceModel> resourceMap = new HashMap<>();

    private RibbonXMLParser parser = null;

    @PostConstruct
    public void init() {

        actionMap.clear();
        tabMap.clear();
        contextMap.clear();

        LOG.info("Ribbon Resources {}", TelveModuleRegistery.getRibbonFileNames());

        //Sadece ilk açılışta dolu geliyor zati
        //resourceMap.clear();
        for (String fd : TelveModuleRegistery.getRibbonFileNames()) {
            resourceMap.put(ribbonName(fd), new RibbonResourceModel(fd));
        }

        LOG.debug("resourceMap : {}", resourceMap);

        parser = new RibbonXMLParser();
        parser.setRegistery(this);

        for (RibbonResourceModel rm : resourceMap.values()) {
            handleRibbonXml(rm.getFileDescriptor());
        }

        LOG.debug("Ribbon Tab Map : {}", tabMap);
        LOG.debug("Ribbon Context Map : {}", contextMap);

        //for (FileDescriptor fd : myDeploymentHandler.getResources()) {
        //    handleRibbonXml(fd);
        //}
        //Aşağısı reload için kapatıldı
        //Geriye memory artığı bırakmayalım...
        //resourceMap.clear();
        //resourceMap = null;
        parser = null;

    }

    protected void handleRibbonXml(String fd) {

        String ribbonName = ribbonName(fd);
        if (resourceMap.containsKey(ribbonName)) {
            RibbonResourceModel mr = resourceMap.get(ribbonName);
            if (mr.isHandled()) {
                LOG.debug("allready handled by dependency {}", ribbonName);
                return;
            }
        } else {
            LOG.error("ribbon not found on resourceMap {}", ribbonName);
            return;
        }

        InputStream is = null;
        LOG.debug("Register file : {}", fd);
        is = this.getClass().getResourceAsStream("/"+fd);
        if (is != null) {
            //Eğer dosya yoksa bişi yapmıyoruz.
            parser.parseRbnXML(is);
        } else {
            LOG.error("ribbon resource {} not found", ribbonName);
        }
        
        RibbonResourceModel mr = resourceMap.get(ribbonName);
        mr.setHandled(true);
    }

    /**
     * Parser tarafından depency olan ribbon için çağrılır.
     *
     * @param dependency
     */
    public void handleDependency(String dependency) {
        RibbonResourceModel mr = resourceMap.get(dependency);
        if (mr == null) {
            LOG.warn("Dependency not found : {}", dependency);
            return;
        }
        if (mr.isHandled()) {
            LOG.debug("Dependency {} is already handled ", dependency);
            return;
        }
        handleRibbonXml(mr.getFileDescriptor());
    }

    /**
     * Sistemde kayıtlı actionların map'ini döndürür
     *
     * @return
     */
    public Map<String, RibbonAction> getActionMap() {
        return actionMap;
    }

    /**
     * ActionMAp'i setler
     *
     * @param actionMap
     */
    public void setActionMap(Map<String, RibbonAction> actionMap) {
        this.actionMap = actionMap;
    }

    /**
     * Tab map'ini döndürür
     *
     * @return
     */
    public Map<String, RibbonTab> getTabMap() {
        return tabMap;
    }

    /**
     * Tab map'ini setler
     *
     * @param tabMap
     */
    public void setTabMap(Map<String, RibbonTab> tabMap) {
        this.tabMap = tabMap;
    }

    /**
     * Context Ribbon için kayıt listesini döndürür
     *
     * @return
     */
    public Map<String, RibbonContext> getContextMap() {
        return contextMap;
    }

    /**
     * ContextMap'i setler
     *
     * @param contextMap
     */
    public void setContextMap(Map<String, RibbonContext> contextMap) {
        this.contextMap = contextMap;
    }

    /**
     * Geriye ID'si verilen ribbon action bilgilerini döndürür.
     *
     * @param id
     * @return
     */
    public RibbonAction getRibbonAction(String id) {
        return actionMap.get(id);
    }

    /**
     * Kayıtlardan ID'si verilen tab'ı döndürür
     *
     * @param id
     * @return
     */
    public RibbonTab getRibbonTab(String id) {
        return tabMap.get(id);
    }

    /**
     * Verilen view'a ait context ribbonu döndürür.
     *
     * @param view
     * @return
     */
    public RibbonContext getRibbonContext(String view) {
        return contextMap.get(view);
    }

    /**
     * Sisteme yeni bir action kaydeder.
     *
     * @param action
     */
    public void registerAction(RibbonAction action) {
        actionMap.put(action.getId(), action);
    }

    /**
     * Sisteme yeni bir tab kaydeder
     *
     * @param tab
     */
    public void registerTab(RibbonTab tab) {
        tabMap.put(tab.getId(), tab);
    }

    /**
     * Sisteme yeni bir context Ribbon kaydeder
     *
     * @param context
     */
    public void registerRibbonContext(RibbonContext context) {
        contextMap.put(context.getView(), context);
    }

    /**
     * Geriye verilen xxx.menu.xml isminden xxx dondurur...
     *
     * @param fileName
     * @return
     */
    private String ribbonName(String fileName) {
        return fileName.substring(0, fileName.indexOf(".rbn.xml"));
    }

    /**
     * CDI Bean instance döndürür
     *
     * @return
     */
    public static RibbonRegistery instance() {
        return BeanProvider.getContextualReference(RibbonRegistery.class, true);
    }
}
