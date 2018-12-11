/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.WildcardPermissionResolver;

/**
 *
 * @author oyas
 */
public class ScopeWildcardPermissonResolver extends WildcardPermissionResolver{

    @Override
    public Permission resolvePermission(String permissionString) {
        return new ScopeWildcardPermission(permissionString);
    }
    
}
