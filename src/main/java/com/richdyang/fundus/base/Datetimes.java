
package com.richdyang.fundus.base;

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
