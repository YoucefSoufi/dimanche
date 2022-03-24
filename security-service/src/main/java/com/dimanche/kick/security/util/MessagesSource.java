package com.dimanche.kick.security.util;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessagesSource implements MessageSourceAware {

	MessageSource messageSource;
	Locale locale = LocaleContextHolder.getLocale();

	public String getMessage(String message) {
		return messageSource.getMessage(message, null, locale);
	}

	public String getMessage(String message, Object[] arg) {
		return messageSource.getMessage(message, arg, locale);
	}

	public String getMessage(String message, Object arg) {
		return messageSource.getMessage(message, oneObjectToListObject(arg), locale);
	}

	public Object[] oneObjectToListObject(Object o) {
		Object[] listObject = new Object[] { 0 };
		listObject[0] = o;
		return listObject;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;

	}
}
