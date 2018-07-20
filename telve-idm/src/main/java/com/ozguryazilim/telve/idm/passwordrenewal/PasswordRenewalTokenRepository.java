/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.passwordrenewal;

import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.idm.entities.PasswordRenewalToken;
import com.ozguryazilim.telve.idm.entities.User;
import java.util.Date;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;
import javax.enterprise.context.Dependent;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.SingleResultType;

/**
 *
 * @author yusuf
 */
@Repository
@Dependent
public abstract class PasswordRenewalTokenRepository extends RepositoryBase<PasswordRenewalToken, PasswordRenewalToken> implements CriteriaSupport<PasswordRenewalToken> {

    @Query(value = "select t from PasswordRenewalToken t where t.token = ?1", singleResult = SingleResultType.OPTIONAL)
    public abstract PasswordRenewalToken hasToken(String token);

    public abstract PasswordRenewalToken findAnyByUser(User user);

}
