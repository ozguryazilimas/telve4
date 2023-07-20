package com.ozguryazilim.telve.view;

import org.primefaces.component.inputtext.InputText;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.regex.Pattern;

@FacesValidator("emailInputTextValidator")
public class EmailInputTextValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }

        try {
            InternetAddress emailAddr = new InternetAddress((String) value);
            emailAddr.validate();
        } catch (AddressException e) {
            ((InputText) component).setValid(false);
        }
    }
}
