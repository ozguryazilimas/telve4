package com.ozguryazilim.telve.view;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.messages.FacesMessages;
import com.ozguryazilim.telve.messages.MessagesUtils;

@FacesConverter("durationConverter")
public class DurationConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		if (Strings.isNullOrEmpty(value)) {
			return null;
		}

		String hourAcr = MessagesUtils.getMessage("general.acronym.Hour");
		String minuteAcr = MessagesUtils.getMessage("general.acronym.Minute");
		Long hours, minutes;

		String[] times = value.split(hourAcr + "|" + minuteAcr);

		Long duration = null;

		try {
			hours = Long.valueOf(times[0]);
			minutes = Long.valueOf(times[1]);
			duration = hours * 60 + minutes;
		} catch (Exception e) {
			FacesMessages.error(MessagesUtils.getMessage("general.message.InvalidDuration"));
			throw new ConverterException();
		}

		return duration;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {

		if (value instanceof Long) {

			String hourAcr = MessagesUtils.getMessage("general.acronym.Hour");
			String minuteAcr = MessagesUtils.getMessage("general.acronym.Minute");

			Long duration = (Long) value;

			long hours = duration / 60, minutes = duration % 60;

			String durationString = hours + hourAcr + minutes + minuteAcr;

			return durationString;
		}

		return null;
	}

}
