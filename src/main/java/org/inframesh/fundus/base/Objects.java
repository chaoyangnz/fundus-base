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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.inframesh.fundus.base.text.Joiner;


/**
 * Helper functions that can operate on any {@code Object}.
 * 
 * @author <a href="mailto:richd.yang@gmail.com">Richard Yang</a>ince fundus
 */
public final class Objects {
	private Objects() {
	}

	/**
	 * Determines whether two possibly-null objects are equal. Returns:
	 * 
	 * <ul>
	 * <li>{@code true} if {@code a} and {@code b} are both null.
	 * <li>{@code true} if {@code a} and {@code b} are both non-null and they
	 * are equal according to {@link Object#equals(Object)}.
	 * <li>{@code false} in all other situations.
	 * </ul>
	 * 
	 * <p>
	 * This assumes that any non-null objects passed to this function conform to
	 * the {@code equals()} contract.
	 */
	public static boolean equals(Object a, Object b) {
		return a == b || (a != null && a.equals(b));
	}

	/**
	 * Convert the given array (which may be a primitive array) to an
	 * object array (if necessary of primitive wrapper objects).
	 * <p>A <code>null</code> source value will be converted to an
	 * empty Object array.
	 * @param source the (potentially primitive) array
	 * @return the corresponding object array (never <code>null</code>)
	 * @throws IllegalArgumentException if the parameter is not an array
	 */
	public static Object[] toObjectArray(Object source) {
		if (source instanceof Object[]) {
			return (Object[]) source;
		}
		if (source == null) {
			return new Object[0];
		}
		if (!source.getClass().isArray()) {
			throw new IllegalArgumentException("Source is not an array: " + source);
		}
		int length = Array.getLength(source);
		if (length == 0) {
			return new Object[0];
		}
		//array.getClass().getComponentType() can return pritive type, such as int for int
		//but Array.get(array, 0).getClass() must retun wrapped type, such as java.lang.Integer for int
		Class wrapperType = Array.get(source, 0).getClass();
		Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);
		for (int i = 0; i < length; i++) {
			newArray[i] = Array.get(source, i);
		}
		return newArray;
	}
	
	/**
	 * Instantiate a object clone, using Object Serialization<br>
	 * Useful when need value semantics
	 * @param src may be <strong>object</strong>, or <strong>object array</strong>, or <strong>primitive type array</strong>
	 * @return clone object, <code>null</code> if src is <code>null</code>
	 * @throws IOException throws when stream exception
	 * @throws ClassNotFoundException throws when class cannot found for reterive
	 * @see ArrayUtil#clone(Object[])
	 * @see ReflectionUtil#deepCopyFieldState(Object, Object)
	 */
	public static Object clone(final Object src) throws IOException, ClassNotFoundException {
		if(src == null) return null;
		
		if(! Serializable.class.isAssignableFrom(src.getClass())) {
			throw new NotSerializableException(src.getClass().getName());
		}
		
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		InputStream bais = null;
		ObjectInputStream ois = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(src);
			
			bais = new ByteArrayInputStream(baos.toByteArray());
			ois = new ObjectInputStream(bais);
			Object clone = ois.readObject();
			
			return clone;
		}  finally {
			try {
				if (oos != null) {
					oos.close();
					oos = null;
				}
				if (baos != null) {
					baos.close();
					baos = null;
				}
				if (ois != null) {
					ois.close();
					ois = null;
				}
				if (bais != null) {
					bais.close();
					bais = null;
				}
			} catch (IOException e) {
			}
		}
	}
	
