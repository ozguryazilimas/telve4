/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.contact;

import java.io.Serializable;
import java.util.Map;

/**
 * Telve içinde haberleşme ve yönlendirme kanallarında Kullanılacak contact model
 * 
 * @author Hakan Uygun
 */
public class Contact implements Serializable{
   
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String mobile;
    private String fax;
    private String address;
    private String jabber;
    private String type;
    private String source;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJabber() {
        return jabber;
    }

    public void setJabber(String jabber) {
        this.jabber = jabber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "cs=contact;type=" + type + "; id=" + id + "; firstname=" + firstname + "; lastname=" + lastname + "; email=" + email + "; phone=" + phone + "; mobile=" + mobile + "; fax=" + fax + "; address=" + address + "; jabber=" + jabber;
    }

    public void pushToMap( Map<String,Object> params ){
        params.put("contactType", type);
        params.put("id", id);
        params.put("firstname", firstname);
        params.put("lastname", lastname);
        params.put("email", email);
        params.put("phone", phone);
        params.put("mobile", mobile);
        params.put("fax", fax);
        params.put("address", address);
        params.put("jabber", jabber);
    }
    
    
}
