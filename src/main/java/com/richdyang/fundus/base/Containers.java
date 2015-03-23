
package com.richdyang.fundus.base;

import static com.richdyang.fundus.base.ArgumentAssert.notNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;

/**
 * For Array, Collection, Map and Enumation
 * 
 * @since fundus
 * @version 
 *
 * @author <a href="mailto:richd.yang@gmail.com">Richard Yang</a>see Collections
 */
public final class Containers {
	
	/**
	 * Determin whether an array has elements, it must be not null and must have at least one element
	 * 
	 * @param array
	 * @return
	 */
	public static <T> boolean isEmpty(T[] array) {
		return (array == null || array.length == 0);
	}
	
	/**
	 * Determin whether an array contains null elements<br>
	 * NOTE: it doesn't complain when array is null, JUST simplely return false
	 * @param array
	 * @return
	 */
	public static <T> boolean hasNullElements(T[] array) {
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				if (array[i] == null) {
					return true;
				}
			}
		}		
		return false;
	}
	
	/**
	 * Determine whether the given Collection only contains a single unique object.
	 * @param collection the Collection to check
	 * @return <code>false</code> if the collection contains multiple references 
	 * to the same instance, <code>true</code> else
	 */
	public static <T> boolean hasUniqueObject(T[] array) {
		if (isEmpty(array)) {
			return true;
		}
		for (Object elem : array) {
			int count = 0;
			for(Object ele : array) {
				if(elem == ele) {
					count++;
				}
			}
			if(count > 1) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Determine whether the given Collection only contains a single unique object 
	 * by <code>equals()</code> method.
	 * @param array the array to check
	 * @return <code>false</code> if the collection contains multiple equals Object 
	 * by their <code>equals()</code>, <code>true</code> else
	 */
	public static <T> boolean hasUniqueEqualsObject(T[] array) {
		if (isEmpty(array)) {
			return true;
		}
		for (Object elem : array) {
			int count = 0;
			for(Object el : array) {
				if(Objects.equals(elem, el)) {
					count++;
				}
			}
			if(count > 1) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Get a new array after add a object<p>
	 * 
	 * Array.asList(T..) return List cannot add(..)
	 * 
	 * @param array
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] addElements(T[] array, T...objs) {
		if(objs == null) {
			return array;
		}
		
		Class compType = Object.class;
		if (array != null) {
			compType = array.getClass().getComponentType();
		} else if (objs != null) {
			compType = objs.getClass().getComponentType();
		}
		
		int newArrLength = (array != null ? array.length + objs.length : objs.length);
		
		List<T> list = new ArrayList<T>();//Arrays.asList(array);
		for(T element : array) {
			list.add(element);
		}
		for (T element : objs) {
			list.add(element);
		}
//		Collections.addAll(list, objs);
		
		return list.toArray((T[])Array.newInstance(compType, newArrLength));
	}
	
	/**
	 * Return <code>true</code> if the supplied Collection is <code>null</code>
	 * or empty. Otherwise, return <code>false</code>.
	 * @param collection the Collection to check
	 * @return whether the given Collection is empty
	 */
	public static boolean isEmpty(Collection collection) {
		return (collection == null || collection.isEmpty());
	}
	
	/**
	 * Determin whether an array contains null elements<br>
	 * NOTE: it doesn't complain when array is null, JUST simplely return false
	 * @param array
	 * @return
	 */
	public static boolean hasNullElements(Collection collection) {
		if (collection != null) {
			Iterator iter = collection.iterator();
			while(iter.hasNext()) {
				if (iter.next() == null) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Return <code>true</code> if the supplied Map is <code>null</code>
	 * or empty. Otherwise, return <code>false</code>.
	 * @param map the Map to check
	 * @return whether the given Map is empty
	 */
	public static boolean isEmpty(Map map) {
		return (map == null || map.isEmpty());
	}

	/**
	 * Merge the given Properties instance into the given Map,
	 * copying all properties (key-value pairs) over.
	 * <p>Uses <code>Properties.propertyNames()</code> to even catch
	 * default properties linked into the original Properties instance.
	 * @param props the Properties instance to merge (may be <code>null</code>)
	 * @param map the target Map to merge the properties into
	 */
	@SuppressWarnings("unchecked")
	public static void mergePropertiesIntoMap(Properties props, Map map) {
		ArgumentAssert.notNull(map, "Map must not be null");

		if (props != null) {
			for (Enumeration en = props.propertyNames(); en.hasMoreElements();) {
				String key = (String) en.nextElement();
				Object value = props.getProperty(key);
				if (value == null) {
					// Potentially a non-String value...
					value = props.get(key);
				}
				map.put(key, value);
			}
		}
	}


	/**
	 * Check whether the given Iterator contains the given <b>ELEMENT</b>.
	 * @param iterator the Iterator to check
	 * @param element the element to look for
	 * @return <code>true</code> if found, <code>false</code> else
	 */
	public static boolean contains(Iterator iterator, Object element) {
		if (iterator != null) {
			while (iterator.hasNext()) {
				Object candidate = iterator.next();
				if (Objects.equals(candidate, element)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check whether the given Enumeration contains the given <b>ELEMENT</b>.
	 * @param enumeration the Enumeration to check
	 * @param element the element to look for
	 * @return <code>true</code> if found, <code>false</code> else
	 */
	public static boolean contains(Enumeration enumeration, Object element) {
		if (enumeration != null) {
			while (enumeration.hasMoreElements()) {
				Object candidate = enumeration.nextElement();
				if (Objects.equals(candidate, element)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check whether the given Collection contains the given element instance.
	 * <p>Enforces the given instance to be present, rather than returning
	 * <code>true</code> for an equal element as well.
	 * @param collection the Collection to check
	 * @param element the element to look for
	 * @return <code>true</code> if found, <code>false</code> else
	 */
	public static boolean containsInstance(Collection collection, Object element) {
		if (collection != null) {
			for (Object candidate : collection) {
				if (candidate == element) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean containsElement(Collection collection, Object element) {
		if (collection != null) {
			for (Object candidate : collection) {
				if (Objects.equals(candidate, element)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Check whether the given array contains the given element.
	 * @param array the array to check (may be <code>null</code>,
	 * in which case the return value will always be <code>false</code>)
	 * @param element the element to check for
	 * @return whether the element has been found in the given array
	 */
	public static boolean containsElement(Object[] array, Object element) {
		if (array == null) {
			return false;
		}
		for (Object arrayEle : array) {
			if (Objects.equals(arrayEle, element)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return <code>true</code> if any element in '<code>candidates</code>' is
	 * contained in '<code>source</code>'; otherwise returns <code>false</code>.
	 * @param source the source Collection
	 * @param candidates the candidates to search for
	 * @return whether any of the candidates has been found
	 */
	public static boolean containsAny(Collection source, Collection candidates) {
		if (isEmpty(source) || isEmpty(candidates)) {
			return false;
		}
		for (Object candidate : candidates) {
			if (source.contains(candidate)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return the first element in '<code>candidates</code>' that is contained in
	 * '<code>source</code>'. If no element in '<code>candidates</code>' is present in
	 * '<code>source</code>' returns <code>null</code>. Iteration order is
	 * {@link Collection} implementation specific.
	 * @param source the source Collection
	 * @param candidates the candidates to search for
	 * @return the first present object, or <code>null</code> if not found
	 */
	public static Object findFirstMatch(Collection source, Collection candidates) {
		if (isEmpty(source) || isEmpty(candidates)) {
			return null;
		}
		for (Object candidate : candidates) {
			if (source.contains(candidate)) {
				return candidate;
			}
		}
		return null;
	}

	/**
	 * Find a single value of the given type in the given Collection.
	 * @param collection the Collection to search
	 * @param type the type to look for
	 * @return a value of the given type found if there is a clear match,
	 * or <code>null</code> if none or more than one such value found
	 */
	@SuppressWarnings("unchecked")
	public static <T> T findValueOfType(Collection<?> collection, Class<T> type) {
		if (isEmpty(collection)) {
			return null;
		}
		T value = null;
		for (Object element : collection) {
			if (type == null || type.isInstance(element)) {
				if (value != null) {
					// More than one value found... no clear single value.
					return null;
				}
				value = (T) element;
			}
		}
		return value;
	}

	/**
	 * Find a single value of one of the given types in the given Collection:
	 * searching the Collection for a value of the first type, then
	 * searching for a value of the second type, etc.
	 * @param collection the collection to search
	 * @param types the types to look for, in prioritized order
	 * @return a value of one of the given types found if there is a clear match,
	 * or <code>null</code> if none or more than one such value found
	 */
	public static Object findValueOfType(Collection<?> collection, Class<?>...types) {
		if (isEmpty(collection) || isEmpty(types)) {
			return null;
		}
		for (Class<?> type : types) {
			Object value = findValueOfType(collection, type);
			if (value != null) {
				return value;
			}
		}
		return null;
	}

	/**
	 * Determine whether the given Collection only contains a single unique object.
	 * @param collection the Collection to check
	 * @return <code>false</code> if the collection contains multiple references 
	 * to the same instance, <code>true</code> else
	 */
	public static boolean hasUniqueObject(Collection collection) {
		if (isEmpty(collection)) {
			return true;
		}
		for (Object elem : collection) {
			int count = 0;
			for(Object ele : collection) {
				if(elem == ele) {
					count++;
				}
			}
			if(count > 1) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Determine whether the given Collection only contains a single unique object 
	 * by <code>equals()</code> method.
	 * @param collection the Collection to check
	 * @return <code>false</code> if the collection contains multiple equals Object 
	 * by their <code>equals()</code>, <code>true</code> else
	 */
	public static boolean hasUniqueEqualsObject(Collection collection) {
		if (isEmpty(collection)) {
			return true;
		}
		for (Object elem : collection) {
			int count = 0;
			for(Object el : collection) {
				if(Objects.equals(elem, el)) {
					count++;
				}
			}
			if(count > 1) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Remove duplicated elements in the collection, by using <code>equals()</code>
	 * <p>
	 * <font color="red">!!important!! element must override equals() and hashCode()</font>
	 * 
	 * @param collection
	 */
	@SuppressWarnings("unchecked")
	public static void unique(final Collection collection) {
		
		Set set = new HashSet(collection);
		collection.clear();
		for(Object elem : set) {
			collection.add(elem);
		}
	}
	
	/**
	 * Adapts an array to an iterator
	 * 
	 * @param <E>
	 * @param array
	 * @return
	 */
	public static <E> Iterator<E> toIterator(E[] array) {
		return new ArrayIterator<E>(array);
	}

	/**
	 * Adapts an enumeration to an iterator.
	 * @param enumeration the enumeration
	 * @return the iterator
	 */
	public static <E> Iterator<E> toIterator(Enumeration<E> enumeration) {
		return new EnumerationIterator<E>(enumeration);
	}
	
	/**
	 * Adapts an enumeration to an iterator.
	 * @param enumeration the enumeration
	 * @return the iterator
	 */
	public static <E> Iterable<E> toIteratable(Enumeration<E> enumeration) {
		return new EnumerationIterable<E>(enumeration);
	}
	
	/**
	 * Adapts an enumeration to an iterator.
	 * @param enumeration the enumeration
	 * @return the iterator
	 */
	public static <E> Enumeration<E> toEnumeration(Iterator<E> iterator) {
		return new IteratorEnumeration<E>(iterator);
	}
	
	/**
	 * Adapts an enumeration to an iterator.
	 * @param enumeration the enumeration
	 * @return the iterator
	 */
	public static <E> Enumeration<E> toEnumeration(Iterable<E> iterable) {
		return new IteratorEnumeration<E>(iterable);
	}
	
	/**
	 * Copy the given Collection into a String array.
	 * The Collection must contain String elements only.
	 * @param collection the Collection to copy
	 * @return the String array (<code>null</code> if the passed-in
	 * Collection was <code>null</code>)
	 */
	public static String[] toStringArray(Collection<String> collection) {
		if (collection == null) {
			return null;
		}
		return collection.toArray(new String[collection.size()]);
	}

	/**
	 * Copy the given Enumeration into a String array.
	 * The Enumeration must contain String elements only.
	 * @param enumeration the Enumeration to copy
	 * @return the String array (<code>null</code> if the passed-in
	 * Enumeration was <code>null</code>)
	 */
	public static String[] toStringArray(Enumeration<String> enumeration) {
		if (enumeration == null) {
			return null;
		}
		List<String> list = Collections.list(enumeration);
		return list.toArray(new String[list.size()]);
	}
	
	/**
	 * A bridge/adaptor of <code>Enumeration</code> => <code>Iterator</code>
	 * 
	 * @since fundus
	 * @version 
	 *
	 * @author <a hre<a href="mailto:richd.yang@gmail.com">Richard Yang</a>
	 */ 
	public static class EnumerationIterator<E> implements Iterator<E> {

		private Enumeration<E> enumeration;
		
		public EnumerationIterator(Enumeration<E> enumeration) {
			this.enumeration = enumeration;
		}
		
		public boolean hasNext() {

			return enumeration.hasMoreElements();
		}

		public E next() {
			
			return enumeration.nextElement();
		}

		public void remove() {
			throw new UnsupportedOperationException("Not support 'remove' operation");
			//Just do noting, an exception threw
		}
	}
	
	/**
	 * A bridge/adaptor of <code>Enumeration</code> => <code>Iterable</code>
	 * 
	 * @since fundus
	 * @version 
	 *
	 * @author <a href="mai<a href="mailto:richd.yang@gmail.com">Richard Yang</a>
	 */ 
	public static class EnumerationIterable<E> implements Iterable<E> {

		private Enumeration<E> enumeration;
		
		public EnumerationIterable(Enumeration<E> enumeration) {
			this.enumeration = enumeration;
		}

		public Iterator<E> iterator() {
			return new EnumerationIterator<E>(enumeration);
		}
	}
	
	/**
	 * A bridge/adaptor of <code>Iterable/Iterator</code> => <code>Enumeration</code> 
	 * <p>
	 * For the bridge of <code>Enumeration</code> => <code>Iterable/Iterator</code>, you can:
	 * <pre>
	 * Enumeration enumeration = Collections.enumeration(c);
	 * //Collection is of Iterable
	 * </pre>
	 * @since fundus
	 * @version 
	 * @see EnumerationIterable
	 * @see EnumerationIterator
	 *
	 * @author <a href="mailto:jo<a href="mailto:richd.yang@gmail.com">Richard Yang</a>
	 */ 
	public static class IteratorEnumeration<E> implements Enumeration<E> {
		private Iterator<E> it;

		public IteratorEnumeration(Iterator<E> it) {
			this.it = it;
		}

		public IteratorEnumeration(Iterable<E> it) {
			this.it = it.iterator();
		}

		public boolean hasMoreElements() {
			return it.hasNext();
		}

		public E nextElement() {
			return it.next();
		}
	}
	
	/**
	 * 
	 * @since fundus
	 * @version $Revision: 1.0 $Date:2010-2-25 下午03:24:25 $
	 * 
	 * @author <a href="mailto:josh.yoa<a href="mailto:richd.yang@gmail.com">Richard Yang</a>
	 */ 
	public static class ArrayIterator<E> implements Iterator<E> {

		/**
		 * Array being converted to iterator.
		 */
		private E[] array;

		/**
		 * Current index into the array.
		 */
		private int index = 0;

		/**
		 * Whether the last element has been removed.
		 */
		private boolean lastRemoved = false;

		/**
		 * Create an Iterator from an Array.
		 *
		 * @param array of objects on which to enumerate.
		 */
		public ArrayIterator(E[] array){
			this(array, 0);
		}
		
		public ArrayIterator(E[] array, int index){
			ArgumentAssert.notNull(array);
			this.array = array;
			this.index = index;
		}

		/**
		 * Tests if this Iterator contains more elements.
		 *
		 * @return true if and only if this Iterator object contains at least
		 * one more element to provide; false otherwise.
		 */
		public boolean hasNext(){
			return (index < array.length);
		}

		/**
		 * Returns the next element of this Iterator if this Iterator
		 * object has at least one more element to provide.
		 *
		 * @return the next element of this Iterator.
		 * @throws NoSuchElementException if no more elements exist.
		 */
		public E next() throws NoSuchElementException {
			if (index >= array.length) throw new NoSuchElementException("Array index: " + index);
			E object = array[index];
			index++;
			lastRemoved = false;
			return object;
		}

		/**
		 * Removes the last object from the array by setting the slot in
		 * the array to null.
		 * This method can be called only once per call to next.
		 *
		 * @throws IllegalStateException if the next method has not yet been called, or the remove method has already been called after the last call to the next method.
		 */
		public void remove(){
			if (index == 0) throw new IllegalStateException();
			if (lastRemoved) throw new IllegalStateException();
			array[index-1] = null;
			lastRemoved = true;
		}
	}
}
