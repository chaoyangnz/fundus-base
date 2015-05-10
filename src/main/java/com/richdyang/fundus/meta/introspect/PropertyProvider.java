package com.richdyang.fundus.meta.introspect;

import java.lang.reflect.Field;

/**
 * @author <a href="mailto:Josh.Yoah@gmail.com">杨超 </a>
 * @since fundus-meta
 */
public interface PropertyProvider {

    /**
     * @param name
     * @param field 可能为null
     * @return
     */
    Object get(String name, Field field);

}
