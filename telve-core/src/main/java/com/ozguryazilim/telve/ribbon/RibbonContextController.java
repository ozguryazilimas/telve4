/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.ribbon;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.ribbon.model.RibbonAction;
import com.ozguryazilim.telve.ribbon.model.RibbonContext;
import com.ozguryazilim.telve.ribbon.model.RibbonSection;
import com.ozguryazilim.telve.ribbon.model.RibbonSectionAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.inject.Named;
import org.picketlink.Identity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Ribbon Context tabı için gerekli mazemeyi hazırlar.
 *
 * Her seferinde yeniden hazırlanması gerektiği için Session scope'da olan
 * RibbonController'dan farklı bir sınıf
 *
 * @author Hakan Uygun
 */
@Named
@RequestScoped
public class RibbonContextController {

    private static final String HINT_AUTO_NEW_BUTTON = "autoNewButton";

    private static final Logger LOG = LoggerFactory.getLogger(RibbonContextController.class);

    private final List<RibbonSection> contextTab = new ArrayList<RibbonSection>();

    private String initedForView;

    @Inject @Any
    private Identity identity;
    
    
    @PostConstruct
    public void init() {
        initContextTab();
    }

    /**
     * Kullanıcı hakları çerçevesinde gerekli menüyü hazırlar.
     */
    protected void initContextTab() {

        //FIXME: Current viewID'yi nasıl bulacağız?
        String currentView = ""; //Conversation.instance().getViewId();

        initedForView = currentView;

        LOG.debug("Init Ribbon Context for #0", currentView);
        //View bilgisi yoksa hemen çık
        if (currentView == null) {
            return;
        }

        contextTab.clear();

        RibbonUtils ru = new RibbonUtils();
        Map<String, RibbonContext> map = RibbonRegistery.instance().getContextMap();

        for (Map.Entry<String, RibbonContext> ent : map.entrySet()) {
            if (testContextURL( currentView, ent.getKey())) {
                for (RibbonSection sec : ent.getValue().getSections()) {
                    if (sec.getPermissionDomain() == null || identity.hasPermission(sec.getPermissionDomain(), sec.getPermissionAction())) {
                        RibbonSection sn = ru.copySection(sec, identity );
                        //İçinde en az bir action varsa ekle
                        if (sn.getActions().size() > 0) {
                            contextTab.add(sn);
                        }
                    }
                }
            }
        }

        LOG.debug("Context Ribbon content : #0", contextTab);

        RibbonSection newSection = new RibbonSection();
        newSection.setId("NEW");
        newSection.setOrder(0);
        newSection.setLabel("ribbon.section.New");

        //Actionları tarayıp edit-url'i olup da viewId'ye uyanlar için new Section2ı hazırlanıyor
        Map<String, RibbonAction> actionMap = RibbonRegistery.instance().getActionMap();
        for (RibbonAction action : actionMap.values()) {
            //Eğer HINT_AUTO_NEW_BUTTON boş bırakılmış ya da true ise yeni düğmesi, render edeceğiz.
            String h = action.getHints().get(HINT_AUTO_NEW_BUTTON);
            if ( Strings.isNullOrEmpty(h) || "true".equals(h)) {
                if ("param".equals(action.getType())) {
                    //Eğer param tip ise 
                    if (action.getPermissionDomain() == null || identity.hasPermission(action.getPermissionDomain(), "select")) {
                        //ve URL'i tutuyorsa
                        String s = action.getUrl().substring(0, action.getUrl().indexOf(".xhtml"));
                        if (currentView.matches(s + ".*")) {
                            RibbonSectionAction nsa = new RibbonSectionAction();
                            nsa.setAction(action);
                            nsa.setClassifier("paramNew");
                            nsa.setType("large");

                            newSection.getActions().add(nsa);
                        }
                    }
                } else //Eğer Edit-URL'i varsa ve nomarl URL'i yoksa 
                if (!Strings.isNullOrEmpty(action.getEditUrl()) && !Strings.isNullOrEmpty(action.getUrl())) {
                    //Eğer insert hakkı varsa
                    if (action.getPermissionDomain() == null || identity.hasPermission(action.getPermissionDomain(), "insert")) {
                        //.xhtml'den öncesini alıyoruz aramak için 
                        String s = action.getEditUrl().substring(0, action.getEditUrl().indexOf(".xhtml"));
                        if (currentView.matches(s + ".*")) {
                            RibbonSectionAction nsa = new RibbonSectionAction();
                            nsa.setAction(action);
                            nsa.setClassifier("new");
                            nsa.setType("large");
                            nsa.getParams().put("eid", "0");

                            newSection.getActions().add(nsa);
                        }
                    }
                }
            }
        }

        //Eğer herhangi bir yeni action2ı varsa section'ı ekliyoruz.
        if (!newSection.getActions().isEmpty()) {
            contextTab.add(newSection);
        }

        LOG.debug("Context Ribbon content : #0", contextTab);

        ru.sortRibbonSections(contextTab);

        LOG.debug("Context Ribbon content : #0", contextTab);
    }

    /**
     * Aktif olan view için kullanıcı hakları dahilinde olan tanımlı sectionlar
     * gelecek
     *
     * @return
     */
    public List<RibbonSection> getContextSections() {
        //FIXME: Current view ID'yi nasıl bulacağız?
        String currentView = ""; //Conversation.instance().getViewId();

        LOG.debug("Ribbon Context requested for #0", currentView);

        if (currentView != null && !currentView.equals(initedForView)) {
            initContextTab();
        }

        LOG.debug("Context Ribbon content : #0", contextTab);

        return contextTab;
    }
    
    /**
     * Verilen viewID'nin verilen kurallara uyup uymadığını denetler.
     * 
     * Eğer uyuyorsa true döner.
     * 
     * eğer kural "regex:" ile başlıyorsa normal regex kullanılır. Başlamıyorsa sonuna ".*" eklenerek kontrol edilir.
     * 
     * @param viewID
     * @param rule
     * @return 
     */
    protected boolean testContextURL( String viewID, String rule ){

        String test;
        
        if( rule.contains("regex:") ){
            test = rule.split(":")[1];
        } else {
            test = rule + ".*";
        }
        
        return viewID.matches(test);
    }
}
