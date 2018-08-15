package com.ozguryazilim.telve.view;

import com.ozguryazilim.telve.messages.FacesMessages;
import com.ozguryazilim.telve.messages.Messages;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Girilen tarih değerinin bind edilen diğer bileşenlerdeki tarihlerden önce
 * veya sonra gelip gelmediğinin kontrolünün yapıldığı validator.
 *
 * @author Erdem Uslu
 */
@FacesValidator("dateRangeValidator")
public class DateRangeValidator implements Validator {

    private Date getComponentDate(UIComponent component) {
        UIInput input = (UIInput) FacesContext.getCurrentInstance().getViewRoot()
                .findComponent(component.getClientId() + ":" + component.getId() + "_inp");
        return input == null ? null : (Date) input.getValue();
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }

        UIComponent prevDateComponent = (UIComponent) component.getAttributes().get("bindRangePrev");
        UIComponent nextDateComponent = (UIComponent) component.getAttributes().get("bindRangeNext");

        Date currentDate = (Date) value;
        String currentComponentLabel = Messages.getMessage((String) component.getAttributes().get("label"));

        if (prevDateComponent != null) {

            Date prevDate = getComponentDate(prevDateComponent);

            if (prevDate != null && prevDate.after(currentDate)) {
                String prevComponentLabel = Messages.getMessage((String) prevDateComponent.getAttributes().get("label"));
                
                FacesMessages.error(Messages.getMessage("dateRange.exception.before",
                        new Object[]{prevComponentLabel, currentComponentLabel}));

                throw new ValidatorException(new FacesMessage());
            }

        }

        if (nextDateComponent != null) {

            Date nextDate = getComponentDate(nextDateComponent);

            if (nextDate != null && nextDate.before(currentDate)) {
                String nextComponentLabel = Messages.getMessage((String) nextDateComponent.getAttributes().get("label"));
                
                FacesMessages.error(Messages.getMessage("dateRange.exception.after",
                        new Object[]{nextComponentLabel, currentComponentLabel}));

                throw new ValidatorException(new FacesMessage());
            }

        }

    }

}
