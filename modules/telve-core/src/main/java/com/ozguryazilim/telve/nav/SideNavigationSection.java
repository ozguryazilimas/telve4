/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
