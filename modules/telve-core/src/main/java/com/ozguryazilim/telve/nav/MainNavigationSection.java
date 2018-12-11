/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.nav;

/**
 * Main bar için section tanımlar. 
 * 
 * Normal navigation sectionları içinde değerlendirilmez.
 * 
 * @author Hakan Uygun
 */
public class MainNavigationSection extends AbstractNavigationSection{

    @Override
    public Integer getOrder() {
        return 0;
    }
    
}
