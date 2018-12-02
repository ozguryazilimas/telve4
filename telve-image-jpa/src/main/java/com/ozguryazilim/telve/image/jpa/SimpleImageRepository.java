/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.image.jpa;

import com.ozguryazilim.telve.entities.SimpleImage;
import javax.enterprise.context.Dependent;
import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.SingleResultType;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

/**
 *
 * @author oyas
 */
@Repository
@Dependent
@Transactional
public abstract class SimpleImageRepository extends AbstractEntityRepository<SimpleImage, Long> implements CriteriaSupport<SimpleImage>{
    
    
    public abstract SimpleImage findAnyByImageKeyAndAspect( String imageKey, String aspect );
    
    @Query( value="select c.imageId from SimpleImage c where c.imageKey = ?1 and c.aspect = ?2 ", singleResult = SingleResultType.ANY)
    public abstract String findImageIdByImageKeyAndAspect( String imageKey, String aspect );
    
    public abstract SimpleImage findAnyByImageId( String imageId );
    
    public abstract void removeByImageKey( String imageKey );
    public abstract void removeByImageKeyAnsAspect( String imageKey, String aspect );
    
    public abstract long countByImageKey( String imageKey );
    public abstract long countByImageKeyAndAspect(  String imageKey, String aspect );
}
