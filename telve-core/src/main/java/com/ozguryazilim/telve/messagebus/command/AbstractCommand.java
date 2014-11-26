/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command;

/**
 * Command interface'i için taban sınıf.
 * 
 * Kullanıcının göreceği komut adını sınıf adı olarak döndürür.
 * 
 * @author Hakan Uygun
 */
public abstract class AbstractCommand implements Command{

    /**
     * Geriye komut adı olarak sınıf adını döndürür.
     * @return 
     */
    @Override
    public String getName(){
        return this.getClass().getSimpleName();
    }
    
}
