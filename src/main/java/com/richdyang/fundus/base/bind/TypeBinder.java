package com.richdyang.fundus.base.bind;

import com.richdyang.fundus.base.Numerics;

/**
 * 
 * @since fundus-meta
 * @version 
 *
 * @author <a href="mailto:Josh.Yoah@gmail.com">杨超 </a>
 */
public class TypeBinder<T> {
	
	private Class<T> clazz;
	
	public TypeBinder(Class<T> clazz) {
		this.clazz = clazz;
	}

	public T bind(Object value) {
		if(value == null) {
			return null;
		}
		
//		Class<?> cls = value.getClass();
		
		//绑定到文本类型
		if(String.class.isAssignableFrom(clazz)) {
			return (T) String.valueOf(value);
		}
		
		//绑定到数字类型
		if(Numerics.isNumericType(clazz)) {
			return Numerics.parseNumeric(String.valueOf(value), clazz);
		}
		
//		if(Datetimes.isDatetimeType(clazz)) {
//			return Datetimes.parseDatetime(String.valueOf(value), clazz);
//		}

		return (T)value;
		
	}

}
