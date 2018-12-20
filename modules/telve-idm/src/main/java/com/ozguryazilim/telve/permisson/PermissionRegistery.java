package com.ozguryazilim.telve.permisson;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
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
import org.apache.deltaspike.core.api.config.ConfigResolver;
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
public class PermissionRegistery implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(PermissionRegistery.class);

    private static final Integer DEFAULT_ORDER = 50;

    private static final String ATTR_ORDER = "order";
    private static final String ATTR_SCOPE = "scope";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_TARGET = "target";
    private static final String ATTR_IMPORTANT = "important";

    private static final String ELEM_PERMISSON_GROUP = "permissionGroup";
    private static final String ELEM_PERMISSON = "permission";
    private static final String ELEM_ACTION = "action";
    private static final String ELEM_ENTITY_ACTIONS = "entityActions";
    private static final String ELEM_EXCLUDE = "exclude";

    private final Map<String, PermissionGroup> permMap = new HashMap<>();
    private final List<PermissionGroup> permissionGroups = new ArrayList<>();
    private final Map<String, PermissionDefinition> permissionMap = new HashMap<>();

    @PostConstruct
    public void init() {
        LOG.info("DeploymentHandler Resources {}", TelveModuleRegistery.getPermissonFileNames());

        for (String fd : TelveModuleRegistery.getPermissonFileNames()) {
            handlePermXml(fd);
        }

        sortData();
    }

    /**
     * Tüm dosyalardan perm'ler geldi şimdi sıralayalım
     */
    protected void sortData() {
        //Şimdi herşeyi bir sıraya koyalım
        permissionGroups.addAll(permMap.values());
        permissionGroups.sort((PermissionGroup t, PermissionGroup t1) -> {
            Integer i = t.getOrder().compareTo(t1.getOrder());
            if (i != 0) {
                return i;
            }
            return t.getName().compareTo(t1.getName());
        });

        permissionGroups.forEach((pg) -> {
            //definitionları da sıralayalım
            pg.getDefinitions().sort((PermissionDefinition t, PermissionDefinition t1) -> {
                Integer i = t.getOrder().compareTo(t1.getOrder());
                if (i != 0) {
                    return i;
                }
                return t.getName().compareTo(t1.getName());
            });

            //actionları sıralayalım
            pg.getActions().sort((PermissionAction t, PermissionAction t1) -> {
                Integer i = t.getOrder().compareTo(t1.getOrder());
                if (i != 0) {
                    return i;
                }
                return t.getName().compareTo(t1.getName());
            });

            pg.getDefinitions().forEach((pd) -> {
                permissionMap.put(pd.getName(), pd);
            });
        });
    }

    public void handlePermXml(String fd) {

        InputStream is = null;
        try {
            LOG.info("Register file : {}", fd);
            is = this.getClass().getResourceAsStream("/" + fd);

            if (is == null) {
                LOG.warn("Permission file not found: {}", fd);
                return;
            }

            SAXReader reader = new SAXReader();
            Document doc = reader.read(is);
            Element root = doc.getRootElement();
            List<Element> elements = root.elements(ELEM_PERMISSON_GROUP);
            for (Element e : elements) {
                registerPermissionGroup(e);
            }

        } catch (DocumentException ex) {
            LOG.error("perm cannot read", ex);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                LOG.error("perm cannot read", ex);
            }
        }

    }

    /**
     * Verilen elementteki grupları ve altındaki permissionları toplar
     *
     * @param e
     */
    private void registerPermissionGroup(Element e) {
        String gn = e.attributeValue(ATTR_NAME);
        String ors = e.attributeValue(ATTR_ORDER);
        String imps = e.attributeValue(ATTR_IMPORTANT);
        Integer or = DEFAULT_ORDER;

        if (!Strings.isNullOrEmpty(ors)) {
            try {
                or = Integer.parseInt(ors);
            } catch (NumberFormatException ex) {
                //Yapılacka bir şey yok. eğer hata varsa 50 default alacağız.
                LOG.warn("Order format exception", ex);
            }
        }

        //Tüm data toplandı şimdi map'e ekleniyor...
        PermissionGroup pg = permMap.get(gn);
        if (pg == null) {
            pg = new PermissionGroup(gn, or);
            permMap.put(gn, pg);
        } else {
            //Order düzenleme eğer üzerinde default order varsa ya da important true ise yenisini yazıyoruz.
            if (pg.getOrder() == 50 || "true".equals(imps)) {
                pg.setOrder(or);
            }
        }

        List<Element> elements = e.elements(ELEM_PERMISSON);
        for (Element a : elements) {
            registerPermission(a, pg);
        }
    }

    /**
     * Verilen elementten ( permission ) actionları toplar ve register eder.
     *
     * @param e
     * @param gn
     */
    private void registerPermission(Element e, PermissionGroup pg) {
        //Eski perm.xml'leri desteklemek için name dışında target ismini de kabul ediyoruz.
        Attribute target = e.attribute(ATTR_NAME);
        if (target == null) {
            target = e.attribute(ATTR_TARGET);
        }
        String ors = e.attributeValue(ATTR_ORDER);
        Integer or = DEFAULT_ORDER;

        String t = target.getText();

        //Önce configden bakalım dışarı atılıyor mu?
        if ("true".equals(ConfigResolver.getPropertyValue("permission.exclude." + t, "false"))) {
            return;
        }

        //Order değeri alalım
        if (!Strings.isNullOrEmpty(ors)) {
            try {
                or = Integer.parseInt(ors);
            } catch (NumberFormatException ex) {
                //Yapılacka bir şey yok. eğer hata varsa 50 default alacağız.
                LOG.warn("Order format exception", ex);
            }
        }

        List<PermissionAction> al = new ArrayList<>();

        populateEntityActions(e, al);
        populateActions(e, al);
        populateExculedActions(e, al);

        PermissionDefinition pd = new PermissionDefinition(t, or);
        pd.getActions().addAll(al);
        pg.getDefinitions().add(pd);

        //Şimdide grup actionlarını düzenleyelim
        al.stream()
                .filter((a) -> (!pg.getActions().contains(a)))
                .forEachOrdered((a) -> {
                    pg.getActions().add(a);
                });
    }

    /**
     * Verilen elementteki ( permission ) entityAction elementi varmı bakar
     * varsa hakları listeye ekler
     *
     * @param e
     * @param als
     */
    private void populateEntityActions(Element e, List<PermissionAction> als) {
        Element element = e.element(ELEM_ENTITY_ACTIONS);
        if (element != null) {
            //verilen scope değerleri varsa ekleyelim.
            String scp = element.attributeValue(ATTR_SCOPE);
            List<String> scps = new ArrayList<>();
            if (!Strings.isNullOrEmpty(scp)) {
                scps = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(scp);
            }

            als.add(new PermissionAction(ActionConsts.SELECT_ACTION, 1, scps));

            //Insert için scope'un bir anlamı yoktur ne yapalım?
            als.add(new PermissionAction(ActionConsts.INSERT_ACTION, 2));

            als.add(new PermissionAction(ActionConsts.UPDATE_ACTION, 3, scps));
            als.add(new PermissionAction(ActionConsts.DELETE_ACTION, 4, scps));
            als.add(new PermissionAction(ActionConsts.EXPORT_ACTION, 5, scps));
            als.add(new PermissionAction(ActionConsts.EXEC_ACTION, 6, scps));
        }

    }

    /**
     * verilen elementteki ( permission ) action elementelerini listeye ekler
     *
     * @param e
     * @param als
     */
    private void populateActions(Element e, List<PermissionAction> als) {
        List<Element> elements = e.elements(ELEM_ACTION);
        for (Element a : elements) {
            LOG.debug("element : {} ; action : {}", e, a.attributeValue(ATTR_NAME));

            String ors = a.attributeValue(ATTR_ORDER);
            Integer or = DEFAULT_ORDER;

            //Order değeri alalım
            if (!Strings.isNullOrEmpty(ors)) {
                try {
                    or = Integer.parseInt(ors);
                } catch (NumberFormatException ex) {
                    //Yapılacka bir şey yok. eğer hata varsa 50 default alacağız.
                    LOG.warn("Order format exception", ex);
                }
            }

            String scp = a.attributeValue(ATTR_SCOPE);
            List<String> scps = new ArrayList<>();
            if (!Strings.isNullOrEmpty(scp)) {
                scps = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(scp);
            }

            als.add(new PermissionAction(a.attributeValue(ATTR_NAME), or, scps));

        }
    }

    /**
     * verilen elementteki ( permission ) exclude elemetlerini listeden
     * çıkartır.
     *
     * Bir exclude için bir action bulmayı umuyoruz. Aynı action iki kez
     * gelmemeli.
     *
     * @param e
     * @param als
     */
    private void populateExculedActions(Element e, List<PermissionAction> als) {
        List<Element> elements = e.elements(ELEM_EXCLUDE);
        for (Element ea : elements) {
            PermissionAction toRemove = null;
            for (PermissionAction a : als) {
                if (a.getName().equals(ea.attributeValue(ATTR_NAME))) {
                    toRemove = a;
                }
            }

            if (toRemove != null) {
                als.remove(toRemove);
            }

        }
    }

    public Map<String, PermissionGroup> getPermMap() {
        return permMap;
    }

    public List<PermissionGroup> getPermissionGroups() {
        return permissionGroups;
    }

    public Map<String, PermissionDefinition> getPermissionMap() {
        return permissionMap;
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
