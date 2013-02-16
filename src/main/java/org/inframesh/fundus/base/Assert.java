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
import java.util.Map;

import org.inframesh.fundus.base.text.Texts;


/**
 * Generally assert, you can use in anywhere, if neccessary
 * 
 * @since fundus
 * @version $Revision: 1.15 $Date:2010-1-26 05:40:06 $
 * 
 * @author <a href="mailto:josh.yoah@gmail.com">杨超</a>
 */ 
public final class Assert {
	
	private Assert() {}
	
	private final static String ASSERTION_FAILED = "[Assertion failed] - ";

	/**
	 * Assert a boolean expression, throwing <code>IllegalArgumentException</code>
	 * if the test result is <code>false</code>.
	 * <pre class="code">Assert.isTrue(i &gt; 0, "The value must be greater than zero");</pre>
	 * @param expression a boolean expression
	 * @param message the exception message to use if the assertion fails
	 * @throws AssertionError if expression is <code>false</code>
	 */
	public static void isTrue(boolean expression, String message, Object...args) {
		if (!expression) {
			throw new AssertionError(MessageFormat.format(message, args));
		}
	}

	/**
	 * Assert a boolean expression, throwing <code>AssertFailedException</code>
	 * if the test result is <code>false</code>.
	 * <pre class="code">Assert.isTrue(i &gt; 0);</pre>
	 * @param expression a boolean expression
	 * @throws AssertionError if expression is <code>false</code>
	 */
	public static void isTrue(boolean expression) {
		isTrue(expression, ASSERTION_FAILED + "this expression must be true");
	}

	/**
	 * Assert that an object is <code>null</code> .
	 * <pre class="code">Assert.isNull(value, "The value must be null");</pre>
	 * @param object the object to check
	 * @param message the exception message to use if the assertion fails
	 * @throws AssertionError if the object is not <code>null</code>
	 */
	public static void isNull(Object object, String message, Object...args) {
		if (object != null) {
			throw new AssertionError(MessageFormat.format(message, args));
		}
	}

	/**
	 * Assert that an object is <code>null</code> .
	 * <pre class="code">Assert.isNull(value);</pre>
	 * @param object the object to check
	 * @throws AssertionError if the object is not <code>null</code>
	 */
	public static void isNull(Object object) {
		isNull(object, ASSERTION_FAILED + "the object argument must be null");
	}

	/**
	 * Assert that an object is not <code>null</code> .
	 * <pre class="code">Assert.notNull(clazz, "The class must not be null");</pre>
	 * @param object the object to check
	 * @param message the exception message to use if the assertion fails
	 * @throws AssertionError if the object is <code>null</code>
	 */
	public static <T> T notNull(T object, String message, Object...args) {
		if (object == null) {
			throw new AssertionError(MessageFormat.format(message, args));
		}
		return object;
	}

	/**
	 * Assert that an object is not <code>null</code> .
	 * <pre class="code">Assert.notNull(clazz);</pre>
	 * @param object the object to check
	 * @throws AssertionError if the object is <code>null</code>
	 */
	public static <T> T notNull(T object) {
		return notNull(object, ASSERTION_FAILED + "this argument is required; it must not be null");
	}

	/**
	 * Assert that the given String is not empty; that is,
	 * it must not be <code>null</code> and not the empty String.
	 * <pre class="code">Assert.hasLength(name, "Name must not be empty");</pre>
	 * @param text the String to check
	 * @param message the exception message to use if the assertion fails
	 * @see StringUtils#hasLength
	 */
	public static void hasLength(String text, String message, Object...args) {
		if (!Texts.hasLength(text)) {
			throw new AssertionError(MessageFormat.format(message, args));
		}
	}

	/**
	 * Assert that the given String is not empty; that is,
	 * it must not be <code>null</code> and not the empty String.
	 * <pre class="code">Assert.hasLength(name);</pre>
	 * @param text the String to check
	 * @see StringUtils#hasLength
	 */
	public static void hasLength(String text) {
		hasLength(text,
				ASSERTION_FAILED + "this String argument must have length; it must not be null or empty");
	}

	/**
	 * Assert that the given String has valid text content; that is, it must not
	 * be <code>null</code> and must contain at least one non-whitespace character.
	 * <pre class="code">Assert.hasText(name, "'name' must not be empty");</pre>
	 * @param text the String to check
	 * @param message the exception message to use if the assertion fails
	 * @see StringUtils#hasText
	 */
	public static void hasText(String text, String message, Object...args) {
		if (!Texts.hasText(text)) {
			throw new AssertionError(MessageFormat.format(message, args));
		}
	}

	/**
	 * Assert that the given String has valid text content; that is, it must not
	 * be <code>null</code> and must contain at least one non-whitespace character.
	 * <pre class="code">Assert.hasText(name, "'name' must not be empty");</pre>
	 * @param text the String to check
	 * @see StringUtils#hasText
	 */
	public static void hasText(String text) {
		hasText(text, ASSERTION_FAILED + "this String argument must have text; it must not be null, empty, or blank");
	}


