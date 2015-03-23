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

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Map;

import org.inframesh.fundus.base.text.Texts;

/**
 * The equivlent of {@link Assert}, but throws {@link IllegalArgumentException}
 * instead
 * 
 * @since fundus
 * @version $Revision: 1.15 $Date:2010-1-26 07:42:51 $
 * 
 * @author <a href="mailto:richd.yang@gmail.com">Richard Yang</a>
 */
public final class ArgumentAssert {

	private ArgumentAssert() {
	}

	private final static String ASSERTION_FAILED = "[Actual Parameter assertion failed] - ";

	/**
	 * Assert a boolean expression, throwing
	 * <code>IllegalArgumentException</code> if the test result is
	 * <code>false</code>.
	 * 
	 * <pre class="code">
	 * Assert.isTrue(i &gt; 0, &quot;The value must be greater than zero&quot;);
	 * </pre>
	 * 
	 * @param expression
	 *            a boolean expression
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws IllegalArgumentException
	 *             if expression is <code>false</code>
	 */
	public static void isTrue(boolean expression, String message, Object... args) {
		if (!expression) {
			throw new IllegalArgumentException(MessageFormat.format(message,
					args));
		}
	}

	/**
	 * Assert a boolean expression, throwing <code>IllegalArgumentException</code>
	 * if the test result is <code>false</code>.
	 * 
	 * <pre class="code">
	 * Assert.isTrue(i &gt; 0);
	 * </pre>
	 * 
	 * @param expression
	 *            a boolean expression
	 * @throws IllegalArgumentException
	 *             if expression is <code>false</code>
	 */
	public static void isTrue(boolean expression) {
		isTrue(expression, ASSERTION_FAILED + "this expression must be true");
	}

	/**
	 * Assert that an object is <code>null</code> .
	 * 
	 * <pre class="code">
	 * Assert.isNull(value, &quot;The value must be null&quot;);
	 * </pre>
	 * 
	 * @param object
	 *            the object to check
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws IllegalArgumentException
	 *             if the object is not <code>null</code>
	 */
	public static void isNull(Object object, String message, Object... args) {
		if (object != null) {
			throw new IllegalArgumentException(MessageFormat.format(message, args));
		}
	}

	/**
	 * Assert that an object is <code>null</code> .
	 * 
	 * <pre class="code">
	 * Assert.isNull(value);
	 * </pre>
	 * 
	 * @param object
	 *            the object to check
	 * @throws IllegalArgumentException
	 *             if the object is not <code>null</code>
	 */
	public static void isNull(Object object) {
		isNull(object, ASSERTION_FAILED + "the object argument must be null");
	}

	/**
	 * Assert that an object is not <code>null</code> .
	 * 
	 * <pre class="code">
	 * Assert.notNull(clazz, &quot;The class must not be null&quot;);
	 * </pre>
	 * 
	 * @param object
	 *            the object to check
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws IllegalArgumentException
	 *             if the object is <code>null</code>
	 */
	public static <T> T notNull(T object, String message, Object... args) {
		if (object == null) {
			throw new IllegalArgumentException(MessageFormat.format(message, args));
		}

		return object;
	}

	/**
	 * Assert that an object is not <code>null</code> .
	 * 
	 * <pre class="code">
	 * Assert.notNull(clazz);
	 * </pre>
	 * 
	 * @param object
	 *            the object to check
	 * @throws IllegalArgumentException
	 *             if the object is <code>null</code>
	 */
	public static <T> T notNull(T object) {
		return notNull(object, ASSERTION_FAILED
				+ "this argument is required; it must not be null");
	}

	/**
	 * Assert that the given String is not empty; that is, it must not be
	 * <code>null</code> and not the empty String.
	 * 
	 * <pre class="code">
	 * Assert.hasLength(name, &quot;Name must not be empty&quot;);
	 * </pre>
	 * 
	 * @param text
	 *            the String to check
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @see Texts#hasLength
	 */
	public static void hasLength(String text, String message, Object... args) {
		if (!Texts.hasLength(text)) {
			throw new IllegalArgumentException(MessageFormat.format(message, args));
		}
	}

	/**
	 * Assert that the given String is not empty; that is, it must not be
	 * <code>null</code> and not the empty String.
	 * 
	 * <pre class="code">
	 * Assert.hasLength(name);
	 * </pre>
	 * 
	 * @param text
	 *            the String to check
	 * @see Texts#hasLength
	 */
	public static void hasLength(String text) {
		hasLength(text, ASSERTION_FAILED + "this String argument must have length; it must not be null or empty");
	}

