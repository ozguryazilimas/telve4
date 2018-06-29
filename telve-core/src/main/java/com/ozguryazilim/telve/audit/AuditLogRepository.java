package com.ozguryazilim.telve.audit;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.entities.AuditLog;
import com.ozguryazilim.telve.entities.AuditLog_;
import com.ozguryazilim.telve.query.QueryDefinition;
import com.ozguryazilim.telve.query.filters.Filter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;

/**
 * AuditLog için repository.
 *
 * @author Hakan Uygun
 */
@Repository
@Dependent
public abstract class AuditLogRepository extends RepositoryBase<AuditLog, AuditLog> implements CriteriaSupport<AuditLog> {

    public abstract List<AuditLog> findByDateBetween(Date beginDate, Date endDate);

    @Override
    public List<AuditLog> browseQuery(QueryDefinition queryDefinition) {
        List<Filter<AuditLog, ?, ?>> filters = queryDefinition.getFilters();

        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        //Geriye PersonViewModel dönecek cq'yu ona göre oluşturuyoruz.
        CriteriaQuery<AuditLog> criteriaQuery = criteriaBuilder.createQuery(AuditLog.class);

        //From Tabii ki         
        Root<AuditLog> from = criteriaQuery.from(AuditLog.class);

        //Filtreleri ekleyelim.
        List<Predicate> predicates = new ArrayList<>();

        decorateFilters(filters, predicates, criteriaBuilder, from);

        //Person filtremize ekledik.
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        //İsme göre sıralayalım
        criteriaQuery.orderBy(criteriaBuilder.desc(from.get(AuditLog_.date)));

        //Haydi bakalım sonuçları alalım
        TypedQuery<AuditLog> typedQuery = entityManager().createQuery(criteriaQuery);
        typedQuery.setMaxResults(queryDefinition.getResultLimit());
        List<AuditLog> resultList = typedQuery.getResultList();

        return resultList;
    }

    @Query("select DISTINCT(al.category) from AuditLog al")
    public abstract List<String> findDistinctCategories();

    @Query("select DISTINCT(al.action) from AuditLog al")
    public abstract List<String> findDistinctActions();

    public void deleteByScheculedCommandParameters(
            Date date, String domain, String category, String action) {
        CriteriaBuilder builder = entityManager().getCriteriaBuilder();
        CriteriaDelete<AuditLog> delete = builder.createCriteriaDelete(AuditLog.class);
        Root<AuditLog> from = delete.from(AuditLog.class);

        List<Predicate> predicates = new ArrayList<>();

        if (date != null) {
            predicates.add(builder.lessThanOrEqualTo(from.<Date>get(AuditLog_.date), date));
        }

        if (!Strings.isNullOrEmpty(domain)) {
            predicates.add(builder.equal(from.get(AuditLog_.domain), domain));
        }

        if (!Strings.isNullOrEmpty(category)) {
            predicates.add(builder.equal(from.get(AuditLog_.category), category));
        }

        if (!Strings.isNullOrEmpty(action)) {
            predicates.add(builder.equal(from.get(AuditLog_.action), action));
        }

        delete.where(predicates.toArray(new Predicate[]{}));

        int deleteCount = entityManager().createQuery(delete).executeUpdate();

        System.out.println(deleteCount);
    }
}
