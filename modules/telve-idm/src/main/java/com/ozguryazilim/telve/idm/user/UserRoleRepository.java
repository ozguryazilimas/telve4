package com.ozguryazilim.telve.idm.user;

import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.idm.entities.Role;
import com.ozguryazilim.telve.idm.entities.Role_;
import com.ozguryazilim.telve.idm.entities.User;
import com.ozguryazilim.telve.idm.entities.UserRole;
import com.ozguryazilim.telve.idm.entities.UserRole_;
import com.ozguryazilim.telve.idm.entities.User_;
import com.ozguryazilim.telve.query.QueryDefinition;
import com.ozguryazilim.telve.query.filters.Filter;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;

/**
 *
 * @author oyas
 */
@Repository
@Dependent
public abstract class UserRoleRepository extends RepositoryBase<UserRole, UserRoleViewModel> implements CriteriaSupport<UserRole>{

    /**
     * Sorgularda filtrelenecek olan person.
     */
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public List<UserRoleViewModel> browseQuery(QueryDefinition queryDefinition) {
        List<Filter<UserRole, ?, ?>> filters = queryDefinition.getFilters();
        
        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        //Geriye AccidentAnalysisViewModel dönecek cq'yu ona göre oluşturuyoruz.
        CriteriaQuery<UserRoleViewModel> criteriaQuery = criteriaBuilder.createQuery(UserRoleViewModel.class);

        //From Tabii ki PersonWorkHistory
        Root<UserRole> from = criteriaQuery.from(UserRole.class);
        Join<UserRole, User> ug = from.join(UserRole_.user, JoinType.LEFT);
        Join<UserRole, Role> rg = from.join(UserRole_.role, JoinType.LEFT);

        //Sonuç filtremiz
        criteriaQuery.multiselect(
                from.get(UserRole_.id),
                ug.get(User_.id),
                ug.get(User_.loginName),
                rg.get(Role_.id),
                rg.get(Role_.name)
        );

        //Filtreleri ekleyelim.
        List<Predicate> predicates = new ArrayList<>();
        if (user != null) {
            predicates.add(criteriaBuilder.equal(from.get(UserRole_.user).get(User_.id), user.getId()));
        }

        decorateFilters(filters, predicates, criteriaBuilder, from);

        //filtremize ekledik.
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));


        //Haydi bakalım sonuçları alalım
        TypedQuery<UserRoleViewModel> typedQuery = entityManager().createQuery(criteriaQuery);
        List<UserRoleViewModel> resultList = typedQuery.getResultList();

        return resultList;
    }
    
    public abstract UserRole findAnyByUserAndRole( User user, Role role );
    
    public abstract List<UserRole> findByUser( User user );
    public abstract List<UserRole> findByRole( Role role );
    
    public abstract List<UserRole> findByUserAndRole_active( User user, Boolean active );

    public abstract List<UserRole> findAnyByRole(Role role);
}
