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
package org.inframesh.fundus.meta.generic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;

import org.inframesh.fundus.meta.reflect.ObjectAccessor;

import sun.reflect.generics.repository.MethodRepository;

/**
 * 对于Class的泛型超类、Field的泛型类型、Method的泛型形参的参数化类型
 * @since fundus
 * @version $Revision: 1.0 $Date:2010-2-24 下午05:16:24 $
 * 
 * @author <a href="mailto:richd.yang@gmail.com">Richard Yang</a>
 */ 
@SuppressWarnings("restriction")
public final class Generics {
  
    /** 
     * 通过反射,获得定义Class时声明的父类的范型参数的实际类型. 
     * <p>
     * 如public BookManager extends GenricManager&lt;Book&gt;  返回Book.class
     * 
     * @param clazz clazz The class to introspect 
     * @param classTypeArgumentIndex the Index of the generic ddeclaration,start from 0.
     * @return the first generic declaration, or <code>Object.class</code> if cannot be determined.
     * 范型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回 <code>Object.class </code>   
     */  
    public static Class getGenericTypeArgumentClassOfSuperClass(Class clazz, int classTypeArgumentIndex) throws IndexOutOfBoundsException {  
  
        Type genType = clazz.getGenericSuperclass();  
  
        if (!(genType instanceof ParameterizedType)) {  
            return Object.class;  
        }
  
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
  
        if (classTypeArgumentIndex >= params.length || classTypeArgumentIndex < 0) {  
            return Object.class;  
        }  
        if (!(params[classTypeArgumentIndex] instanceof Class)) {  
            return Object.class;  
        }  
        return (Class) params[classTypeArgumentIndex];  
    }
    
    
    /**
     * 对象的泛型参数字段的参数化类型
     * 
     * @param field
     * @param fieldTypeArgumentIndex
     * @return
     */
    public static Class getGenericTypeArgumentClassOfObjectField(Field field, int fieldTypeArgumentIndex) {
    	
    	Type fieldType = field.getGenericType();
    	
    	if(!(fieldType instanceof ParameterizedType)) {
    		throw new IllegalStateException(MessageFormat.format("{0} is not of generic type", field.getType().getName()));
    	}
  
    	Type[] paramTypes = ((ParameterizedType) fieldType).getActualTypeArguments();
    	Type componentType = paramTypes[fieldTypeArgumentIndex];
    	
    	if(!(componentType instanceof Class)) {
    		throw new IllegalStateException(MessageFormat.format("{0} is not of parameterized generic type", field.getType().getName()));
    	}
 
    	return (Class) componentType;
	}
    
	/**
	 * 方法的泛型参数的参数化类型
	 * <p>
	 * 如：
	 * void foo(List&lt;String&gt; list)
	 * 
	 * 返回 String.class
	 * 
	 * @param method
	 * @param parameterIndex
	 * @param parameterTypeArgumentIndex
	 * @return
	 */
	public static Class getGenericTypeArgumentClassOfMethodParameter(Method method, int parameterIndex, int parameterTypeArgumentIndex) {
		Type parameterType = getMethodParameterType(method, parameterIndex);
		
		if(!(parameterType instanceof ParameterizedType)) {
			throw new IllegalStateException(MessageFormat.format("{0}'s parameter {1} is not of generic type", method.getName(), parameterIndex));
		}
		
		Type[] paramTypes = ((ParameterizedType) parameterType).getActualTypeArguments();
		Type componentType = paramTypes[parameterTypeArgumentIndex];
			
		if(!(componentType instanceof Class)) {
			throw new IllegalStateException(MessageFormat.format("{0}'s parameter {1} is not of parameterized generic type", method.getName(), parameterIndex));
		}
		
		return (Class) componentType;
	}
	
	/**
	 * 返回方法的参数类型
	 * 
	 * @param method
	 * @param parameterIndex
	 * @return
	 */
	private static Type getMethodParameterType(Method method, int parameterIndex) {
		ObjectAccessor accessor = new ObjectAccessor(method);
		MethodRepository genericInfo = (MethodRepository) accessor.invokeMethod("getGenericInfo", new Object[0]);
		Type parameterType = genericInfo.getParameterTypes()[parameterIndex];
		
		return parameterType;
	}
}
