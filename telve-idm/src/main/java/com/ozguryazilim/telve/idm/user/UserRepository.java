/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.user;

import com.ozguryazilim.telve.data.ParamRepositoryBase;
import com.ozguryazilim.telve.idm.entities.User;
import com.ozguryazilim.telve.idm.entities.User_;
import javax.enterprise.context.Dependent;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;

/**
 * Kullanıcı verilerine erişim için repository sınıf
 * 
 * @author Hakan Uygun
 */
@Repository
@Dependent
public abstract class UserRepository extends ParamRepositoryBase<User, User> implements CriteriaSupport<User>{

    @Override
    protected Class<User> getViewModelClass() {
        return User.class;
    }

    @Override
    protected SingularAttribute<? super User, Long> getIdAttribute() {
        return User_.id;
    }
}
