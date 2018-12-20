package com.ozguryazilim.telve.idm;

import java.util.List;
import java.util.Set;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.WildcardPermission;

/**
 * Normal WildcardPermission'ın üzerinde scope tabanlı wildcard kontrolü yapar.
 * 
 * Scope tanımları $ ile başlamalı.
 * bir scope'a yetki kontrolü içinde sorgu sırasında $ ile başlamalı.
 * 
 * Örneğin: 
 * 
 * contact:select:$owner yetki tanımı için normal sorgu sırasında kayıt üzerinde bulunan owner bilgisi gönderilmeli. 
 * Bu bilgi kontrolün yapıldığı kullanının ki ile karşılaştırılacak. Yani contact:select:huygun kısmındaki huygun, subject ile karşılaştırılacak.
 * 
 * Bir scope'a yetki olup olmadının kontrolü için ise $scope şeklinde gönderilmeli. Bu taktirde yetki scopu ile veri scopu karşılaştırılacaktır.
 * 
 * 
 * @author Hakan Uygun
 */
public class ScopeWildcardPermission extends WildcardPermission{

    public ScopeWildcardPermission(String wildcardString) {
        super(wildcardString);
    }
 
    @Override
    public boolean implies(Permission p) {
        // By default only supports comparisons with other WildcardPermissions
        if (!(p instanceof ScopeWildcardPermission)) {
            return false;
        }

        ScopeWildcardPermission wp = (ScopeWildcardPermission) p;

        
        List<Set<String>> otherParts = wp.getParts();

        int i = 0;
        for (Set<String> otherPart : otherParts) {
            // If this permission has less parts than the other permission, everything after the number of parts contained
            // in this permission is automatically implied, so return true
            if (getParts().size() - 1 < i) {
                return true;
            } else {
                Set<String> part = getParts().get(i);
                if (!part.contains(WILDCARD_TOKEN) && !part.containsAll(otherPart)) {
                    return false;
                }
                i++;
            }
        }

        // If this permission has more parts than the other parts, only imply it if all of the other parts are wildcards
        for (; i < getParts().size(); i++) {
            Set<String> part = getParts().get(i);
            if (!part.contains(WILDCARD_TOKEN) && !part.contains("$owner") && !part.contains("$group")) {
                return false;
            }
        }

        return true;
    }
}
