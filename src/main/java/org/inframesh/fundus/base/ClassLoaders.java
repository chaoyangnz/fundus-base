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

/**
 * 
 * @since fundus-base
 * @version 
 *
 * @author <a href="mailto:Josh.Yoah@gmail.com">杨超 </a>
 */
public final class ClassLoaders {
	
	public static ClassLoader getDefaultClassLoader() {
		
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		}
		catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back to system class loader...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = ClassLoaders.class.getClassLoader();
		}
		
		return cl;
	}

}
