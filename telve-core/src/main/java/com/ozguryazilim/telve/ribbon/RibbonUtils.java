/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.ribbon;

import com.ozguryazilim.telve.ribbon.model.RibbonSection;
import com.ozguryazilim.telve.ribbon.model.RibbonSectionAction;
import com.ozguryazilim.telve.ribbon.model.RibbonTab;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.picketlink.Identity;


/**
 *
 * Ribbon Tab'ları yönetmek için gerekli araç seti
 *
 * Tab, Section action kopyalama gibi metodları içerir
 *
 * @author Hakan Uygun
 */
public class RibbonUtils {

    /**
     * Kullanıcı haklarına bakarak tab listesini oluşturur.
     */
    public List<RibbonTab> findUserTabs( Identity identity ) {
        List<RibbonTab> tabs = new ArrayList<RibbonTab>();
        for (RibbonTab tab : RibbonRegistery.instance().getTabMap().values()) {
            if (tab.getPermissionDomain() == null || identity.hasPermission(tab.getPermissionDomain(), tab.getPermissionAction())) {
                tabs.add(copyTab(tab, identity ));
            }
        }
        return tabs;
    }

    /**
     *
     * Verilen RibbonTab'ın içeriğini yetki kontrolü yaparak kopyalar
     *
     * @param tab
     * @return
     */
    public RibbonTab copyTab(RibbonTab tab,  Identity identity ) {
        RibbonTab result = new RibbonTab();

        result.setId(tab.getId());
        result.setLabel(tab.getLabel());
        result.setOrder(tab.getOrder());
        result.setPermissionAction(tab.getPermissionAction());
        result.setPermissionDomain(tab.getPermissionDomain());

        for (RibbonSection sec : tab.getSections()) {
            //Sadece yetkili olunan sectionlar aktarılıyor. Eğer section üzerinde yetki bilgisi yoksa herkes için geçerli demek
            if (sec.getPermissionDomain() == null || identity.hasPermission(sec.getPermissionDomain(), sec.getPermissionAction())) {
                RibbonSection sn = copySection(sec, identity);
                //İçinde en az bir action varsa ekle
                if (sn.getActions().size() > 0) {
                    result.getSections().add(sn);
                }
            }
        }

        return result;
    }

    /**
     * Sadece kullanıcın hakkı olan işlemlerle section kopyalanıyor
     *
     * @param sec
     * @return
     */
    public RibbonSection copySection(RibbonSection sec, Identity identity ) {
        RibbonSection result = new RibbonSection();

        result.setId(sec.getId());
        result.setAlignment(sec.getAlignment());
        result.setLabel(sec.getLabel());
        result.setOrder(sec.getOrder());
        result.setPermissionAction(sec.getPermissionAction());
        result.setPermissionDomain(sec.getPermissionDomain());

        //Sadece kullanıcının hakkı olan işlemler kopyalanıyor
        for (RibbonSectionAction a : sec.getActions()) {
            if (identity.hasPermission(a.getAction().getPermissionDomain(), a.getAction().getPermissionAction())) {

                RibbonSectionAction an = new RibbonSectionAction();
                an.setAction(a.getAction());
                an.setType(a.getType());
                an.setClassifier(a.getClassifier());

                //Rendering'de kolaylık için action param'ları sectionaction'a kopyalanıyor
                an.getParams().putAll(a.getParams());
                an.getParams().putAll(a.getAction().getParams());

                //Eğer new clasifier'i varsa eid=0 parametresini url'e ekleyelim
                if ("new".equals(an.getClassifier())) {
                    an.getParams().put("eid", "0");
                }

                //Şimdide valueları işleyelim
                interpolateParamValues(an.getParams());

                //System.out.println(an.getAction().getId() + ":" + an.getParams());

                result.getActions().add(an);
            }
        }

        return result;
    }

    /**
     * Parametrelerin içinde bulunan EL değerlerini düzenler.
     *
     * context için özellikle önemli
     *
     * @param params
     */
    protected void interpolateParamValues(Map<String, String> params) {

        for (Map.Entry<String, String> param : params.entrySet()) {
            //FIXME: interpolate işini nasıl yapacağız?
            //param.setValue(Interpolator.instance().interpolate(param.getValue()));
        }

    }

    /**
     * Ribbon tablarını uygun sıraya koyar ve tab görünümlerini optimize eder.
     */
    public void optimizeRibbon(List<RibbonTab> tabs) {

        for (RibbonTab tab : tabs) {
            optimizeRibbonTab(tab);
        }

        sortRibbonTabs(tabs);
    }

    /**
     * Ribon üzerinde en uygun görünümü sağlamaya çalışır.
     *
     * 8'den daha az item varsa small olanları large yapar
     *
     * 8'den fazla 12'den az item varsa bir section'da tek başına small olan
     * itemları large yapar
     *
     * TODO: Belki ileride section merge düşünülebilir
     *
     */
    public void optimizeRibbonTab(RibbonTab tab) {

        int actCount = 0;

        for (RibbonSection sec : tab.getSections()) {
            actCount = actCount + sec.getActions().size();
        }

        if (actCount < 8) {
            for (RibbonSection sec : tab.getSections()) {
                for (RibbonSectionAction a : sec.getActions()) {
                    a.setType("large");
                }
            }
        } else if (actCount < 12) {
            for (RibbonSection sec : tab.getSections()) {
                if (sec.getActions().size() < 2) {
                    for (RibbonSectionAction a : sec.getActions()) {
                        a.setType("large");
                    }
                }
            }
        }

    }

    /**
     * Ribbon tabları uygun sıraya koyar
     */
    public void sortRibbonTabs(List<RibbonTab> tabs) {
        Collections.sort(tabs, TAB_COMPARATOR);
    }
    
    /**
     * Ribbon sectionlarını sıralar
     * @param sections 
     */
    public void sortRibbonSections( List<RibbonSection> sections ){
        Collections.sort(sections, SECTION_COMPARATOR);
    }
    
    /**
     * Tabları sıralamak için karşılaştırır
     */
    private static final Comparator<RibbonTab> TAB_COMPARATOR = new Comparator<RibbonTab>() {
        @Override
        public int compare(RibbonTab o1, RibbonTab o2) {
            //o1.getOrder() o2.getOrder();
            if (o1.getOrder() > o2.getOrder()) {
                return 1;
            } else if (o1.getOrder() < o2.getOrder()) {
                return -1;
            } else {
                return 0; //o1.getId().compareTo(o2.getId());
            }
        }
    };
    
    /**
     * Sectionları sıralamak için karşılaştırır.
     */
    private static final Comparator<RibbonSection> SECTION_COMPARATOR = new Comparator<RibbonSection>() {
        @Override
        public int compare(RibbonSection o1, RibbonSection o2) {
            //o1.getOrder() o2.getOrder();
            if (o1.getOrder() > o2.getOrder()) {
                return 1;
            } else if (o1.getOrder() < o2.getOrder()) {
                return -1;
            } else {
                return 0; //o1.getId().compareTo(o2.getId());
            }
        }
    };
}
