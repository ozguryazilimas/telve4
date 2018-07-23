/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.passwordrenewal;

import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.idm.entities.PasswordRenewalToken;
import com.ozguryazilim.telve.idm.entities.User;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;
import javax.enterprise.context.Dependent;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.SingleResultType;

/**
 *
 * @author yusuf
 */
@Repository
@Dependent
public abstract class PasswordRenewalTokenRepository extends RepositoryBase<PasswordRenewalToken, PasswordRenewalToken> implements CriteriaSupport<PasswordRenewalToken> {

    public abstract PasswordRenewalToken findAnyByToken(String token);

    public abstract PasswordRenewalToken findAnyByUser(User user);

}