	/**
	 * Assert that the given String has valid text content; that is, it must not
	 * be <code>null</code> and must contain at least one non-whitespace
	 * character.
	 * 
	 * <pre class="code">
	 * Assert.hasText(name, &quot;'name' must not be empty&quot;);
	 * </pre>
	 * 
	 * @param text
	 *            the String to check
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @see Texts#hasText
	 */
	public static void hasText(String text, String message, Object... args) {
		if (!Texts.hasText(text)) {
			throw new IllegalArgumentException(MessageFormat.format(message, args));
		}
	}

	/**
	 * Assert that the given String has valid text content; that is, it must not
	 * be <code>null</code> and must contain at least one non-whitespace
	 * character.
	 * 
	 * <pre class="code">
	 * Assert.hasText(name, &quot;'name' must not be empty&quot;);
	 * </pre>
	 * 
	 * @param text
	 *            the String to check
	 * @see StringUtils#hasText
	 */
	public static void hasText(String text) {
		hasText(text, ASSERTION_FAILED + "this String argument must have text; it must not be null, empty, or blank");
	}

	/**
	 * Assert that an array has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * 
	 * <pre class="code">
	 * Assert.notEmpty(array, &quot;The array must have elements&quot;);
	 * </pre>
	 * 
	 * @param array
	 *            the array to check
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws IllegalArgumentException
	 *             if the object array is <code>null</code> or has no elements
	 */
	public static <T> T[] notEmpty(T[] array, String message, Object... args) {
		if (Containers.isEmpty(array)) {
			throw new IllegalArgumentException(MessageFormat.format(message, args));
		}

		return array;
	}

	/**
	 * Assert that an array has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * 
	 * <pre class="code">
	 * Assert.notEmpty(array);
	 * </pre>
	 * 
	 * @param array
	 *            the array to check
	 * @throws IllegalArgumentException
	 *             if the object array is <code>null</code> or has no elements
	 */
	public static <T> T[] notEmpty(T[] array) {
		return notEmpty(array, ASSERTION_FAILED	+ "this array must not be empty: it must contain at least 1 element");
	}

	/**
	 * Assert that an array has no null elements. Note: Does not complain if the
	 * array is empty!
	 * 
	 * <pre class="code">
	 * Assert.noNullElements(array, &quot;The array must have non-null elements&quot;);
	 * </pre>
	 * 
	 * @param array
	 *            the array to check
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws IllegalArgumentException
	 *             if the object array contains a <code>null</code> element
	 */
	public static <T> T[] noNullElements(T[] array, String message,
			Object... args) {
		if (Containers.hasNullElements(array)) {
			throw new IllegalArgumentException(MessageFormat.format(message,
					args));
		}

		return array;
	}

	/**
	 * Assert that an array has no null elements. Note: Does not complain if the
	 * array is empty!
	 * 
	 * <pre class="code">
	 * Assert.noNullElements(array);
	 * </pre>
	 * 
	 * @param array
	 *            the array to check
	 * @throws IllegalArgumentException
	 *             if the object array contains a <code>null</code> element
	 */
	public static <T> T[] noNullElements(T[] array) {
		return noNullElements(array, ASSERTION_FAILED
				+ "this array must not contain any null elements");
	}

	/**
	 * Assert that a collection has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * 
	 * <pre class="code">
	 * Assert.notEmpty(collection, &quot;Collection must have elements&quot;);
	 * </pre>
	 * 
	 * @param collection
	 *            the collection to check
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws IllegalArgumentException
	 *             if the collection is <code>null</code> or has no elements
	 */
	public static Collection<?> notEmpty(Collection<?> collection,
			String message, Object... args) {
		if (Containers.isEmpty(collection)) {
			throw new IllegalArgumentException(MessageFormat.format(message,
					args));
		}

		return collection;
	}

	/**
	 * Assert that a collection has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * 
	 * <pre class="code">
	 * Assert.notEmpty(collection, &quot;Collection must have elements&quot;);
	 * </pre>
	 * 
	 * @param collection
	 *            the collection to check
	 * @throws IllegalArgumentException
	 *             if the collection is <code>null</code> or has no elements
	 */
	public static Collection<?> notEmpty(Collection<?> collection) {
		return notEmpty(
				collection,
				ASSERTION_FAILED
						+ "this collection must not be empty: it must contain at least 1 element");
	}

