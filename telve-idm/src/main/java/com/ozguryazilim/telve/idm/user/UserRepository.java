/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.user;

import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.idm.entities.User;
import com.ozguryazilim.telve.idm.entities.User_;
import com.ozguryazilim.telve.query.QueryDefinition;
import com.ozguryazilim.telve.query.filters.Filter;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;

/**
 * Kullanıcı verilerine erişim için repository sınıf
 * 
 * @author Hakan Uygun
 */
@Repository
@Dependent
public abstract class UserRepository extends RepositoryBase<User, UserViewModel> implements CriteriaSupport<User>{

    public User createNew() throws InstantiationException, IllegalAccessException {
        User result = super.createNew();
        result.setUserType("STANDART");
        return result;
    }

    @Override
    public List<UserViewModel> browseQuery( QueryDefinition queryDefinition ){
        List<Filter<User, ?>> filters = queryDefinition.getFilters();
        
        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        //Geriye PersonViewModel dönecek cq'yu ona göre oluşturuyoruz.
        CriteriaQuery<UserViewModel> criteriaQuery = criteriaBuilder.createQuery(UserViewModel.class);

        //From Tabii ki User
        Root<User> from = criteriaQuery.from(User.class);
        
        
        //Sonuç filtremiz
        criteriaQuery.multiselect(
                from.get(User_.id),
                from.get(User_.loginName),
                from.get(User_.firstName),
                from.get(User_.lastName),
                from.get(User_.email),
                from.get(User_.active),
                from.get(User_.userType),
                from.get(User_.info)
        );
        
        //Filtreleri ekleyelim.
        List<Predicate> predicates = new ArrayList<>();
        
        //FIXME: Burada Grup Manager olanlar için grup bazlı bir sorgu lazım
        //Root<UserOrganization> uoFrom = criteriaQuery.from(UserOrganization.class);
        //predicates.add( criteriaBuilder.equal(uoFrom.get(UserOrganization_.username), userLookup.getActiveUser().getLoginName()));
        //predicates.add( criteriaBuilder.equal(from.get(Capa_.organization), uoFrom.get(UserOrganization_.organization)));
        
        decorateFilters(filters, predicates, criteriaBuilder, from);
        
        //Person filtremize ekledik.
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));
        
        // Öncelikle default sıralama verelim eğer kullanıcı tarafından tanımlı sıralama var ise onu setleyelim
        if ( queryDefinition.getSorters().isEmpty() )  {
            criteriaQuery.orderBy(criteriaBuilder.asc(from.get(User_.loginName)));
        } else {
            criteriaQuery.orderBy(decorateSorts(queryDefinition.getSorters(), criteriaBuilder, from));
        }
        
        //Haydi bakalım sonuçları alalım
        TypedQuery<UserViewModel> typedQuery = entityManager().createQuery(criteriaQuery);
        typedQuery.setMaxResults(queryDefinition.getResultLimit());
        List<UserViewModel> resultList = typedQuery.getResultList();

        return resultList;
    }
    
    public abstract User findAnyByLoginName( String loginname );
}
