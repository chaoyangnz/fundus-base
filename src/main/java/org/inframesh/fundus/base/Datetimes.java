/* 
 *  Copyright (c)2009-2010 The Inframesh Software Foundation (ISF)
 *
 *  Licensed under the Inframesh Software License (the "License"), 
 *	Version 1.0 ; you may obtain a copy of the license at
 *
 *  	http://www.inframesh.org/licenses/LICENSE-1.0
 *
 *  Software distributed under the License is distributed  on an "AS IS" 
 *  BASIS but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the License 
 *  for more details.
 *  
 *  Inframesh Software Foundation is donated by Drowell Technology Limited.
 */ 
package org.inframesh.fundus.base;

import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @since fundus-base
 * @version 
 *
 * @author <a href="mailto:Josh.Yoah@gmail.com">杨超 </a>
 */
public final class Datetimes {
	
	/**
	 * <pre>
	 * java.util.Date
	 *  	|- java.sql.Date
	 *  	|- java.sql.Time
	 *  	|- java.sql.Timestamp
	 * java.util.Calendar
	 *  	|- java.util.GregorianCalendar
	 * </pre>
	 * @param clazz
	 * @return
	 */
	public static boolean isDatetimeType(Class<?> clazz) {
		return Date.class.isAssignableFrom(clazz) || Calendar.class.isAssignableFrom(clazz);
	}
	
	/**
	 * //TODO
	 * @param <T>
	 * @param text
	 * @param targetClass
	 * @return
	 */
	public static <T> T parseDatetime(String text, Class<T> targetClass) {
		return null;
	}
}
