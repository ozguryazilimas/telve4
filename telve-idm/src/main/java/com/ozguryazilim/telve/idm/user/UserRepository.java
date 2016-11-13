/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.idm.user;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.auth.UserInfo;
import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.entities.TreeNodeEntityBase;
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
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.SingleResultType;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;

/**
 * Kullanıcı verilerine erişim için repository sınıf
 *
 * @author Hakan Uygun
 */
@Repository
@Dependent
public abstract class UserRepository extends RepositoryBase<User, UserViewModel> implements CriteriaSupport<User> {

    @Inject
    private Identity identity;

    public User createNew() throws InstantiationException, IllegalAccessException {
        User result = super.createNew();
        result.setUserType("STANDART");
        return result;
    }

    @Override
    public List<UserViewModel> browseQuery(QueryDefinition queryDefinition) {
        List<Filter<User, ?>> filters = queryDefinition.getFilters();

        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        //Geriye PersonViewModel dönecek cq'yu ona göre oluşturuyoruz.
        CriteriaQuery<UserViewModel> criteriaQuery = criteriaBuilder.createQuery(UserViewModel.class);

        //From Tabii ki User
        Root<User> from = criteriaQuery.from(User.class);
        Join<User, Group> groupFrom = from.join(User_.domainGroup, JoinType.LEFT);

        //Sonuç filtremiz
        buildVieModelSelect(criteriaQuery, from, groupFrom);

        //Filtreleri ekleyelim.
        List<Predicate> predicates = new ArrayList<>();

        Boolean domainGroupcontrol = "true".equals(ConfigResolver.getPropertyValue("security.domainGroup.control", "false"));
        Boolean multiGroupcontrol = "true".equals(ConfigResolver.getPropertyValue("security.multiGroup.control", "false"));

        if (multiGroupcontrol) {
            //Eğer UI üzerinden filre gelmiş ya da domainGroupcontrol gerekiyorsa
            if (!queryDefinition.getExtraFilters().isEmpty() || domainGroupcontrol) {

                Subquery<UserGroup> subquery = criteriaQuery.subquery(UserGroup.class);
                Root fromUserGroup = subquery.from(UserGroup.class);
                //Join<UserGroup, User> sqUser = fromUserGroup.join(UserGroup_.user);
                subquery.select(fromUserGroup.get(User_.id));

                List<Predicate> subPredicates = new ArrayList<>();

                //Gelen extra filtre UserGroup bilgisi içermeli
                Filter<?, ?> f = (Filter<?, ?>) queryDefinition.getExtraFilters().get(0);
                if (f.getAttribute().equals(UserGroup_.group) && f.getValue() != null) {
                    f.decorateCriteriaQuery(subPredicates, criteriaBuilder, fromUserGroup);
                }

                //Yetki kontrolü yapacak mıyız?
                if (domainGroupcontrol) {
                    buildMultiGroupDomainControl( criteriaBuilder, subPredicates, fromUserGroup);
                }

                //Eğer detay UserGroup sorgu için filtre var sa ekleyelim...
                if (!subPredicates.isEmpty()) {
                    subPredicates.add(criteriaBuilder.equal(fromUserGroup.get(UserGroup_.user), from));
                    subquery.where(subPredicates.toArray(new Predicate[]{}));
                    predicates.add(criteriaBuilder.exists(subquery));
                }
            }
        } else if (!queryDefinition.getExtraFilters().isEmpty() || domainGroupcontrol) {
            //Eğer UI üzerinden filre gelmiş ya da domainGroupcontrol gerekiyorsa
            //Tek bir grup tanımı var o da domainGroup üzerinde dolayısı ile UserGroup tablosuna bakılmayacak.

            //Gelen extra filtre UserGroup bilgisi içermeli o yüzden burada path kontrolüne kendimiz ekliyoruz.
            Filter<?, ?> f = (Filter<?, ?>) queryDefinition.getExtraFilters().get(0);
            if (f.getAttribute().equals(UserGroup_.group) && f.getValue() != null) {
                predicates.add(criteriaBuilder.like(from.get(User_.domainGroup).get(Group_.path), ((TreeNodeEntityBase) f.getValue()).getPath() + "%"));
            }

            if (domainGroupcontrol) {
                buildSingleGroupDomainControl(criteriaBuilder, predicates, from);
            }
        }

        decorateFilters(filters, predicates, criteriaBuilder, from);

        
        buildSearchTextControl(queryDefinition.getSearchText(),criteriaBuilder, predicates, from);

        //Oluşan filtreleri sorgumuza ekliyoruz
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        // Öncelikle default sıralama verelim eğer kullanıcı tarafından tanımlı sıralama var ise onu setleyelim
        if (queryDefinition.getSorters().isEmpty()) {
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

    @Override
    public List<User> suggestion(String searchText) {
        return criteria()
                .or(
                        criteria().like(User_.loginName, "%" + searchText + "%"),
                        criteria().like(User_.firstName, "%" + searchText + "%"),
                        criteria().like(User_.lastName, "%" + searchText + "%")
                )
                .eq(User_.active, true)
                .createQuery()
                .setMaxResults(100)
                .getResultList();
    }

    
    @Override
    public List<UserViewModel> lookupQuery(String searchText) {
        return lookupQuery( searchText, null, null );
    }
    
    
    public List<UserViewModel> lookupQuery(String searchText, String userType, String groupPath ) {

        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        //Geriye PersonViewModel dönecek cq'yu ona göre oluşturuyoruz.
        CriteriaQuery<UserViewModel> criteriaQuery = criteriaBuilder.createQuery(UserViewModel.class);

        //From Tabii ki User
        Root<User> from = criteriaQuery.from(User.class);
        Join<User, Group> groupFrom = from.join(User_.domainGroup, JoinType.LEFT);

        //Sonuç filtremiz
        buildVieModelSelect(criteriaQuery, from, groupFrom);

        //Filtreleri ekleyelim.
        List<Predicate> predicates = new ArrayList<>();

        Boolean domainGroupcontrol = "true".equals(ConfigResolver.getPropertyValue("security.domainGroup.control", "false"));
        Boolean multiGroupcontrol = "true".equals(ConfigResolver.getPropertyValue("security.multiGroup.control", "false"));

        if( domainGroupcontrol ){
            if( multiGroupcontrol ){
                
                Subquery<UserGroup> subquery = criteriaQuery.subquery(UserGroup.class);
                Root fromUserGroup = subquery.from(UserGroup.class);
                //Join<UserGroup, User> sqUser = fromUserGroup.join(UserGroup_.user);
                subquery.select(fromUserGroup.get(User_.id));

                List<Predicate> subPredicates = new ArrayList<>();
                
                buildMultiGroupDomainControl(criteriaBuilder, subPredicates, fromUserGroup);
                
                //Eğer belirli bir grup için isteniyor ise
                if( !Strings.isNullOrEmpty(groupPath)){
                    subPredicates.add(criteriaBuilder.like(fromUserGroup.get(UserGroup_.group).get(Group_.path), groupPath + "%"));
                }
                
                //Eğer detay UserGroup sorgu için filtre var sa ekleyelim...
                if (!subPredicates.isEmpty()) {
                    subPredicates.add(criteriaBuilder.equal(fromUserGroup.get(UserGroup_.user), from));
                    subquery.where(subPredicates.toArray(new Predicate[]{}));
                    predicates.add(criteriaBuilder.exists(subquery));
                }
                
            } else {
                buildSingleGroupDomainControl(criteriaBuilder, predicates, from);
                
                //Eğer belirli bir grup için isteniyor ise
                if( !Strings.isNullOrEmpty(groupPath)){
                    predicates.add(criteriaBuilder.like(from.get(User_.domainGroup).get(Group_.path), groupPath + "%"));
                }
            }
        }
        
        buildSearchTextControl(searchText, criteriaBuilder, predicates, from);

        //Lookup'da geriye sadece aktifler döner.
        predicates.add( criteriaBuilder.equal(from.get(User_.active), Boolean.TRUE));
        
        //UserType ile ilgili filtreleri ekleyelim
        buildUserTypeControl(userType, criteriaBuilder, predicates, from);
        
        //Person filtremize ekledik.
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        // Öncelikle default sıralama verelim eğer kullanıcı tarafından tanımlı sıralama var ise onu setleyelim
        criteriaQuery.orderBy(criteriaBuilder.asc(from.get(User_.loginName)));

        //Haydi bakalım sonuçları alalım
        TypedQuery<UserViewModel> typedQuery = entityManager().createQuery(criteriaQuery);
        List<UserViewModel> resultList = typedQuery.getResultList();

        return resultList;
    }

    public abstract User findAnyByLoginName(String loginname);
    
    @Query(value = "select u from User u where u.loginName = ?1 and u.id != ?2 ", singleResult = SingleResultType.OPTIONAL)
    public abstract User hasLoginName(String loginname, Long id);

    public abstract List<User> findByUserTypeAndActive(String userType, Boolean active);


    //**************************************
    // Helpers
    //**************************************
    /**
     * UserViewModel için criteria selectini hazırlar.
     */
    private void buildVieModelSelect(CriteriaQuery<UserViewModel> criteriaQuery, Root<User> from, Join<User, Group> groupFrom) {
        criteriaQuery.multiselect(
                from.get(User_.id),
                from.get(User_.loginName),
                from.get(User_.firstName),
                from.get(User_.lastName),
                from.get(User_.email),
                from.get(User_.active),
                from.get(User_.userType),
                from.get(User_.info),
                groupFrom.get(Group_.id),
                groupFrom.get(Group_.name)
        );
    }

    /**
     * MultiGroup yapısında domainGroup filtelemesi
     *
     * @param criteriaBuilder
     * @param subPredicates
     * @param fromUserGroup
     */
    private void buildMultiGroupDomainControl(CriteriaBuilder criteriaBuilder, List<Predicate> subPredicates, Root fromUserGroup) {
        if( identity == null ) return;
        
        UserInfo ui = identity.getUserInfo();
        //Geriye UserInfo gelmiyor ise Login olan bir kullanıcı yok demek.
        if( ui == null ) return;
        
        //SuperAdmin için grup yetki kontrolü yapılmayacak
        if (!"SUPERADMIN".equals(ui.getUserType())) {
            //FIXME: Eğer grup tanımı yoksa ne yapalım? Şu hali ile her şeyi getirecek...
            if (!Strings.isNullOrEmpty(ui.getDomainGroupPath())) {
                Join<UserGroup, Group> o = fromUserGroup.join(UserGroup_.group, JoinType.INNER);
                subPredicates.add(criteriaBuilder.like(fromUserGroup.get(UserGroup_.group).get(Group_.path), ui.getDomainGroupPath() + "%"));
            }
        }
    }

    /**
     * Sadece User üzerinde bulunan domainGroup üzerinden domain kontrol yapılacak ise
     * @param criteriaBuilder
     * @param predicates
     * @param from 
     */
    private void buildSingleGroupDomainControl(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Root<User> from) {
        if( identity == null ) return;
        
        UserInfo ui = identity.getUserInfo();
        //Geriye UserInfo gelmiyor ise Login olan bir kullanıcı yok demek.
        if( ui == null ) return;
        
        //SuperAdmin için grup yetki kontrolü yapılmayacak
        if (!"SUPERADMIN".equals(ui.getUserType())) {
            //FIXME: Eğer grup tanımı yoksa ne yapalım? Şu hali ile her şeyi getirecek...
            if (!Strings.isNullOrEmpty(ui.getDomainGroupPath())) {
                predicates.add(criteriaBuilder.like(from.get(User_.domainGroup).get(Group_.path), ui.getDomainGroupPath() + "%"));
            }
        }
    }
    
    /**
     * Verilen searchText'i loginName, firstName ve lastName'de like ile arayacak şekilde filtreler
     * @param searchText
     * @param criteriaBuilder
     * @param predicates
     * @param from 
     */
    private void buildSearchTextControl( String searchText, CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Root<User> from ){
        if (!Strings.isNullOrEmpty(searchText)) {
            predicates.add(criteriaBuilder.or(criteriaBuilder.like(from.get(User_.loginName), "%" + searchText + "%"),
                    criteriaBuilder.like(from.get(User_.firstName), "%" + searchText + "%"),
                    criteriaBuilder.like(from.get(User_.lastName), "%" + searchText + "%")));
        }
    }
    
    
    /**
     * Eğer gelen UserType virgüller ile ayrılmış ise in ile değil ise equal ile kontrol ekler.
     * @param userType
     * @param criteriaBuilder
     * @param predicates
     * @param from 
     */
    private void buildUserTypeControl( String userType, CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Root<User> from  ){
        
        if (!Strings.isNullOrEmpty(userType)) {
            
            List<String> userTypes = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(userType);
            if( userTypes.isEmpty() ) return;
            
            if( userTypes.size() > 1 ){
                predicates.add( from.get(User_.userType).in(userTypes));
            } else {
                predicates.add( criteriaBuilder.equal( from.get(User_.userType), userTypes.get(0)));
            }
        }
    }

    
    /**
     * Kullanıcının gruplarına ve alt gruplarına üye kullanıcıların listesini döndürür.
     * TODO: Native sorgu yerine criteria'ya çevirelim.
     * @param loginName sorgu için baz alınacak olan kullanıcı
     * @return 
     */
//  Multi Grup desteği olan durumda tüm grup üyeleri üzerinde arama yapan sorgu
//    @Query(value = "select uu.LOGIN_NAME from TLI_USER uu\n" +
//                    "inner join TLI_USER_GROUP ugg on uu.ID = ugg.USER_ID\n" +
//                    "inner join TLI_GROUP gm on gm.ID = ugg.GROUP_ID\n" +
//                    "inner join \n" +
//                    "( SELECT concat( g.PATH , '%' ) as grpPath FROM TLI_USER u \n" +
//                    "inner join TLI_USER_GROUP ug on u.ID = ug.USER_ID\n" +
//                    "inner join TLI_GROUP g on ug.GROUP_ID = g.ID\n" +
//                    "where LOGIN_NAME = ?1 ) gg on gm.PATH like gg.grpPath", isNative = true)
    //Etki grubu üzerinden kontrol yapmak daha makul
    @Query(value =  "select u.LOGIN_NAME FROM TLI_USER u \n" +
                    "INNER JOIN TLI_GROUP g on u.GROUP_ID = g.ID\n" +
                    "INNER JOIN (\n" +
                    "SELECT concat( g.PATH , '%' ) as grpPath  FROM TLI_USER u \n" +
                    "INNER JOIN TLI_GROUP g on u.GROUP_ID = g.ID\n" +
                    "WHERE u.LOGIN_NAME = ?1 ) gg on g.path like gg.grpPath", isNative = true)
    public abstract List<String> findAllGroupMembers(String loginName);
}
