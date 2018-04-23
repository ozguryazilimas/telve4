/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.reports;

/**
 * Sistem içerisinde çalışabilen rapor tipleri.
 * 
 * Şu anda sadece JasperReport destekleniyor.
 * 
 * @author Hakan Uygun
 */
public enum ReportType {
    JasperReport,
    /**
     * Geliştirici kendi rapor motorunu kullanıyor.
     * Özellikle Excel v.b. için kullanılır.
     */
    CustomReport,
    //Browse,
    //Birt,
    //3Party
}
