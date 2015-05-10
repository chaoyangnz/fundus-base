package com.richdyang.fundus.meta.introspect;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.richdyang.fundus.base.bind.TypeBinder;
import com.richdyang.fundus.base.ArgumentAssert;
import com.richdyang.fundus.meta.ExpressionHelper;
import com.richdyang.fundus.meta.ExpressionHelper.Segment;
import com.richdyang.fundus.meta.reflect.ObjectAccessor;

import static com.richdyang.fundus.base.ArgumentAssert.hasLength;
import static com.richdyang.fundus.base.ArgumentAssert.notNull;
import static com.richdyang.fundus.meta.ExpressionHelper.parse;
import static com.richdyang.fundus.meta.introspect.BeanIntrospector.forClass;
import static java.lang.reflect.Modifier.isPublic;


/**
 * JavaBean accessor/mututor if your object follows JavaBean specification
 * <p>
 * use MVEL 2.0 or OGNL for best performance!!
 *
 * @author <a href="mailto:richd.yang@gmail.com">Richard Yang</a>
 * @see BeanIntrospector
 * @see ObjectAccessor
 * @since fundus
 */
public class BeanAccessor {

    protected Class clazz;
    protected Object object;
    protected BeanIntrospector classIntrospector; // cached class info for performance

    public BeanAccessor(Object object) {
        notNull(object);

        this.object = object;
        this.clazz = object.getClass();
        this.classIntrospector = forClass(clazz);
    }

    public BeanIntrospector getIntrospector() {
        if (classIntrospector == null) {
            return forClass(clazz);
        }
        return classIntrospector;
    }

    /**
     * Support propertyExpression read:
     * <pre>
     * accessor.readProperty("prop1.prop2.listprop[2].prop3");
     * </pre>
     *
     * @param propertyExpression
     * @return
     */
    public Object readNestedProperty(String propertyExpression) {
        hasLength(propertyExpression);

        BeanAccessor accessor = null;
        Object propertyValue = object;

        Segment[] segments = parse(propertyExpression);

        for (Segment segment : segments) {
            String propertyName = segment.name;

            if (propertyValue == null) {
                throw new IllegalStateException(segment.name + " is null");
            }

            accessor = (accessor == null) ? this : new BeanAccessor(propertyValue);

            propertyValue = accessor.readProperty(propertyName);

            if (segment.array) {
                if (propertyValue == null) {
                    throw new IllegalStateException(segment.name + " is null and index access failed");
                }

                int index = segment.index;

                Class<?> type = propertyValue.getClass();
                if (type.isArray()) {
                    Object[] arr = (Object[]) propertyValue;
                    if (index >= arr.length) {
                        throw new IllegalStateException(segment.name + " index out of bound");
                    }
                    propertyValue = arr[index];
                } else if (List.class.isAssignableFrom(type)) {
                    List list = (List) propertyValue;
                    if (index >= list.size()) {
                        throw new IllegalStateException(segment.name + " index out of bound");
                    }
                    propertyValue = list.get(index);
                } else {
                    throw new IllegalStateException(segment.name + " not support index access");
                }
            }
        }

        return propertyValue;
    }

