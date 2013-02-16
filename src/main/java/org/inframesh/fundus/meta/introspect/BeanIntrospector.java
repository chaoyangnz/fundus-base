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
package org.inframesh.fundus.meta.introspect;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.inframesh.fundus.base.datastruct.LRUMap;

/**
 * Bean information for <code>Class</code> and cached support provided
 * 
 * @since fundus
 * @version
 * @see BeanAccessor
 * 
 * @author <a href="mailto:josh.yoah@gmail.com">杨超</a>
 */
public class BeanIntrospector<T> {
	/*------------------For cached descriptor-------------------*/
	private static Map<Class, BeanIntrospector> INSTANCES = Collections.synchronizedMap(new LRUMap(40));// cached

	public static <T> BeanIntrospector<T> forClass(Class<T> clazz) {
		BeanIntrospector classIntrospector = INSTANCES.get(clazz);
		if (classIntrospector == null) {
			classIntrospector = new BeanIntrospector(clazz);
			INSTANCES.put(clazz, classIntrospector);
		}
		return classIntrospector;
	}

	/*----------------------------------------------------------*/

	private Class<T> clazz;
	private boolean resolved = false;

	/*--------------Bean Inspector-----------------------------*/
	private BeanInfo beanInfo;
	private Map<String, PropertyDescriptor> propertyDescriptors;
	private Map<String, PropertyDescriptor> readablePropertyDescriptors;
	private Map<String, PropertyDescriptor> writablePropertyDescriptors;

	private Map<String, MethodDescriptor> methodDescriptors;

	private BeanIntrospector(Class clazz) {
		this.clazz = clazz;
		this.propertyDescriptors = new LinkedHashMap<String, PropertyDescriptor>();
		this.readablePropertyDescriptors = new LinkedHashMap<String, PropertyDescriptor>();
		this.writablePropertyDescriptors = new LinkedHashMap<String, PropertyDescriptor>();

		this.methodDescriptors = new LinkedHashMap<String, MethodDescriptor>();

		resolve();
	}

	private void resolve() {
		if (!resolved) {

			try {
				beanInfo = Introspector.getBeanInfo(clazz);
			} catch (IntrospectionException e) {
				// e.printStackTrace();
			}

			PropertyDescriptor[] propertyDescriptorArray = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor propertyDescriptor : propertyDescriptorArray) {
				String name = propertyDescriptor.getName();
				propertyDescriptors.put(name, propertyDescriptor);

				Method getter = propertyDescriptor.getReadMethod();
				if (getter != null) {
					readablePropertyDescriptors.put(name, propertyDescriptor);
				}

				Method setter = propertyDescriptor.getWriteMethod();
				if (setter != null) {
					writablePropertyDescriptors.put(name, propertyDescriptor);
				}
			}

			MethodDescriptor[] methodDescriptorArray = beanInfo.getMethodDescriptors();
			for (MethodDescriptor methodDescriptor : methodDescriptorArray) {
				methodDescriptors.put(methodDescriptor.getName(), methodDescriptor);
			}

			resolved = true;
		}
	}

	public Class<T> getTargetClass() {
		return clazz;
	}

	public Map<String, PropertyDescriptor> getPropertyDescriptors() {
		return propertyDescriptors;
	}

	public Map<String, MethodDescriptor> getMethodDescriptors() {
		return methodDescriptors;
	}

	public String[] getPropertyNames() {
		Set<String> keyset = propertyDescriptors.keySet();
		keyset.remove("class");// exclude "class"

		return keyset.toArray(new String[keyset.size()]);
	}

	public String[] getWritablePropertyNames() {
		Set<String> keyset = writablePropertyDescriptors.keySet();

		return keyset.toArray(new String[keyset.size()]);
	}

	public String[] getReadablePropertyNames() {
		Set<String> keyset = readablePropertyDescriptors.keySet();

		return keyset.toArray(new String[keyset.size()]);
	}

	public Method getGetterMethod(String propertyName) {

		PropertyDescriptor propertyDescriptor = propertyDescriptors.get(propertyName);
		if (propertyDescriptor == null) {
			return null;
		}

		return propertyDescriptor.getReadMethod();
	}

	public Method getSetterMethod(String propertyName) {

		PropertyDescriptor propertyDescriptor = propertyDescriptors.get(propertyName);
		if (propertyDescriptor == null) {
			return null;
		}

		return propertyDescriptor.getWriteMethod();
	}

	public PropertyDescriptor getPropertyDescriptor(String propertyName) {
		return propertyDescriptors.get(propertyName);
	}

	/**
	 * 递归查找所有类层次的field
	 * 
	 * @param propertyName
	 * @return null if not found
	 */
	public Field getPropertyField(String propertyName) {
		return getFieldRecursively(clazz, propertyName);
	}

	private static Field getFieldRecursively(Class<?> clazz, String fieldName) {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			try {
				return clazz.getField(fieldName);
			} catch (NoSuchFieldException e1) {
				return getFieldRecursively(clazz.getSuperclass(), fieldName);
			} catch(Exception e2) {
				return null;
			}
		} catch(Exception ex) {
			return null;
		}
	}
}
