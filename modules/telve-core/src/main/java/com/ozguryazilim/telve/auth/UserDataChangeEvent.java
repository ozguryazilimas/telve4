/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

/**
 * Kullanıcı verileri üzerinde her hangi bir değişiklik olduğunda fırlatılacan bir mesajdır.
 * 
 * @author Hakan Uygun
 */
public class UserDataChangeEvent {
    
    public String username;

    public UserDataChangeEvent() {
    }

    public UserDataChangeEvent(String username) {
        this.username = username;
    }


    /**
     * Verisinde değişiklik yapılan kullanıcı adı
     * @return 
     */
    public String getUsername() {
        return username;
    }

    /**
     * Verisinde değişiklik yapılan kullanıcı adı
     * @param username 
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    
    
}
