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
package org.inframesh.fundus.base.text;

import static org.inframesh.fundus.base.ArgumentAssert.notNull;

import java.io.IOException;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * An object which joins pieces of text (specified as an array, {@link Iterable}
 * , varargs or even a {@link Map}) with a separator. It either appends the
 * results to an {@link Appendable} or returns them as a {@link String}.
 * Example:
 * 
 * <pre>
 * @code
 * 
 *   Joiner joiner = Joiner.on("; ").skipNulls();
 *    . . .
 *   return joiner.join("Harry", null, "Ron", "Hermione");}
 * </pre>
 * 
 * This returns the string {@code "Harry; Ron; Hermione"}. Note that all input
 * elements are converted to strings using {@link Object#toString()} before
 * being appended.
 * 
 * <p>
 * If neither {@link #skipNulls()} nor {@link #substituteNulls(String)} is specified,
 * the joining methods will throw {@link NullPointerException} if any given
 * element is null.
 * 
 * <p>
 * <b>Warning: joiner instances are always immutable</b>; a configuration method
 * such as {@code useForNull} has no effect on the instance it is invoked on!
 * You must store and use the new joiner instance returned by the method. This
 * makes joiners thread-safe, and safe to store as {@code static final}
 * constants.
 * 
 * <pre>
 * @code
 * 
 *   // Bad! Do not do this!
 *   Joiner joiner = Joiner.on(',');
 *   joiner.skipNulls(); // does nothing!
 *   return joiner.join("wrong", null, "wrong");}
 * </pre>
 * 
 * @author <a href="mailto:richd.yang@gmail.com">Richard Yang</a>ince fundus
 */
public class Joiner {
	
	public static interface ElementHandler {
		
		Object handle(Object element);
	}

	/**
	 * Returns a joiner which automatically places {@code separator} between
	 * consecutive elements.
	 */
	public static Joiner on(String separator) {
		return new Joiner(separator);
	}
	
	/**
	 * Returns a {@code MapJoiner} using the given key-value separator, and the
	 * same configuration as this {@code Joiner} otherwise.
	 */
	public static MapJoiner on(String separator, String keyValueSeparator) {
		return new MapJoiner(on(separator), notNull(keyValueSeparator));
	}
	
	public static Joiner on(String separator, ElementHandler handler) {
		return new Joiner(separator, handler);
	}

	/**
	 * Returns a joiner which automatically places {@code separator} between
	 * consecutive elements.
	 */
	public static Joiner on(char separator) {
		return new Joiner(String.valueOf(separator));
	}

	private final String separator;

	private Joiner(String separator) {
		this.separator = notNull(separator);
	}
	
	private static final ElementHandler DEFAULT_HANDLER = new ElementHandler() {

		public Object handle(Object element) {

			return element;
		}
	};
	
	private ElementHandler handler = DEFAULT_HANDLER;
	
	private Joiner(String separator, ElementHandler handler) {
		this.separator = notNull(separator);
		this.handler = handler;
	}

	private Joiner(Joiner prototype) {
		this.separator = prototype.separator;
	}

	/**
	 * Appends the string representation of each of {@code parts}, using the
	 * previously configured separator between each, to {@code appendable}.
	 * @throws IOException 
	 */
	protected <A extends Appendable> A appendTo(A appendable, Iterable<?> parts) throws IOException {
		notNull(appendable);
		Iterator<?> iterator = parts.iterator();
		if (iterator.hasNext()) {
			appendable.append(toString(iterator.next()));
			while (iterator.hasNext()) {
				appendable.append(separator);
				appendable.append(toString(handler.handle(iterator.next())));
			}
		}
		return appendable;
	}

	/**
	 * Appends the string representation of each of {@code parts}, using the
	 * previously configured separator between each, to {@code appendable}.
	 */
	protected final <A extends Appendable> A appendTo(A appendable, Object[] parts) throws IOException {
		return appendTo(appendable, Arrays.asList(parts));
	}

	/**
	 * Appends to {@code appendable} the string representation of each of the
	 * remaining arguments.
	 */
	protected final <A extends Appendable> A appendTo(A appendable, Object first, Object second, Object... rest) throws IOException {
		return appendTo(appendable, iterable(first, second, rest));
	}

	/**
	 * Appends the string representation of each of {@code parts}, using the
	 * previously configured separator between each, to {@code builder}.
	 * Identical to {@link #appendTo(Appendable, Iterable)}, except that it does
	 * not throw {@link IOException}.
	 */
	public final StringBuilder appendTo(StringBuilder builder, Iterable<?> parts) {
		try {
			appendTo((Appendable) builder, parts);
		} catch (IOException impossible) {
			throw new AssertionError(impossible);
		}
		return builder;
	}

	/**
	 * Appends the string representation of each of {@code parts}, using the
	 * previously configured separator between each, to {@code builder}.
	 * Identical to {@link #appendTo(Appendable, Iterable)}, except that it does
	 * not throw {@link IOException}.
	 */
	protected final StringBuilder appendTo(StringBuilder builder, Object[] parts) {
		return appendTo(builder, Arrays.asList(parts));
	}

	/**
	 * Appends to {@code builder} the string representation of each of the
	 * remaining arguments. Identical to
	 * {@link #appendTo(Appendable, Object, Object, Object...)}, except that it
	 * does not throw {@link IOException}.
	 */
	protected final StringBuilder appendTo(StringBuilder builder, Object first, Object second, Object... rest) {
		return appendTo(builder, iterable(first, second, rest));
	}

	/**
	 * Returns a string containing the string representation of each of {@code
	 * parts}, using the previously configured separator between each.
	 */
	public final String join(Iterable<?> parts) {
		return appendTo(new StringBuilder(), parts).toString();
	}

	/**
	 * Returns a string containing the string representation of each of {@code
	 * parts}, using the previously configured separator between each.
	 */
	public final String join(Object[] parts) {
		return join(Arrays.asList(parts));
	}

	/**
	 * Returns a string containing the string representation of each argument,
	 * using the previously configured separator between each.
	 */
	public final String join(Object first, Object second, Object... rest) {
		return join(iterable(first, second, rest));
	}

	/**
	 * Returns a joiner with the same behavior as this one, except automatically
	 * substituting {@code nullText} for any provided null elements.
	 */
	public Joiner substituteNulls(final String nullText) {
		notNull(nullText);
		return new Joiner(this) {
			@Override
			CharSequence toString(Object part) {
				return (part == null) ? nullText : Joiner.this.toString(part);
			}

			@Override
			public Joiner substituteNulls(String nullText) {
				notNull(nullText); // weird, just to satisfy NullPointerTester!
				// TODO: fix that?
				throw new UnsupportedOperationException("already specified useForNull");
			}

			@Override
			public Joiner skipNulls() {
				throw new UnsupportedOperationException("already specified useForNull");
			}
		};
	}

	/**
	 * Returns a joiner with the same behavior as this joiner, except
	 * automatically skipping over any provided null elements.
	 */
	public Joiner skipNulls() {
		return new Joiner(this) {
			@Override
			public <A extends Appendable> A appendTo(A appendable, Iterable<?> parts) throws IOException {
				notNull(appendable, "appendable");
				notNull(parts, "parts");
				Iterator<?> iterator = parts.iterator();
				while (iterator.hasNext()) {
					Object part = iterator.next();
					if (part != null) {
						appendable.append(Joiner.this.toString(part));
						break;
					}
				}
				while (iterator.hasNext()) {
					Object part = iterator.next();
					if (part != null) {
						appendable.append(separator);
						appendable.append(Joiner.this.toString(part));
					}
				}
				return appendable;
			}

			@Override
			public Joiner substituteNulls(String nullText) {
				notNull(nullText); // weird, just to satisfy NullPointerTester!
				throw new UnsupportedOperationException("already specified skipNulls");
			}
		};
	}



	/**
	 * An object that joins map entries in the same manner as {@code Joiner}
	 * joins iterables and arrays. Like {@code Joiner}, it is thread-safe and
	 * immutable.
	 */
	public static class MapJoiner {
		private final Joiner joiner;
		private final String keyValueSeparator;

		private MapJoiner(Joiner joiner, String keyValueSeparator) {
			this.joiner = joiner;
			this.keyValueSeparator = keyValueSeparator;
		}

		/**
		 * Appends the string representation of each entry of {@code map}, using
		 * the previously configured separator and key-value separator, to
		 * {@code appendable}.
		 */
		public <A extends Appendable> A appendTo(A appendable, Map<?, ?> map) throws IOException {
			notNull(appendable);
			Iterator<? extends Map.Entry<?, ?>> iterator = map.entrySet().iterator();
			if (iterator.hasNext()) {
				Entry<?, ?> entry = iterator.next();
				appendable.append(joiner.toString(entry.getKey()));
				appendable.append(keyValueSeparator);
				appendable.append(joiner.toString(entry.getValue()));
				while (iterator.hasNext()) {
					appendable.append(joiner.separator);
					Entry<?, ?> e = iterator.next();
					appendable.append(joiner.toString(e.getKey()));
					appendable.append(keyValueSeparator);
					appendable.append(joiner.toString(e.getValue()));
				}
			}
			return appendable;
		}

		/**
		 * Appends the string representation of each entry of {@code map}, using
		 * the previously configured separator and key-value separator, to
		 * {@code builder}. Identical to {@link #appendTo(Appendable, Map)},
		 * except that it does not throw {@link IOException}.
		 */
		public StringBuilder appendTo(StringBuilder builder, Map<?, ?> map) {
			try {
				appendTo((Appendable) builder, map);
			} catch (IOException impossible) {
				throw new AssertionError(impossible);
			}
			return builder;
		}

		/**
		 * Returns a string containing the string representation of each entry
		 * of {@code map}, using the previously configured separator and
		 * key-value separator.
		 */
		public String join(Map<?, ?> map) {
			return appendTo(new StringBuilder(), map).toString();
		}

		/**
		 * Returns a map joiner with the same behavior as this one, except
		 * automatically substituting {@code nullText} for any provided null
		 * keys or values.
		 */
		public MapJoiner substituteNulls(String nullText) {
			return new MapJoiner(joiner.substituteNulls(nullText), keyValueSeparator);
		}
	}

	CharSequence toString(Object part) {
		if(part == null) return "";//fixed
		return (part instanceof CharSequence) ? (CharSequence) part : part.toString();
	}

	private static Iterable<Object> iterable(final Object first, final Object second, final Object[] rest) {
		notNull(rest);
		return new AbstractList<Object>() {
			@Override
			public int size() {
				return rest.length + 2;
			}

			@Override
			public Object get(int index) {
				switch (index) {
				case 0:
					return first;
				case 1:
					return second;
				default:
					return rest[index - 2];
				}
			}
		};
	}
	
//	public static void main(String[] args) {
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("22", "00");
//		map.put("33", null);
//		String result = Joiner.on(", ", "=").join(map);
//		
//		System.out.println(result);
//	}
}
