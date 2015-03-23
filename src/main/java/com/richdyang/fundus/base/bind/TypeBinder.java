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
