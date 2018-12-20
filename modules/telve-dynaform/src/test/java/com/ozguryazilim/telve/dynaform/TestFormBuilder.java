package com.ozguryazilim.telve.dynaform;

import com.ozguryazilim.telve.dynaform.calc.CalcResultCheckType;
import com.ozguryazilim.telve.dynaform.calc.CalcResultRule;
import com.ozguryazilim.telve.dynaform.calc.EqualRule;
import com.ozguryazilim.telve.dynaform.calc.HasValueRule;
import com.ozguryazilim.telve.dynaform.model.DynaBigDecimalField;
import com.ozguryazilim.telve.dynaform.model.DynaBooleanField;
import com.ozguryazilim.telve.dynaform.model.DynaCalcField;
import com.ozguryazilim.telve.dynaform.model.DynaCalcType;
import com.ozguryazilim.telve.dynaform.model.DynaDateField;
import com.ozguryazilim.telve.dynaform.model.DynaForm;
import com.ozguryazilim.telve.dynaform.model.DynaLongField;
import com.ozguryazilim.telve.dynaform.model.DynaStringField;
import com.ozguryazilim.telve.dynaform.model.DynaStringListField;
import java.util.List;

/**
 *
 * @author haky
 */
public class TestFormBuilder extends DynaFormBuilder{

    @Override
    public List<DynaForm> buildForms() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public DynaForm buildTestForm(){
        
        return createForm("test", "Test Form", "test")
                .addContainer("grp1", "Group 1", 
                        new DynaStringField("fld1", "Alan 1", "Lütfen metin girin!"),
                        new DynaStringField("fld2", "Alan 2", "Lütfen bir değer girin", "deneme"),
                        new DynaStringListField("fld3", "Liste", "Değer seçiniz", "abc,def", "HEDE"),
                        new DynaLongField("fld4", "Liste", "Değer seçiniz", 3l)
                        )
                .addContainer("Group 1", 
                        new DynaStringField("Alan 1"),
                        new DynaStringField("Alan 2", 
                                new EqualRule<>( 3l, "DEGER1"),
                                new EqualRule<>( 5l, "DEGER2"),
                                new HasValueRule( 8l ),
                                new HasValueRule( 1l, "test")
                            )
                        )
                .addContainer("FieldTypes", 
                        new DynaStringField("strFld","String Field"),
                        new DynaStringListField("lstFld","List Field","Bişiler seçiniz","abc,def", "hede"),
                        new DynaDateField("dtFld","Date Field"),
                        new DynaBooleanField("bFld","Boolean Field"),
                        new DynaLongField("lFld","Long Field","Lütfen bir değer girin"),
                        new DynaBigDecimalField("bdFld","BigDecimal Field","Lütfen bir değer girin")
                    )
                .addCalcField(new DynaCalcField("Deneme", DynaCalcType.SUM)
                    .addResultRule(new CalcResultRule("0", "Heyoooo", CalcResultCheckType.Ge))
                )
                .build();
        
    }
    
}
