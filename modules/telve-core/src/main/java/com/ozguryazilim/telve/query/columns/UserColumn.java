package com.ozguryazilim.telve.query.columns;

import com.ozguryazilim.telve.auth.UserService;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import javax.persistence.metamodel.Attribute;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.deltaspike.core.api.provider.BeanProvider;

/**
 * UserLogin bilgisi içeren bir alanı Kullanıcının gerçek adı ile sunar.
 * @author oyas
 */
public class UserColumn<E> extends Column<E> {

    private UserService userService;
    
    public UserColumn(Attribute<? super E, ?> attribute, String labelKey) {
        super(attribute, labelKey);
        userService = BeanProvider.getContextualReference(UserService.class, true);
    }

    @Override
    public String getTemplate() {
        return "userColumn";
    }

    @Override
    public void export(E e, Writer doc) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        doc.write("\"");
        doc.write( userService.getUserName(BeanUtils.getNestedProperty(e,  getName())));
        doc.write("\"");
    }
    
    
    
}
