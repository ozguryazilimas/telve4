package com.ozguryazilim.telve.nav;

/**
 * Sidebar üst kısmını tanımlar. 
 * 
 * Normal navigation sectionları içinde değerlendirilmez.
 * 
 * @author Hakan Uygun
 */
public class SideNavigationSection extends AbstractNavigationSection{

    @Override
    public Integer getOrder() {
        return 0;
    }
    
}
