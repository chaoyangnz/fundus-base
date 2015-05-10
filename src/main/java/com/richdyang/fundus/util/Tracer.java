package com.richdyang.fundus.util;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.valueOf;
import static java.text.MessageFormat.format;

/**
 * 日志工具类，注意此类的Logger的category无法指定，因为为静态类
 *
 * @author <a href="mailto:Josh.Yoah@gmail.com">杨超 </a>
 * @since fundus-base
 */
public abstract class Tracer {
    private static final Logger log = LoggerFactory.getLogger(Tracer.class);

    private static String concat(String paramString, Object... args) {
        StringBuffer sbf = new StringBuffer(128);
        sbf.append(":: ").append(format(paramString, args));
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
        log.info(valueOf(paramString));
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
