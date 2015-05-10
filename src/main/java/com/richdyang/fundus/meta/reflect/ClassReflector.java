package com.richdyang.fundus.meta.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.richdyang.fundus.base.datastruct.HashMultiMap;
import com.richdyang.fundus.base.datastruct.LRUMap;
import com.richdyang.fundus.base.datastruct.MultiMap;

import static java.util.Collections.synchronizedMap;

/**
 * Reflected class information for <code>Class</code> and cached support provided
 *
 * @author <a href="mailto:richd.yang@gmail.com">Richard Yang</a>
 * @see ObjectAccessor
 * @since fundus
 */
public class ClassReflector<T> {

    /*------------------For cached descriptor-------------------*/
    private static Map<Class, ClassReflector> INSTANCES = synchronizedMap(new LRUMap(40));//cached

    public static <T> ClassReflector<T> forClass(Class<T> clazz) {
        ClassReflector classDescriptor = INSTANCES.get(clazz);
        if (classDescriptor == null) {
            classDescriptor = new ClassReflector(clazz);
            INSTANCES.put(clazz, classDescriptor);
        }
        return classDescriptor;
    }
	/*----------------------------------------------------------*/

    private Class<T> clazz;
    private boolean resolved = false;

    private Map<String, Field> declaredFields; //all declared fields excluding supper class's
    private MultiMap<String, Method> declaredMethods;//all declared methods excluding supper class's
    private MultiMap<String, Constructor> declaredConstructors;

    private ClassReflector(Class clazz) {
        this.clazz = clazz;
        this.declaredFields = new HashMap<String, Field>();
        this.declaredMethods = new HashMultiMap<String, Method>();
        this.declaredConstructors = new HashMultiMap<String, Constructor>();

        resolve();
    }

    private void resolve() {
        if (!resolved) {
            Field[] fieldsArray = clazz.getDeclaredFields();
            for (Field field : fieldsArray) {
                declaredFields.put(field.getName(), field);
            }

            Method[] methodsArray = clazz.getDeclaredMethods();
            for (Method method : methodsArray) {
                declaredMethods.put(method.getName(), method);
            }

            Constructor[] constructorsArray = clazz.getDeclaredConstructors();
            for (Constructor constructor : constructorsArray) {
                declaredConstructors.put(constructor.getName(), constructor);
            }

            resolved = true;
        }
    }

    public Class<T> getTargetClass() {
        return clazz;
    }

    public Map<String, Field> getDeclaredFields() {
        return declaredFields;
    }

    public Field getDeclaredField(String fieldName) {
        return declaredFields.get(fieldName);
    }

    public MultiMap<String, Method> getDeclaredMethods() {
        return declaredMethods;
    }

    public Method getDeclaredMethod(String methodName, Object[] args) {
        Collection<Method> methods = declaredMethods.get(methodName);
        for (Method method : methods) {
            if (checkArgumentSignature(method.getParameterTypes(), args)) {
                return method;
            }
        }
        return null;
    }

    public Collection<Constructor> getDeclaredConstructors() {
        return null;//TODO
    }

    public Constructor getDeclaredConstructor(List<Class<?>> paramTypeList) {
        return null;//TODO
    }

    private static boolean checkArgumentSignature(Class[] paramTypes, Object[] args) {

        if (paramTypes == null && args == null) {
            return true;
        } else if (paramTypes != null && args != null) {
            if (paramTypes.length == args.length) {
                int len = paramTypes.length;
                for (int i = 0; i < len; ++i) {
                    if (args[i] == null) {
                        continue;
                    }

                    if (!paramTypes[i].isAssignableFrom(args[i].getClass())) {
                        return false;
                    }
                }
                return true;
            }

            return false;
        }

        return false;
    }
}
