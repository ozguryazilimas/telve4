/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command;

/**
 * Telve MessageBus komutlarının bu interface'i implemente etmesi gerek.
 * 
 * @author Hakan Uygun
 * @param <C> Karşılayıp çalışacağı komut sınıfı
 */
public abstract class AbstractCommandExecuter<C extends Command>{
    
    /**
     * Çalıştırılacak komut bilgisi parametre olarak gelir.
     * @param command 
     */
    public abstract void execute(C command);
    
}
