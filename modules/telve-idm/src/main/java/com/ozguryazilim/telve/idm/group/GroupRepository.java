package com.ozguryazilim.telve.idm.group;

import com.ozguryazilim.telve.config.LocaleSelector;
import com.ozguryazilim.telve.data.TreeRepositoryBase;
import com.ozguryazilim.telve.entities.TreeNodeEntityBase_;
import com.ozguryazilim.telve.idm.entities.Group;
import javax.enterprise.context.Dependent;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author oyas
 */
@Repository
@Dependent
public abstract class GroupRepository extends TreeRepositoryBase<Group> implements CriteriaSupport<Group>{
    
    /**
     * Geriye yeni bir Group oluşturup döndürür.
     *
     * @param parent
     * @return
     */
    public Group newGroup(Group  parent) {
        Group entity = new Group();
        entity.setParent(parent);
        return entity;
    }

    public abstract Group findAnyByName(String name);

    public abstract Group findAnyByCode(String code);

    public abstract List<Group> findAnyByAutoCreated(Boolean autoCreated);

    @Override
    public List<Group> suggestion(String searchText) {
        Locale sessionLocale = LocaleSelector.instance().getLocale();

        List<Predicate> predicates = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        CriteriaQuery<Group> criteriaQuery = criteriaBuilder.createQuery(Group.class);

        Root<Group> from = criteriaQuery.from(Group.class);

        predicates.add(
                criteriaBuilder.or(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(from.get(TreeNodeEntityBase_.code)),
                                "%" + searchText.toLowerCase(sessionLocale) + "%"
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.lower(from.get(TreeNodeEntityBase_.name)),
                                "%" + searchText.toLowerCase(sessionLocale) + "%"
                        )
                )
        );
        predicates.add(criteriaBuilder.equal(from.get(TreeNodeEntityBase_.active), true));

        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        TypedQuery<Group> typedQuery = entityManager().createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }
}