	/**
	 * Assert that an array has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * <pre class="code">Assert.notEmpty(array, "The array must have elements");</pre>
	 * @param array the array to check
	 * @param message the exception message to use if the assertion fails
	 * @throws AssertionError if the object array is <code>null</code> or has no elements
	 */
	public static <T> T[] notEmpty(T[] array, String message, Object...args) {
		if (Containers.isEmpty(array)) {
			throw new AssertionError(MessageFormat.format(message, args));
		}
		
		return array;
	}

	/**
	 * Assert that an array has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * <pre class="code">Assert.notEmpty(array);</pre>
	 * @param array the array to check
	 * @throws AssertionError if the object array is <code>null</code> or has no elements
	 */
	public static <T> T[] notEmpty(T[] array) {
		return notEmpty(array, ASSERTION_FAILED + "this array must not be empty: it must contain at least 1 element");
	}

	/**
	 * Assert that an array has no null elements.
	 * Note: Does not complain if the array is empty!
	 * <pre class="code">Assert.noNullElements(array, "The array must have non-null elements");</pre>
	 * @param array the array to check
	 * @param message the exception message to use if the assertion fails
	 * @throws AssertionError if the object array contains a <code>null</code> element
	 */
	public static <T> T[] noNullElements(T[] array, String message, Object...args) {
		if (Containers.hasNullElements(array)) {
			throw new AssertionError(MessageFormat.format(message, args));
		}
		
		return array;
	}

	/**
	 * Assert that an array has no null elements.
	 * Note: Does not complain if the array is empty!
	 * <pre class="code">Assert.noNullElements(array);</pre>
	 * @param array the array to check
	 * @throws AssertionError if the object array contains a <code>null</code> element
	 */
	public static <T> T[] noNullElements(T[] array) {
		return noNullElements(array, ASSERTION_FAILED + "this array must not contain any null elements");
	}

	/**
	 * Assert that a collection has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * <pre class="code">Assert.notEmpty(collection, "Collection must have elements");</pre>
	 * @param collection the collection to check
	 * @param message the exception message to use if the assertion fails
	 * @throws AssertionError if the collection is <code>null</code> or has no elements
	 */
	public static Collection notEmpty(Collection collection, String message, Object...args) {
		if (Containers.isEmpty(collection)) {
			throw new AssertionError(MessageFormat.format(message, args));
		}
		
		return collection;
	}

	/**
	 * Assert that a collection has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * <pre class="code">Assert.notEmpty(collection, "Collection must have elements");</pre>
	 * @param collection the collection to check
	 * @throws AssertionError if the collection is <code>null</code> or has no elements
	 */
	public static Collection notEmpty(Collection collection) {
		return notEmpty(collection,
				ASSERTION_FAILED + "this collection must not be empty: it must contain at least 1 element");
	}

	/**
	 * Assert that a Map has entries; that is, it must not be <code>null</code>
	 * and must have at least one entry.
	 * <pre class="code">Assert.notEmpty(map, "Map must have entries");</pre>
	 * @param map the map to check
	 * @param message the exception message to use if the assertion fails
	 * @throws AssertionError if the map is <code>null</code> or has no entries
	 */
	public static Map notEmpty(Map map, String message, Object...args) {
		if (Containers.isEmpty(map)) {
			throw new AssertionError(MessageFormat.format(message, args));
		}
		
		return map;
	}

	/**
	 * Assert that a Map has entries; that is, it must not be <code>null</code>
	 * and must have at least one entry.
	 * <pre class="code">Assert.notEmpty(map);</pre>
	 * @param map the map to check
	 * @throws AssertionError if the map is <code>null</code> or has no entries
	 */
	public static Map notEmpty(Map map) {
		return notEmpty(map, ASSERTION_FAILED + "this map must not be empty; it must contain at least one entry");
	}

	/**
	 * Assert a boolean expression, throwing <code>AssertionError</code>
	 * if the test result is <code>false</code>. Call isTrue if you wish to
	 * throw AssertFailedException on an assertion failure.
	 * <pre class="code">Assert.state(id == null, "The id property must not already be initialized");</pre>
	 * @param expression a boolean expression
	 * @param message the exception message to use if the assertion fails
	 * @throws AssertionError if expression is <code>false</code>
	 */
	public static void state(boolean expression, String message, Object...args) {
		if (!expression) {
			throw new AssertionError(MessageFormat.format(message, args));
		}
	}

	/**
	 * Assert a boolean expression, throwing {@link AssertionError}
	 * if the test result is <code>false</code>.
	 * <p>Call {@link #isTrue(boolean)} if you wish to
	 * throw {@link AssertionError} on an assertion failure.
	 * <pre class="code">Assert.state(id == null);</pre>
	 * @param expression a boolean expression
	 * @throws AssertionError if the supplied expression is <code>false</code>
	 */
	public static void state(boolean expression) {
		state(expression, ASSERTION_FAILED + "this state invariant must be true");
	}
}