    /**
     * Support propertyExpression write:
     * <pre>
     * accessor.writeProperty("prop1.prop2.listprop[2].prop3", 22);// set
     * //listprop is List or Array
     * 	or
     * accessor.writeProperty("prop1.prop2.listprop[]", new Prop()); // add
     * //listprop must be List, not support when it is Array
     * </pre>
     *
     * @param propertyExpression
     * @return
     */
    public void writeNestedProperty(String propertyExpression, Object value) {
        hasLength(propertyExpression);

        BeanAccessor accessor = null;
        Object propertyValue = object;

        Segment[] segments = parse(propertyExpression);

        for (Segment segment : segments) {
            String propertyName = segment.name;

            if (propertyValue == null) {
                throw new IllegalStateException(segment.name + " is null");
            }

            accessor = (accessor == null) ? this : new BeanAccessor(propertyValue);

            if (segment.last && !segment.array) {
                accessor.writeProperty(propertyName, value);
            } else {
                propertyValue = accessor.readProperty(propertyName);

                if (segment.array) {
                    if (propertyValue == null) {
                        throw new IllegalStateException(segment.name + " is null and index access failed");
                    }

                    int index = segment.index;

                    Class<?> type = propertyValue.getClass();
                    if (type.isArray()) {
                        Object[] arr = (Object[]) propertyValue;
                        if (index >= arr.length) {
                            throw new IllegalStateException(segment.name + " index out of bound");
                        }
                        if (segment.last) {
                            if (index == -1) {
                                throw new IllegalStateException(segment.name + " no index specified");//只允许List添加
                            } else {
                                arr[index] = value;
                            }
                        } else {
                            propertyValue = arr[index];
                        }

                    } else if (List.class.isAssignableFrom(type)) {
                        List list = (List) propertyValue;
                        if (index >= list.size()) {
                            throw new IllegalStateException(segment.name + " index out of bound");
                        }
                        if (segment.last) {
                            if (index == -1) {
                                list.add(value);
                            } else {
                                list.set(index, value);
                            }
                        } else {
                            propertyValue = list.get(index);
                        }
                    } else {
                        throw new IllegalStateException(segment.name + " not support index access");
                    }
                }
            }
        }
    }

    public Object readProperty(String propertyName) {
        hasLength(propertyName);

        Method getter = classIntrospector.getGetterMethod(propertyName);
        if (getter == null) {
            throw new IllegalStateException("the class [" + clazz.getName() + "] hasn't property getter: " + propertyName);
        }

        int modifiers = getter.getModifiers();
        if (isPublic(modifiers)) {
            try {
                return getter.invoke(object, new Object[0]);
            } catch (Exception ex) {
                throw new IllegalStateException("invoke property getter failed");
            }
        }

        throw new IllegalStateException("the class [" + clazz.getName() + "] hasn't public property getter: " + propertyName);
    }

    public void writeProperty(String propertyName, Object value) {
        hasLength(propertyName);

        PropertyDescriptor descriptor = classIntrospector.getPropertyDescriptor(propertyName);
        Method setter = classIntrospector.getSetterMethod(propertyName);
        if (setter == null) {
            throw new IllegalStateException("the class [" + clazz.getName() + "] hasn't property setter: " + propertyName);
        }

        Class cls = descriptor.getPropertyType();
        TypeBinder binder = new TypeBinder(cls);

        int modifiers = setter.getModifiers();
        if (isPublic(modifiers)) {
            try {
                Object obj = binder.bind(value);
                setter.invoke(object, obj);
                return;
            } catch (Exception ex) {
                throw new IllegalStateException("invoke property setter failed: " + setter + " with actual parameter " + value + "(" + (value != null ? value.getClass().getName() : Object.class.getName()) + ")");
            }
        }

        throw new IllegalStateException("the class [" + clazz.getName() + "] hasn't public property setter: " + propertyName);
    }

    public void writeProperties(PropertyProvider provider) {
        String[] names = getIntrospector().getWritablePropertyNames();
        for (String name : names) {
            Field field = getIntrospector().getPropertyField(name);
            if (field == null) continue;
            writeProperty(name, provider.get(name, field));
        }
    }

    public Map<String, Object> forFieldMap() {

        Map<String, Object> map = new HashMap();

        Map<String, PropertyDescriptor> properties = classIntrospector.getPropertyDescriptors();
        for (Entry<String, PropertyDescriptor> entry : properties.entrySet()) {
            String fieldName = entry.getKey();
            if ("class".equals(fieldName)) continue;
            PropertyDescriptor property = entry.getValue();


            map.put(fieldName, readProperty(property.getName()));
        }

        return map;
    }
}
