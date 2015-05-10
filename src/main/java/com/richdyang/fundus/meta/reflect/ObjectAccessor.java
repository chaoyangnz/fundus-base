package com.richdyang.fundus.meta.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.richdyang.fundus.base.ArgumentAssert;
import com.richdyang.fundus.base.bind.TypeBinder;
import com.richdyang.fundus.meta.ExpressionHelper;

import static com.richdyang.fundus.base.ArgumentAssert.*;
import static com.richdyang.fundus.meta.ExpressionHelper.*;
import static com.richdyang.fundus.meta.reflect.ClassReflector.forClass;


/**
 * Java object accessor/mututor
 * <p>
 * use MVEL 2.0 or OGNL for best performance!!
 *
 * @author <a href="mailto:richd.yang@gmail.com">Richard Yang</a>
 * @since fundus
 */
public class ObjectAccessor {

    private Class clazz;
    private Object object;
    private ClassReflector classReflector; // cached class info for performance

    public ObjectAccessor(Object object) {
        notNull(object);

        this.object = object;
        this.clazz = object.getClass();
        this.classReflector = forClass(clazz);
    }

    public ClassReflector getReflector() {
        if (classReflector == null) {
            return forClass(clazz);
        }
        return classReflector;
    }

    public Object readField(Field field) {
        notNull(field);
        isTrue(field.getDeclaringClass().isAssignableFrom(clazz), "field " + field.getName() + "not of " + clazz.getName() + " or its superclass");

        Object value = null;

        try {
            field.setAccessible(true);
            value = field.get(object);
            field.setAccessible(false);
        } catch (IllegalArgumentException e) {
            //simplely suppressed, because shouldn't occur
        } catch (IllegalAccessException e) {
            //simplely suppressed, because shouldn't occur
        }

        return value;
    }

    public Object readField(String fieldName) {
        return readField(clazz, fieldName);
    }

    private Object readField(Class<?> inheritedClass, String fieldName) {
        hasLength(fieldName);

        ClassReflector classReflector = getClassDescriptor(inheritedClass);

        Field field = classReflector.getDeclaredField(fieldName);
        if (field == null) {
            throw new IllegalStateException("the class [" + inheritedClass.getName() + "] hasn't field: " + fieldName);
        }

        return readField(field);
    }

    public Object readNestedField(String fieldNameExpression) {
        hasLength(fieldNameExpression);

        ObjectAccessor accessor = null;
        Object fieldValue = object;
        Class<?> inheritedClass = clazz;

        Segment[] segments = parse(fieldNameExpression);

        for (Segment segment : segments) {
            String fieldName = segment.name;

            if (fieldValue == null) {
                throw new IllegalStateException(segment.name + " is null");
            }

            accessor = (accessor == null) ? this : new ObjectAccessor(fieldValue);

            inheritedClass = inheritedClass.isAssignableFrom(fieldValue.getClass()) ? inheritedClass : fieldValue.getClass();

            if ("super".equals(fieldName)) {
                inheritedClass = inheritedClass.getSuperclass();
                continue;
            }

            fieldValue = accessor.readField(inheritedClass, fieldName);

            if (segment.array) {
                if (fieldValue == null) {
                    throw new IllegalStateException(segment.name + " is null and index access failed");
                }

                int index = segment.index;

                if (index < 0) {
                    throw new IllegalStateException(segment.name + " no index specified");
                }

                Class<?> type = fieldValue.getClass();
                if (type.isArray()) {
                    Object[] arr = (Object[]) fieldValue;
                    if (index >= arr.length) {
                        throw new IllegalStateException(segment.name + " index out of bound");
                    }

                    fieldValue = arr[index];
                } else if (List.class.isAssignableFrom(type)) {
                    List list = (List) fieldValue;
                    if (index >= list.size()) {
                        throw new IllegalStateException(segment.name + " index out of bound");
                    }
                    fieldValue = list.get(index);
                } else {
                    throw new IllegalStateException(segment.name + " not support index access");
                }
            }
        }

        return fieldValue;
    }

    public void writeField(Field field, Object value) {
        notNull(field);

        try {
            boolean accessible = field.isAccessible();

            Class<?> cls = field.getType();
            TypeBinder binder = new TypeBinder(cls);

            field.setAccessible(true);
            field.set(object, binder.bind(value));
            field.setAccessible(accessible);
        } catch (IllegalArgumentException e) {
            //simplely suppressed, because shouldn't occur
        } catch (IllegalAccessException e) {
            //simplely suppressed, because shouldn't occur
        }
    }

    public void writeField(String fieldName, Object value) {
        writeField(clazz, fieldName, value);
    }

    private void writeField(Class<?> inheritedClass, String fieldName, Object value) {
        hasLength(fieldName);

        ClassReflector classReflector = getClassDescriptor(inheritedClass);

        Field field = classReflector.getDeclaredField(fieldName);
        if (field == null) {
            throw new IllegalStateException("the class [" + inheritedClass.getName() + "] hasn't field: " + fieldName);
        }

        writeField(field, value);
    }

