package com.ozguryazilim.telve.idm.user;

import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.idm.entities.Group;
import com.ozguryazilim.telve.idm.entities.Group_;
import com.ozguryazilim.telve.idm.entities.User;
import com.ozguryazilim.telve.idm.entities.UserGroup;
import com.ozguryazilim.telve.idm.entities.UserGroup_;
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
public abstract class UserGroupRepository extends RepositoryBase<UserGroup, UserGroupViewModel> implements CriteriaSupport<UserGroup>{

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
    public List<UserGroupViewModel> browseQuery(QueryDefinition queryDefinition) {
        List<Filter<UserGroup, ?, ?>> filters = queryDefinition.getFilters();

        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        //Geriye AccidentAnalysisViewModel dönecek cq'yu ona göre oluşturuyoruz.
        CriteriaQuery<UserGroupViewModel> criteriaQuery = criteriaBuilder.createQuery(UserGroupViewModel.class);

        //From Tabii ki PersonWorkHistory
        Root<UserGroup> from = criteriaQuery.from(UserGroup.class);
        Join<UserGroup, User> ug = from.join(UserGroup_.user, JoinType.LEFT);
        Join<UserGroup, Group> rg = from.join(UserGroup_.group, JoinType.LEFT);

        //Sonuç filtremiz
        criteriaQuery.multiselect(
                from.get(UserGroup_.id),
                ug.get(User_.id),
                ug.get(User_.loginName),
                rg.get(Group_.id),
                rg.get(Group_.name)
        );

        //Filtreleri ekleyelim.
        List<Predicate> predicates = new ArrayList<>();
        if (user != null) {
            predicates.add(criteriaBuilder.equal(from.get(UserGroup_.user).get(User_.id), user.getId()));
        }

        decorateFilters(filters, predicates, criteriaBuilder, from);

        //filtremize ekledik.
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));


        //Haydi bakalım sonuçları alalım
        TypedQuery<UserGroupViewModel> typedQuery = entityManager().createQuery(criteriaQuery);
        List<UserGroupViewModel> resultList = typedQuery.getResultList();

        return resultList;
    }

    public abstract UserGroup findAnyByUserAndGroup( User user, Group grup );

    public abstract List<UserGroup> findByGroup( Group grup );
    public abstract List<UserGroup> findByUser( User user );

    public abstract List<UserGroup> findAnyByGroup(Group group);
}
