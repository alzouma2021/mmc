/**
 * 
 */
package com.siliconwise.common.locale;

import java.util.Locale;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * java.util.Locale persistence converter with JPA 2.1
 * @author GNAKALE Bernardin
 *
 */
 @Converter(autoApply=true)
public class LocalePersistenceConverter implements AttributeConverter<Locale, String> {
	
	@Override
	public String convertToDatabaseColumn(Locale locale) {
		
		if (locale == null) return null ;
		return  LocaleUtil.serialiseLocale(locale) ;
		
	}

	@Override
	public Locale convertToEntityAttribute(String databaseValue) {
		
		if (databaseValue == null) return null ;
		return LocaleUtil.deserialiseLocale(databaseValue) ;
	}


}