//	//-------------------------------------------------
//	// Shallow and deep copy
//	//-------------------------------------------------
//
//	/**
//	 * Given the source object and the destination, which must be the same class
//	 * or a subclass, copy all fields, including inherited fields. Designed to
//	 * work on objects with public no-arg constructors.
//	 * @throws IllegalArgumentException if the arguments are incompatible
//	 */
//	public static void shallowCopyFieldState(final Object src, final Object dest) throws IllegalArgumentException {
//		if (src == null) {
//			throw new IllegalArgumentException("Source for field copy cannot be null");
//		}
//		if (dest == null) {
//			throw new IllegalArgumentException("Destination for field copy cannot be null");
//		}
//		if (!src.getClass().isAssignableFrom(dest.getClass())) {
//			throw new IllegalArgumentException("Destination class [" + dest.getClass().getName()
//					+ "] must be same or subclass as source class [" + src.getClass().getName() + "]");
//		}
//		ClassUtil.traversalFields(src.getClass(), new ArrayElementResolver<Field>() {
////			public void resolve(int index, Object element) {
////				Field field = (Field)element;
////				ReflectionUtil.makeAccessible(field);
////				Object srcValue;
////				try {
////					srcValue = field.get(src);
////					field.set(dest, srcValue);
////				} catch (IllegalAccessException ex) {
////					throw new IllegalStateException(
////					"Shouldn't be illegal to access field '" + field.getName() + "': " + ex);
////				}
////			}
//			
//			public void callback(Iterator<Field> iter) {
//				Field field = iter.next();
//				ReflectionUtil.makeAccessible(field);
//				Object srcValue;
//				try {
//					srcValue = field.get(src);
//					field.set(dest, srcValue);
//				} catch (IllegalAccessException ex) {
//					throw new IllegalStateException(
//					"Shouldn't be illegal to access field '" + field.getName() + "': " + ex);
//				}
//			}
//		}, COPYABLE_FIELDS);
//	}
//	
//	/**
//	 * Using Serializable API to implement deep copy
//	 * @param src
//	 * @param dest
//	 * @throws IOException 
//	 * @throws ClassNotFoundException 
//	 * @see ObjectUtil#clone(Object)
//	 * @see ArrayUtil#clone(Object[])
//	 */
//	public static void deepCopyFieldState(final Object src, final Object dest) throws IOException, ClassNotFoundException {
//		if (src == null) {
//			throw new IllegalArgumentException("Source for field copy cannot be null");
//		}
//		if (dest == null) {
//			throw new IllegalArgumentException("Destination for field copy cannot be null");
//		}
//		if (!src.getClass().isAssignableFrom(dest.getClass())) {
//			throw new IllegalArgumentException("Destination class [" + dest.getClass().getName()
//					+ "] must be same or subclass as source class [" + src.getClass().getName() + "]");
//		}
//
//		Object clone = clone(src);
//		shallowCopyFieldState(clone, dest);
//	}

	/**
	 * Support class for {@link Objects#toStringHelper}.
	 * 
	 * @author <a hre<a href="mailto:richd.yang@gmail.com">Richard Yang</a>fundus
	 */
	public final static class ToStringHelper {
		private final List<String> fieldString = new ArrayList<String>();
		private final Object instance;
		
		private static final String NULL_STRING = "null";

		private static final String ARRAY_START = "[";
		private static final String ARRAY_END = "]";
		private static final String EMPTY_ARRAY = ARRAY_START + ARRAY_END;
		private static final String ARRAY_ELEMENT_SEPARATOR = ", ";

		/**
		 * Use {@link Objects#toStringHelper(Object)} to create an instance.
		 */
		private ToStringHelper(Object instance) {
			this.instance = ArgumentAssert.notNull(instance);
		}

		/**
		 * Adds a name/value pair to the formatted output in {@code name=value}
		 * format. If {@code value} is {@code null}, the string {@code "null"}
		 * is used.
		 */
		public ToStringHelper add(String name, Object value) {
			return addValue(ArgumentAssert.notNull(name) + "=" + value);
		}

		/**
		 * Adds a value to the formatted output in {@code value} format.
		 * <p/>
		 * 
		 * It is strongly encouraged to use {@link #add(String, Object)} instead
		 * and give value a readable name.
		 */
		public ToStringHelper addValue(Object value) {
			fieldString.add(String.valueOf(value));
			return this;
		}

		private static final Joiner JOINER = Joiner.on(", ");

		/**
		 * Returns the formatted string.
		 */
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder(100).append(
					simpleName(instance.getClass())).append('{');
			return JOINER.appendTo(builder, fieldString).append('}').toString();
		}

		/**
		 * {@link Class#getSimpleName()} is not GWT compatible yet, so we
		 * provide our own implementation.
		 */
		static String simpleName(Class<?> clazz) {
			String name = clazz.getName();

			// we want the name of the inner class all by its lonesome
			int start = name.lastIndexOf('$');

			// if this isn't an inner class, just find the start of the
			// top level class name.
			if (start == -1) {
				start = name.lastIndexOf('.');
			}
			return name.substring(start + 1);
		}
		
		/**
		 * Return a String representation of the contents of the specified
		 * array.
		 * <p>
		 * The String representation consists of a list of the array's elements,
		 * enclosed in curly braces (<code>"{}"</code>). Adjacent elements are
		 * separated by the characters <code>", "</code> (a comma followed by a
		 * space). Returns <code>"null"</code> if <code>array</code> is
		 * <code>null</code>.
		 * 
		 * @param array
		 *            the array to build a String representation for
		 * @return a String representation of <code>array</code>
		 * @deprecated
		 * @see Arrays#toString(boolean[])
		 */
		public static String toString(boolean[] array) {
			if (array == null) {
				return NULL_STRING;
			}
			int length = array.length;
			if (length == 0) {
				return EMPTY_ARRAY;
			}
			StringBuilder sb = new StringBuilder();
			sb.append(ARRAY_START);
			for (int i = 0; i < length; i++) {
				if (i != 0) {
					sb.append(ARRAY_ELEMENT_SEPARATOR);
				}
				sb.append(array[i]);
			}
			sb.append(ARRAY_END);
			return sb.toString();
		}

		/**
		 * Return a String representation of the contents of the specified
		 * array.
		 * <p>
		 * The String representation consists of a list of the array's elements,
		 * enclosed in curly braces (<code>"{}"</code>). Adjacent elements are
		 * separated by the characters <code>", "</code> (a comma followed by a
		 * space). Returns <code>"null"</code> if <code>array</code> is
		 * <code>null</code>.
		 * 
		 * @param array
		 *            the array to build a String representation for
		 * @return a String representation of <code>array</code>
		 * @deprecated
		 * @see Arrays#toString(byte[])
		 */
		public static String toString(byte[] array) {
			if (array == null) {
				return NULL_STRING;
			}
			int length = array.length;
			if (length == 0) {
				return EMPTY_ARRAY;
			}
			StringBuilder sb = new StringBuilder();
			sb.append(ARRAY_START);
			for (int i = 0; i < length; i++) {
				if (i != 0) {
					sb.append(ARRAY_ELEMENT_SEPARATOR);
				}
				sb.append(array[i]);
			}
			sb.append(ARRAY_END);
			return sb.toString();
		}

		/**
		 * Return a String representation of the contents of the specified
		 * array.
		 * <p>
		 * The String representation consists of a list of the array's elements,
		 * enclosed in curly braces (<code>"{}"</code>). Adjacent elements are
		 * separated by the characters <code>", "</code> (a comma followed by a
		 * space). Returns <code>"null"</code> if <code>array</code> is
		 * <code>null</code>.
		 * 
		 * @param array
		 *            the array to build a String representation for
		 * @return a String representation of <code>array</code>
		 * @deprecated
		 * @see Arrays#toString(char[])
		 */
		public static String toString(char[] array) {
			if (array == null) {
				return NULL_STRING;
			}
			int length = array.length;
			if (length == 0) {
				return EMPTY_ARRAY;
			}
			StringBuilder sb = new StringBuilder();
			sb.append(ARRAY_START);
			for (int i = 0; i < length; i++) {
				if (i != 0) {
					sb.append(ARRAY_ELEMENT_SEPARATOR);
				}
				sb.append("'").append(array[i]).append("'");
			}
			sb.append(ARRAY_END);
			return sb.toString();
		}

		/**
		 * Return a String representation of the contents of the specified
		 * array.
		 * <p>
		 * The String representation consists of a list of the array's elements,
		 * enclosed in curly braces (<code>"{}"</code>). Adjacent elements are
		 * separated by the characters <code>", "</code> (a comma followed by a
		 * space). Returns <code>"null"</code> if <code>array</code> is
		 * <code>null</code>.
		 * 
		 * @param array
		 *            the array to build a String representation for
		 * @return a String representation of <code>array</code>
		 * @deprecated
		 * @see Arrays#toString(double[])
		 */
		public static String toString(double[] array) {
			if (array == null) {
				return NULL_STRING;
			}
			int length = array.length;
			if (length == 0) {
				return EMPTY_ARRAY;
			}
			StringBuilder sb = new StringBuilder();
			sb.append(ARRAY_START);
			for (int i = 0; i < length; i++) {
				if (i != 0) {
					sb.append(ARRAY_ELEMENT_SEPARATOR);
				}
				sb.append(array[i]);
			}
			sb.append(ARRAY_END);
			return sb.toString();
		}

		/**
		 * Return a String representation of the contents of the specified
		 * array.
		 * <p>
		 * The String representation consists of a list of the array's elements,
		 * enclosed in curly braces (<code>"{}"</code>). Adjacent elements are
		 * separated by the characters <code>", "</code> (a comma followed by a
		 * space). Returns <code>"null"</code> if <code>array</code> is
		 * <code>null</code>.
		 * 
		 * @param array
		 *            the array to build a String representation for
		 * @return a String representation of <code>array</code>
		 * @deprecated
		 * @see Arrays#toString(float[])
		 */
		public static String toString(float[] array) {
			if (array == null) {
				return NULL_STRING;
			}
			int length = array.length;
			if (length == 0) {
				return EMPTY_ARRAY;
			}
			StringBuilder sb = new StringBuilder();
			sb.append(ARRAY_START);
			for (int i = 0; i < length; i++) {
				if (i != 0) {
					sb.append(ARRAY_ELEMENT_SEPARATOR);
				}
				sb.append(array[i]);
			}
			sb.append(ARRAY_END);
			return sb.toString();
		}

		/**
		 * Return a String representation of the contents of the specified
		 * array.
		 * <p>
		 * The String representation consists of a list of the array's elements,
		 * enclosed in curly braces (<code>"{}"</code>). Adjacent elements are
		 * separated by the characters <code>", "</code> (a comma followed by a
		 * space). Returns <code>"null"</code> if <code>array</code> is
		 * <code>null</code>.
		 * 
		 * @param array
		 *            the array to build a String representation for
		 * @return a String representation of <code>array</code>
		 * @deprecated
		 * @see Arrays#toString(int[])
		 */
		public static String toString(int[] array) {
			if (array == null) {
				return NULL_STRING;
			}
			int length = array.length;
			if (length == 0) {
				return EMPTY_ARRAY;
			}
			StringBuilder sb = new StringBuilder();
			sb.append(ARRAY_START);
			for (int i = 0; i < length; i++) {
				if (i != 0) {
					sb.append(ARRAY_ELEMENT_SEPARATOR);
				}
				sb.append(array[i]);
			}
			sb.append(ARRAY_END);
			return sb.toString();
		}

		/**
		 * Return a String representation of the contents of the specified
		 * array.
		 * <p>
		 * The String representation consists of a list of the array's elements,
		 * enclosed in curly braces (<code>"{}"</code>). Adjacent elements are
		 * separated by the characters <code>", "</code> (a comma followed by a
		 * space). Returns <code>"null"</code> if <code>array</code> is
		 * <code>null</code>.
		 * 
		 * @param array
		 *            the array to build a String representation for
		 * @return a String representation of <code>array</code>
		 * @deprecated
		 * @see Arrays#toString(long[])
		 */
		public static String toString(long[] array) {
			if (array == null) {
				return NULL_STRING;
			}
			int length = array.length;
			if (length == 0) {
				return EMPTY_ARRAY;
			}
			StringBuilder sb = new StringBuilder();
			sb.append(ARRAY_START);
			for (int i = 0; i < length; i++) {
				if (i != 0) {
					sb.append(ARRAY_ELEMENT_SEPARATOR);
				}
				sb.append(array[i]);
			}
			sb.append(ARRAY_END);
			return sb.toString();
		}

		/**
		 * Return a String representation of the contents of the specified
		 * array.
		 * <p>
		 * The String representation consists of a list of the array's elements,
		 * enclosed in curly braces (<code>"{}"</code>). Adjacent elements are
		 * separated by the characters <code>", "</code> (a comma followed by a
		 * space). Returns <code>"null"</code> if <code>array</code> is
		 * <code>null</code>.
		 * 
		 * @param array
		 *            the array to build a String representation for
		 * @return a String representation of <code>array</code>
		 * @deprecated
		 * @see Arrays#toString(short[])
		 */
		public static String toString(short[] array) {
			if (array == null) {
				return NULL_STRING;
			}
			int length = array.length;
			if (length == 0) {
				return EMPTY_ARRAY;
			}
			StringBuilder sb = new StringBuilder();
			sb.append(ARRAY_START);
			for (int i = 0; i < length; i++) {
				if (i != 0) {
					sb.append(ARRAY_ELEMENT_SEPARATOR);
				}
				sb.append(array[i]);
			}
			sb.append(ARRAY_END);
			return sb.toString();
		}
	}
	
	/**
	 * 
	 * 
	 * @since fundus-base
	 * @version 
	 *
	 * @author <a href="mailto:josh.yoah@gmail.com">杨超 </a>
	 */
	public final static class HashCodeHelper {
		
		private HashCodeHelper() {}
		
		/**
		 * Generates a hash code for multiple values. The hash code is generated by
		 * calling {@link Arrays#hashCode(Object[])}.
		 * 
		 * <p>
		 * This is useful for implementing {@link Object#hashCode()}. For example,
		 * in an object that has three properties, {@code x}, {@code y}, and {@code
		 * z}, one could write:
		 * 
		 * <pre>
		 * public int hashCode() {
		 * 	return Objects.hashCode(getX(), getY(), getZ());
		 * }
		 * </pre>
		 * 
		 * <b>Warning</b>: When a single object is supplied, the returned hash code
		 * does not equal the hash code of that object.
		 * @deprecated
		 * @see Arrays#hashCode()
		 */
		public static int hashCode(Object... objects) {
			return Arrays.hashCode(objects);
		}
		
		/**
		 * Returns a hash code for {@code value}; equal to the result of invoking
		 * {@code ((Boolean) value).hashCode()}.
		 * 
		 * @param value
		 *            a primitive {@code boolean} value
		 * @return a hash code for the value
		 */
		public static int hashCode(boolean value) {
			return value ? 1231 : 1237;
		}
		
		/**
		 * Returns a hash code for {@code value}; equal to the result of invoking
		 * {@code ((Byte) value).hashCode()}.
		 * 
		 * @param value
		 *            a primitive {@code byte} value
		 * @return a hash code for the value
		 */
		public static int hashCode(byte value) {
			return value;
		}
		
		/**
		 * Returns a hash code for {@code value}; equal to the result of invoking
		 * {@code ((Character) value).hashCode()}.
		 * 
		 * @param value
		 *            a primitive {@code char} value
		 * @return a hash code for the value
		 */
		public static int hashCode(char value) {
			return value;
		}
		
		/**
		 * Returns a hash code for {@code value}; equal to the result of invoking
		 * {@code ((Double) value).hashCode()}.
		 * 
		 * @param value
		 *            a primitive {@code double} value
		 * @return a hash code for the value
		 */
		public static int hashCode(double value) {
			long bits = Double.doubleToLongBits(value);
			return hashCode(bits);
		}
		
		/**
		 * Returns a hash code for {@code value}; equal to the result of invoking
		 * {@code ((Float) value).hashCode()}.
		 * 
		 * @param value
		 *            a primitive {@code float} value
		 * @return a hash code for the value
		 */
		public static int hashCode(float value) {
			return Float.floatToIntBits(value);
		}
		
		/**
		 * Returns a hash code for {@code value}; equal to the result of invoking
		 * {@code ((Integer) value).hashCode()}.
		 * 
		 * @param value
		 *            a primitive {@code int} value
		 * @return a hash code for the value
		 */
		public static int hashCode(int value) {
			return value;
		}
		
		/**
		 * Returns a hash code for {@code value}; equal to the result of invoking
		 * {@code ((Long) value).hashCode()}.
		 * 
		 * @param value
		 *            a primitive {@code long} value
		 * @return a hash code for the value
		 */
		public static int hashCode(long value) {
			return (int) (value ^ (value >>> 32));
		}
		
		/**
		 * Returns a hash code for {@code value}; equal to the result of invoking
		 * {@code ((Short) value).hashCode()}.
		 * 
		 * @param value
		 *            a primitive {@code short} value
		 * @return a hash code for the value
		 */
		public static int hashCode(short value) {
			return value;
		}
	}
}
