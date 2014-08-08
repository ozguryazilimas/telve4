package com.ozguryazilim.telve.ribbon;

import java.io.InputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.ribbon.model.RibbonAction;
import com.ozguryazilim.telve.ribbon.model.RibbonContext;
import com.ozguryazilim.telve.ribbon.model.RibbonSection;
import com.ozguryazilim.telve.ribbon.model.RibbonSectionAction;
import com.ozguryazilim.telve.ribbon.model.RibbonTab;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Ribbon XML dosyaları parse edip model sınıflarına dönüştürür.
 *
 * @author Hakan Uygun
 *
 */
public class RibbonXMLParser {

    private static final Logger LOG = LoggerFactory.getLogger(RibbonXMLParser.class);

    private static final String ATTR_ID = "id";
    private static final String ATTR_LABEL = "label";
    private static final String ATTR_PERMISSON = "permission";
    private static final String ATTR_URL = "url";
    private static final String ATTR_EDIT_URL = "edit-url";
    private static final String ATTR_TYPE = "type";
    private static final String ATTR_ORDER = "order";
    private static final String ATTR_IMG = "img";
    private static final String ATTR_HINT = "hint";
    private static final String ATTR_DEPENDS = "depends";
    private static final String ATTR_ALIGN = "align";
    private static final String ATTR_VERSION = "version";
    private static final String ATTR_VIEW = "view";
    private static final String ATTR_CLASSIFIER = "classifier";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_VALUE = "value";

    private static final String TAG_RIBBON = "ribbon";
    private static final String TAG_SECTION = "section";
    private static final String TAG_ACTIONS = "actions";
    private static final String TAG_ACTION = "action";
    private static final String TAG_RIBBON_CONTEXT = "ribbon-context";
    private static final String TAG_PARAM = "param";
    private static final String TAG_HINT = "hint";

    private RibbonRegistery registery;

    @SuppressWarnings("unchecked")
    public void parseRbnXML(InputStream in) {

        SAXReader reader = new SAXReader();
        Document doc;
        try {
            doc = reader.read(in);
            Element root = doc.getRootElement();

            LOG.info("parse file version {}", root.attributeValue(ATTR_VERSION));

            String dependency = root.attributeValue(ATTR_DEPENDS);
            if (dependency != null) {
                String[] deps = dependency.split(" ");
                LOG.debug("dependency list {}", Arrays.asList(deps));
                for (String s : deps) {
                    registery.handleDependency(s);
                }
            }

            List<Element> elements = root.elements(TAG_ACTIONS);
            if (elements.isEmpty()) {
                LOG.info("This file not contains actions");
            } else if (elements.size() > 1) {
                LOG.error("This file not contains more than one actions");
                return;
            } else {
                parseActions(elements.get(0));
            }

            elements = root.elements(TAG_RIBBON);
            if (elements.isEmpty()) {
                LOG.info("This file not contains Ribbons");
            }

            for (Element el : elements) {
                parseRibbon(el);
            }

            elements = root.elements(TAG_RIBBON_CONTEXT);
            if (elements.isEmpty()) {
                LOG.info("This file not contains Ribbon context definitions");
            }

            for (Element el : elements) {
                parseRibbonContext(el);
            }

        } catch (DocumentException e) {
            LOG.error("Document parse error", e);
        }

    }

    /**
     * ActionList parsed and resgitered
     *
     * @param e
     */
    @SuppressWarnings("unchecked")
    private void parseActions(Element e) {
        List<Element> actions = e.elements(TAG_ACTION);
        for (Element a : actions) {
            String id = a.attributeValue(ATTR_ID);
            LOG.debug("Parsing action : {}", id);

            RibbonAction action = new RibbonAction();
            action.setId(id);
            action.setLabel(a.attributeValue(ATTR_LABEL));
            action.setHint(a.attributeValue(ATTR_HINT));
            action.setImg(a.attributeValue(ATTR_IMG));

            action.setUrl(a.attributeValue(ATTR_URL));
            action.setEditUrl(a.attributeValue(ATTR_EDIT_URL));
            action.setType(a.attributeValue(ATTR_TYPE));

            String[] perm = parsePermission(a.attributeValue(ATTR_PERMISSON));
            action.setPermissionDomain(perm[0]);
            action.setPermissionAction(perm[1]);

            //Action paramlar parse ediliyor
            List<Element> elements = a.elements(TAG_PARAM);
            action.setParams(parseParams(elements));
            
            //Action hints parse ediliyor
            elements = a.elements(TAG_HINT);
            action.setHints(parseHints(elements));
            
            LOG.debug("Action : {}", action );
            
            registery.registerAction(action);
        }
    }

