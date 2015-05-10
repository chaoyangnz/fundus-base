package com.richdyang.fundus.base;

import static java.lang.Thread.currentThread;

/**
 * @author <a href="mailto:Josh.Yoah@gmail.com">杨超 </a>
 * @since fundus-base
 */
public final class ClassLoaders {

    public static ClassLoader getDefaultClassLoader() {

        ClassLoader cl = null;
        try {
            cl = currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back to system class loader...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassLoaders.class.getClassLoader();
        }

        return cl;
    }

}