	/**
	 * Assert that a Map has entries; that is, it must not be <code>null</code>
	 * and must have at least one entry.
	 * 
	 * <pre class="code">
	 * Assert.notEmpty(map, &quot;Map must have entries&quot;);
	 * </pre>
	 * 
	 * @param map
	 *            the map to check
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws IllegalArgumentException
	 *             if the map is <code>null</code> or has no entries
	 */
	public static Map notEmpty(Map map, String message, Object... args) {
		if (!Containers.isEmpty(map)) {
			throw new IllegalArgumentException(MessageFormat.format(message,
					args));
		}

		return map;
	}

	/**
	 * Assert that a Map has entries; that is, it must not be <code>null</code>
	 * and must have at least one entry.
	 * 
	 * <pre class="code">
	 * Assert.notEmpty(map);
	 * </pre>
	 * 
	 * @param map
	 *            the map to check
	 * @throws IllegalArgumentException
	 *             if the map is <code>null</code> or has no entries
	 */
	public static Map notEmpty(Map map) {
		return notEmpty(
				map,
				ASSERTION_FAILED
						+ "this map must not be empty; it must contain at least one entry");
	}

	/**
	 * Assert that a Dictionary has entries; that is, it must not be
	 * <code>null</code> and must have at least one entry.
	 * 
	 * <pre class="code">
	 * Assert.notEmpty(dictionary, &quot;dictionary must have entries&quot;);
	 * </pre>
	 * 
	 * @param dictionary
	 *            the dictionary to check
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws IllegalArgumentException
	 *             if the map is <code>null</code> or has no entries
	 */
	public static Dictionary notEmpty(Dictionary dictionary, String message,
			Object... args) {
		if (dictionary == null ? true : dictionary.isEmpty()) {
			throw new IllegalArgumentException(MessageFormat.format(message,
					args));
		}

		return dictionary;
	}

	/**
	 * Assert that a Dictionary has entries; that is, it must not be
	 * <code>null</code> and must have at least one entry.
	 * 
	 * <pre class="code">
	 * Assert.notEmpty(dictionary);
	 * </pre>
	 * 
	 * @param dictionary
	 *            the dictionary to check
	 * @throws IllegalArgumentException
	 *             if the map is <code>null</code> or has no entries
	 */
	public static Dictionary notEmpty(Dictionary dictionary) {
		return notEmpty(
				dictionary,
				ASSERTION_FAILED
						+ "this dictionary must not be empty; it must contain at least one entry");
	}

	/**
	 * Assert a boolean expression, throwing <code>IllegalStateException</code>
	 * if the test result is <code>false</code>. Call isTrue if you wish to
	 * throw AssertError on an assertion failure.
	 * 
	 * <pre class="code">
	 * Assert.state(id == null, &quot;The id property must not already be initialized&quot;);
	 * </pre>
	 * 
	 * @param expression
	 *            a boolean expression
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws IllegalStateException
	 *             if expression is <code>false</code>
	 */
	public static void state(boolean expression, String message, Object... args) {
		if (!expression) {
			throw new IllegalStateException(MessageFormat.format(message, args));
		}
	}

	/**
	 * Assert a boolean expression, throwing {@link IllegalStateException} if
	 * the test result is <code>false</code>.
	 * <p>
	 * Call {@link #isTrue(boolean)} if you wish to throw
	 * {@link IllegalArgumentException} on an assertion failure.
	 * 
	 * <pre class="code">
	 * ArgumentAssert.state(id == null);
	 * </pre>
	 * 
	 * @param expression
	 *            a boolean expression
	 * @throws IllegalStateException
	 *             if the supplied expression is <code>false</code>
	 */
	public static void state(boolean expression) {
		state(expression, ASSERTION_FAILED
				+ "this state invariant must be true");
	}

	public static int indexInBounds(int index, int size) {
		// Carefully optimized for execution by hotspot (explanatory comment
		// above)
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException(index + " is out of bounds: ["
					+ size + ")");
		}
		return index;
	}

	public static void indexInBounds(int start, int end, int size) {
		// Carefully optimized for execution by hotspot (explanatory comment
		// above)
		if (start < 0 || end < start || end > size) {
			throw new IndexOutOfBoundsException(start + "-" + end
					+ " is a bad bound for: [" + size + ")");
		}
	}
}
