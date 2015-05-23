/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.dynaform;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.dynaform.model.DynaCalcField;
import com.ozguryazilim.telve.dynaform.model.DynaContainer;
import com.ozguryazilim.telve.dynaform.model.DynaField;
import com.ozguryazilim.telve.dynaform.model.DynaForm;
import java.io.Serializable;
import java.util.List;

/**
 * DynaForm modeli oluşturmak için builder base sınıf.
 * 
 * Örnek Form build
 * 
 * public DynaForm buildTest(){
        return createForm("frm", "label", "ggrooop")
                .setPermission("FORM1")
                .setMetaData("file", "hede.json")
                .addContainer("grp1", "gup lbl", 
                        new DynaStringField("", "", ""),
                        new DynaStringField("", "", ""))

                .addContainer("grp1", "gup lbl", 
                        new DynaStringField("", "", ""),
                        new DynaStringField("", "", ""))
                .addCalcField( new CalcField(...))
                .build();
    }
 * 
 * 
 * @author Hakan Uygun
 */
public abstract class DynaFormBuilder implements Serializable{
    
    private DynaForm form;
    
    public DynaFormBuilder createForm( String id, String label, String domain ){
        this.form = new DynaForm();
        this.form.setId(id);
        this.form.setLabel(label);
        this.form.setDomain(domain);
        
        return this;
    }
    
    public DynaFormBuilder addContainer( String id, String label, DynaField... fields){
        DynaContainer c = new DynaContainer(id, label);
        
        for( DynaField f : fields ){
            c.addField(f);
        }
        
        form.getContainers().add(c);
        return this;
    }
    
    
    public DynaFormBuilder addContainer( String label, DynaField... fields){
        
        String id = form.getId() + ":" + form.getContainers().size();
        
        return addContainer( id, label, fields );
    }
    
    public DynaFormBuilder setPermission( String perm ){
        this.form.setPermission(perm);
        return this;
    }
    
    public DynaFormBuilder setMetaData( String key, String value ){
        this.form.setMetaData(key + value);
        return this;
    }

    public DynaFormBuilder setVersion( String value ){
        this.form.setVersion( value );
        return this;
    }
    
    /**
     * 3 karakterlik bir seri no prefixi
     * @param prefix
     * @return 
     */
    public DynaFormBuilder setProfix( String prefix ){
        this.form.setPrefix( prefix );
        return this;
    }
    
    public DynaFormBuilder setDoubleColumn( Boolean b ){
        this.form.setDoubleColumn( b );
        return this;
    }
    
    public DynaFormBuilder addCalcField( DynaCalcField field ){
        if( Strings.isNullOrEmpty(field.getId())){
            field.setId(this.form.getId() + "::" + form.getCalcFields().size());
        }
        this.form.getCalcFields().add(field);
        return this;
    }
    
    public DynaForm build(){
        return form;
    }
    
    public abstract List<DynaForm> buildForms();
    
}
