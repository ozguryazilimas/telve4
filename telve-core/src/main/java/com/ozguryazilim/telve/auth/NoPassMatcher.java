/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.auth;

import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 *
 * @author oyas
 */
public class NoPassMatcher extends SimpleCredentialsMatcher{

    @Override
    protected boolean equals(Object tokenCredentials, Object accountCredentials) {
        return true;
    }

}
