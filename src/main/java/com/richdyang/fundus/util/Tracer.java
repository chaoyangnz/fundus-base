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
package com.richdyang.fundus.util;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类，注意此类的Logger的category无法指定，因为为静态类
 * 
 * @since fundus-base
 * @version
 * 
 * @author <a href="mailto:Josh.Yoah@gmail.com">杨超 </a>
 */
public abstract class Tracer {
	private static final Logger log = LoggerFactory.getLogger(Tracer.class);

	private static String concat(String paramString, Object... args) {
		StringBuffer sbf = new StringBuffer(128);
		sbf.append(":: ").append(MessageFormat.format(paramString, args));
		return sbf.toString();
	}

	public static final void trace(String paramString) {
		log.trace(concat(paramString));
	}

	public static final void trace(String paramString,
			Object... paramArrayOfObject) {
		log.trace(concat(paramString, paramArrayOfObject));
	}

	public static final void trace(String paramString, Throwable paramThrowable) {
		log.trace(concat(paramString), paramThrowable);
	}

	public static final void debug(String paramString) {
		log.debug(concat(paramString));
	}

	public static final void debug(String paramString,
			Object... paramArrayOfObject) {
		log.debug(concat(paramString, paramArrayOfObject));
	}

	public static final void debug(String paramString, Throwable paramThrowable) {
		log.debug(concat(paramString), paramThrowable);
	}

	public static final void info(Object paramString) {
		log.info(String.valueOf(paramString));
	}

	public static final void info(String paramString,
			Object... paramArrayOfObject) {
		log.info(concat(paramString, paramArrayOfObject));
	}

	public static final void info(String paramString, Throwable paramThrowable) {
		log.info(concat(paramString), paramThrowable);
	}

	public static final void warn(String paramString) {
		log.warn(concat(paramString));
	}

	public static final void warn(String paramString,
			Object... paramArrayOfObject) {
		log.warn(concat(paramString, paramArrayOfObject));
	}

	public static final void warn(String paramString, Throwable paramThrowable) {
		log.warn(concat(paramString), paramThrowable);
	}

	public static final void error(String paramString) {
		log.error(concat(paramString));
	}

	public static final void error(String paramString,
			Object... paramArrayOfObject) {
		log.error(concat(paramString, paramArrayOfObject));
	}

	public static final void error(String paramString, Throwable paramThrowable) {
		log.error(concat(paramString), paramThrowable);
	}
}