    @SuppressWarnings("unchecked")
    private void parseRibbon(Element e) {

        String id = e.attributeValue(ATTR_ID);
        LOG.debug("Parsing ribbon : {}", id);

        if (id == null) {
            LOG.warn("Ribbon id cannot be null!");
            return;
        }

        RibbonTab tab = null;
        //Extend edilecek mi diye bakılıyor
        tab = registery.getRibbonTab(id);
        if (tab != null) {
            LOG.debug("Extending ribbon : {}", id);
        } else {
            tab = new RibbonTab();
        }

        tab.setId(id);
        if( e.attributeValue(ATTR_LABEL) != null ){
            tab.setLabel(e.attributeValue(ATTR_LABEL));
        }
        if( e.attributeValue(ATTR_PERMISSON) != null ){
            String[] perm = parsePermission(e.attributeValue(ATTR_PERMISSON));
            tab.setPermissionDomain(perm[0]);
            tab.setPermissionAction(perm[1]);
        }

        if( e.attributeValue(ATTR_ORDER) != null ){
            try {
                int order = Integer.parseInt(e.attributeValue(ATTR_ORDER));
                tab.setOrder(order);
            } catch (NumberFormatException ex) {
                // Yapılacak bişi yok.
            }
        }

        List<Element> elements = e.elements(TAG_SECTION);
        for (Element el : elements) {
            //tab.getSections().add(parseSection(el));
            parseSection(el, tab);
        }

        registery.registerTab(tab);
    }

    /**
     * Verilen label'a sahip section arar bulamazsa null döner
     * @param tab
     * @param sectionLabel
     * @return 
     */
    private RibbonSection findTabSection( RibbonTab tab, String sectionLabel ){
        for( RibbonSection sec : tab.getSections() ){
            if( sectionLabel != null && sectionLabel.equals(sec.getLabel())){
                return sec;
            }
        }
        return null;
    }
    
    /**
     * ContextTab içinde verilen label'a sahip section bulur
     * @param tab
     * @param sectionLabel
     * @return 
     */
    private RibbonSection findContextTabSection( RibbonContext tab, String sectionLabel ){
        for( RibbonSection sec : tab.getSections() ){
            if( sectionLabel != null && sectionLabel.equals(sec.getLabel())){
                return sec;
            }
        }
        return null;
    }
    
    
    private RibbonSection parseSection(Element e, RibbonContext tab ) {
        String sectionLabel = e.attributeValue(ATTR_LABEL);
        RibbonSection section = findContextTabSection(tab, sectionLabel);
        
        if( section == null ){
            section = new RibbonSection();
            section.setLabel(e.attributeValue(ATTR_LABEL));
            try {
                int order = Integer.parseInt(e.attributeValue(ATTR_ORDER));
                section.setOrder(order);
            } catch (NumberFormatException ex) {
                // Yapılacak bişi yok.
            }
            tab.getSections().add(section);
        }
        return  parseSection(e, section );
    }
    
    private RibbonSection parseSection(Element e, RibbonTab tab) {
        String sectionLabel = e.attributeValue(ATTR_LABEL);
        RibbonSection section = findTabSection(tab, sectionLabel);
        
        if( section == null ){
            section = new RibbonSection();
            section.setLabel(e.attributeValue(ATTR_LABEL));
            try {
                int order = Integer.parseInt(e.attributeValue(ATTR_ORDER));
                section.setOrder(order);
            } catch (NumberFormatException ex) {
                // Yapılacak bişi yok.
            }
            tab.getSections().add(section);
        }
        return  parseSection(e, section );
    }
    
