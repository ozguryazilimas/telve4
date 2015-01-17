/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.note;

import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.entities.Note;
import com.ozguryazilim.telve.entities.Note_;
import java.util.List;
import javax.enterprise.context.Dependent;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.Criteria;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;

/**
 * Note entitysi için repository sınıfı.
 * 
 * @author Hakan Uygun
 */
@Repository
@Dependent
public abstract class NoteRepository extends RepositoryBase<Note, Note> implements CriteriaSupport<Note>{
    
    public List<Note> findNotes( String username, String attachment ){
        Criteria<Note,Note> crit = criteria();
        
        //FIXME: Buraya daha sonra kişiye özel not kontrolü de eklenecek.
        //crit.or(crit.eq(Note_.owner, username), crit.eq( Note_.permission, "ALL"));
        crit.eq(Note_.attachtment, attachment);
        
        return crit.getResultList();
    }
    
}
