package com.ozguryazilim.telve.view;

import org.primefaces.component.inputtext.InputText;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
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