    @SuppressWarnings("unchecked")
    private RibbonSection parseSection(Element e, RibbonSection section) {

        LOG.debug("Section parsing");

        
        //Permission tanımlı değilse tab'ın ki ile aynıdır
        String perm = e.attributeValue(ATTR_PERMISSON);
        if (perm == null) {
			//section.setPermissionDomain(tab.getPermissionDomain());
            //section.setPermissionAction(tab.getPermissionAction());
        } else {
            String[] perms = parsePermission(e.attributeValue(ATTR_PERMISSON));
            section.setPermissionDomain(perms[0]);
            section.setPermissionAction(perms[1]);
        }

        String align = e.attributeValue(ATTR_ALIGN);
        if (align != null) {
            section.setAlignment(align);
        }

        //Section Action'ları taranıyor
        List<Element> elements = e.elements(TAG_ACTION);
        for (Element el : elements) {
            parseSectionDetails(el, section);
        }

        //tab.getSections().add(section);
        return section;
    }

    protected void parseSectionDetails(Element e, RibbonSection section) {
        String id = e.attributeValue(ATTR_ID);

        LOG.debug("Section action parsing: {}", id);

        RibbonAction act = registery.getActionMap().get(id);
        if (act == null) {
            LOG.warn("Section action not found: {}", id);
            return;
        }

        RibbonSectionAction sact = new RibbonSectionAction();
        sact.setAction(act);

        sact.setType(e.attributeValue(ATTR_TYPE));
        String s = e.attributeValue(ATTR_CLASSIFIER);
        if (s != null) {
            sact.setClassifier(s);
        }

        //Action paramlar parse ediliyor
        List<Element> elements = e.elements(TAG_PARAM);
        sact.setParams(parseParams(elements));

        section.getActions().add(sact);
    }

    /**
     * Verilen PARAM elementelerini parse edip geriye MAP olarak döndürür
     *
     * @param elements
     * @return
     */
    private Map<String, String> parseParams(List<Element> elements) {
        Map<String, String> result = new HashMap<String, String>();

        for (Element el : elements) {
            result.put(el.attributeValue(ATTR_NAME), el.attributeValue(ATTR_VALUE));
        }

        LOG.debug("Parsed Params : {}", result);

        return result;
    }
    
    /**
     * Verilen HINT elementelerini parse edip geriye MAP olarak döndürür
     *
     * @param elements
     * @return
     */
    private Map<String, String> parseHints(List<Element> elements) {
        Map<String, String> result = new HashMap<String, String>();

        for (Element el : elements) {
            result.put(el.attributeValue(ATTR_NAME), el.attributeValue(ATTR_VALUE));
        }

        LOG.debug("Parsed hints : {}", result);

        return result;
    }

    @SuppressWarnings("unchecked")
    private void parseRibbonContext(Element e) {

        String id = e.attributeValue(ATTR_VIEW);
        LOG.debug("Parsing ribbon context : {}", id);

        if (id == null) {
            LOG.warn("Ribbon context view id cannot be null!");
            return;
        }

        RibbonContext rbc = null;
        //Extend edilecek mi diye bakılıyor
        rbc = registery.getRibbonContext(id);
        if (rbc != null) {
            LOG.debug("Extending ribbon context : {}", id);
        } else {
            rbc = new RibbonContext();
        }

        rbc.setView(id);

        List<Element> elements = e.elements(TAG_SECTION);
        for (Element el : elements) {
            //rbc.getSections().add(parseSection(el));
            parseSection(el, rbc);
        }

        registery.registerRibbonContext(rbc);
    }

    /**
     * Permission stringini parse eder.
     *
     * domain:action formunda bulunur
     *
     * @param perm
     * @return
     */
    public String[] parsePermission(String perm) {
        String[] result = new String[2];

        if (Strings.isNullOrEmpty(perm)) {
            return result;
        }

        String[] sa = perm.split(":");

        if (sa.length > 0) {
            result[0] = sa[0];
        }

        if (sa.length > 1) {

            result[1] = sa[1];
        } else {
            result[1] = "select";
        }

        return result;
    }

    /**
     * Tanımlı registery bileşenini döndürür
     *
     * @return
     */
    public RibbonRegistery getRegistery() {
        return registery;
    }

    /**
     * Kullanılacak olan registery bileşenini setler
     *
     * @param registery
     */
    public void setRegistery(RibbonRegistery registery) {
        this.registery = registery;
    }

}