    public void writeNestedField(String fieldNameExpression, Object value) {
        hasLength(fieldNameExpression);

        Object[] segments = split(fieldNameExpression);

        String prefix = (String) segments[0];

        Segment segment = (Segment) segments[1];

        Object fieldValue = readNestedField(prefix);
        if (fieldValue == null) {
            throw new IllegalStateException(prefix + " is null");
        }

        ObjectAccessor accessor = new ObjectAccessor(fieldValue);
        Class inheritedClass = fieldValue.getClass();
        if (!segment.array) {
            accessor.writeField(inheritedClass, segment.name, value);
        } else {
            fieldValue = accessor.readField(inheritedClass, segment.name);

            if (fieldValue == null) {
                throw new IllegalStateException(segment.name + " is null and index access failed");
            }

            int index = segment.index;

            Class<?> type = fieldValue.getClass();
            if (type.isArray()) {
                Object[] arr = (Object[]) fieldValue;
                if (index >= arr.length) {
                    throw new IllegalStateException(segment.name + " index out of bound");
                }

                if (index == -1) {
                    throw new IllegalStateException(segment.name + " no index specified");//只允许List添加
                } else {
                    arr[index] = value;
                }


            } else if (List.class.isAssignableFrom(type)) {
                List list = (List) fieldValue;
                if (index >= list.size()) {
                    throw new IllegalStateException(segment.name + " index out of bound");
                }

                if (index == -1) {
                    list.add(value);
                } else {
                    list.set(index, value);
                }

            } else {
                throw new IllegalStateException(segment.name + " not support index access");
            }
        }
    }


    public Object invokeMethod(Method method, Object... args) {
        notNull(method);
        isTrue(method.getDeclaringClass().isAssignableFrom(clazz), "method " + method.getName() + "not of " + clazz.getName() + " or its superclass");

        try {
            boolean accessible = method.isAccessible();

            method.setAccessible(accessible ? accessible : !accessible);
            Object ret = method.invoke(object, args);
            method.setAccessible(accessible);

            return ret;
        } catch (Exception ex) {

        }
        throw new IllegalStateException("Should never get here");
    }

    public Object invokeMethod(String methodName, Object... args) {
        return invokeMethod(clazz, methodName, args);
    }

    private Object invokeMethod(Class<?> inheritedClass, String methodName, Object... args) {
        hasLength(methodName);

        ClassReflector classReflector = getClassDescriptor(inheritedClass);

        args = (args == null) ? new Object[0] : args;//fixed
        Method method = classReflector.getDeclaredMethod(methodName, args);

        if (method == null) {
            throw new IllegalStateException("the class [" + object.getClass().getName() + "] hasn't method: " + methodName);
        }

        return invokeMethod(method, args);
    }

    public Object invokeNestedMethod(String methodNameExpression, Object... args) {
        hasLength(methodNameExpression);

        Object ret = null;

        Object[] segments = split(methodNameExpression);

        String prefix = (String) segments[0];
        Segment segment = (Segment) segments[1];

        Object fieldValue = readNestedField(prefix);
        if (fieldValue == null) {
            throw new IllegalStateException(prefix + " is null");
        }

        ObjectAccessor accessor = new ObjectAccessor(fieldValue);
        Class inheritedClass = fieldValue.getClass();
        if (!segment.array) {
            ret = accessor.invokeMethod(inheritedClass, segment.name, args);
        } else {
            throw new IllegalStateException(segment.name + " not valid method name");
        }

        return ret;
    }

    private ClassReflector getClassDescriptor(Class<?> inheritedClass) {
        isTrue(inheritedClass.isAssignableFrom(clazz),
                "{0} must be either the same as, or superclass/superinterface of the class/interface {1}", inheritedClass.getName(), clazz.getName());

        return (inheritedClass == clazz) ? this.classReflector : forClass(inheritedClass);
    }

    public Map<String, Object> forFieldMap(FieldFilter filter) {

        Map<String, Object> map = new HashMap<String, Object>();

        Map<String, Field> fields = classReflector.getDeclaredFields();
        for (Entry<String, Field> entry : fields.entrySet()) {
            String fieldName = entry.getKey();
            Field field = entry.getValue();

            if (field.isSynthetic()) {//exclude "this$0"
                continue;
            }
            ;

            if (filter.filter(field)) {
                map.put(fieldName, readField(field));
            }
        }

        return map;
    }

    public Map<String, Object> forFieldMap() {
        return forFieldMap(new FieldFilter() {

            public boolean filter(Field field) {
                if ("class".equals(field.getName())) {
                    return false;
                }
                return true;
            }
        });
    }

    public static interface FieldFilter {
        boolean filter(Field field);
    }
}
