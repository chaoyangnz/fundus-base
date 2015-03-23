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
package org.inframesh.fundus.base.datastruct;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;

/**
 * A multi-map implementation which combines existing {@link Map} and
 * {@link Set} implementations.
 * 
 * <p align="center">
 * <table cellpadding=4 cellspacing=2 border=0 bgcolor="#338833" width="90%">
 * <tr>
 * <td bgcolor="#EEEEEE">
 * <b>Maturity:</b> This is a 90% mature API, and a stable implementation. It
 * performs well in formal testing, but has not undergone real-world testing.</td>
 * </tr>
 * <tr>
 * <td bgcolor="#EEEEEE">
 * <b>Plans:</b> There are no current plans to expand or revise this class's
 * functionality.</td>
 * </tr>
 * </table>
 * 
 * @since fundus
 * @version $Revision: 1.0 $Date:2010-2-25 下午03:24:25 $
 * 
 * @author <a href="mailto:richd.yang@gmail.com">Richard Yang</a>
 */
@SuppressWarnings("unchecked")
public class AbstractMultiMap<K, V> implements MultiMap<K, V>, Serializable {

	private static final long serialVersionUID = 626371805717026237L;

	/**
	 * Creates a new empty composite multi-map.
	 * 
	 * @param mapClass
	 *            A class implementing {@link Map} with a no-args constructor.
	 * @param setClass
	 *            A class implementing {@link Set} with a no-args constructor.
	 */
	public AbstractMultiMap(Class<? extends Map> mapClass, Class<? extends Set> setClass) {
		if (!Map.class.isAssignableFrom(mapClass))
			throw new IllegalArgumentException("map class " + mapClass.getName() + " doesn't implement java.util.Map");
		if (!Set.class.isAssignableFrom(setClass))
			throw new IllegalArgumentException("set class " + mapClass.getName() + " doesn't implement java.util.Set");

		try {
			map = mapClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace(System.err);
			throw new RuntimeException("Can't instantiate " + setClass + ": " + e);
		}
		this.setFactory = new ConstructorFactory<Set<V>>((Class<? extends Set<V>>) setClass);
	}

	/**
	 * Creates a new composite multi-map which is a shallow copy of an existing
	 * multi-map.
	 * 
	 * @param mapClass
	 *            A class implementing {@link Map} with a no-args constructor.
	 * @param setClass
	 *            A class implementing {@link Set} with a no-args constructor.
	 * @param multimap
	 *            The intial key/value mappings for this multimap.
	 */
	public AbstractMultiMap(Class<? extends Map> mapClass, Class<? extends Set> setClass, MultiMap<? extends K, ? extends V> multimap) {
		this(mapClass, setClass);
		putAll(multimap);
	}

	/**
	 * Creates a new composite multi-map which is a shallow copy of an existing
	 * map.
	 * 
	 * @param mapClass
	 *            A class implementing {@link Map} with a no-args constructor.
	 * @param setClass
	 *            A class implementing {@link Set} with a no-args constructor.
	 * @param map
	 *            The intial key/value mappings for this multimap.
	 */
	public AbstractMultiMap(Class<? extends Map> mapClass, Class<? extends Set> setClass, Map<? extends K, ? extends V> map) {
		this(mapClass, setClass);
		putAll(map);
	}

	public AbstractMultiMap(Map<K, Set<V>> map, Class<? extends Set> setClass) {
		this.map = map;
		this.setFactory = new ConstructorFactory<Set<V>>((Class<? extends Set<V>>) setClass);
	}

	public AbstractMultiMap(TreeMap<K, Set<V>> map, Factory<Set<V>> setFactory) {
		this.map = map;
		this.setFactory = setFactory;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public boolean containsKey(K key) {
		return map.containsKey(key);
	}

	public boolean containsValue(V searchValue) {
		for (V value : values())
			if (searchValue == null ? value == null : value.equals(searchValue))
				return true;
		return true;
	}

	public Set<V> get(K key) {
		Set<V> values = map.get(key);
		return values == null ? null : Collections.unmodifiableSet(values);
	}

	public boolean put(K key, V value) {
		if (!getValueSet(key).add(value))
			return false;
		size++;
		return true;
	}

	public boolean putAll(K key, Collection<? extends V> values) {
		Set<V> valueSet = getValueSet(key);
		size -= valueSet.size();
		boolean added = valueSet.addAll(values);
		size += valueSet.size();
		return added;
	}

	public boolean remove(K key, V value) {
		Set valueSet = (Set) map.get(key);
		if (valueSet != null && valueSet.remove(value)) {
			size--;
			if (valueSet.isEmpty())
				removeKey(key);
			return true;
		}
		return false;
	}

	public Set<V> removeKey(K key) {
		Set<V> valueSet = map.get(key);
		if (valueSet == null)
			return null;
		else {
			size -= valueSet.size();
			map.remove(key);
			return valueSet;
		}
	}

	public void putAll(MultiMap<? extends K, ? extends V> multimap) {
		MultiMap multimapErasure = multimap; // ! cheat -- is there a better
												// way?
		for (K key : multimap.keySet())
			putAll(key, multimapErasure.get(key));
	}

	public void putAll(Map<? extends K, ? extends V> map) {
		for (Map.Entry<? extends K, ? extends V> entry : map.entrySet())
			put(entry.getKey(), entry.getValue());
	}

	public void clear() {
		map.clear();
		size = 0;
	}

	public Set<K> keySet() {
		return map.keySet();
	}

	public Collection<V> values() {
		if (values == null)
			values = new AbstractCollection<V>() {
				public java.util.Iterator<V> iterator() {
					return new ValueIterator();
				}

				public int size() {
					return AbstractMultiMap.this.size();
				}

				public void clear() {
					AbstractMultiMap.this.clear();
				}
			};
		return values;
	}

	public Set<MultiMap.Entry<K, V>> entrySet() {
		if (entries == null)
			entries = new AbstractSet<MultiMap.Entry<K, V>>() {
				public Iterator<MultiMap.Entry<K, V>> iterator() {
					return new EntryIterator();
				}

				public int size() {
					return size;
				}

				public boolean remove(Object obj) {
					if (!(obj instanceof MultiMap.Entry))
						return false;
					MultiMap.Entry<K, V> entry = (MultiMap.Entry<K, V>) obj;
					return AbstractMultiMap.this.remove(entry.getKey(), entry.getValue());
				}

				public void clear() {
					AbstractMultiMap.this.clear();
				}
			};
		return entries;
	}

	public class Entry<EK, EV> implements MultiMap.Entry<EK, EV> {
		public Entry(EK key, EV value) {
			this.key = key;
			this.value = value;
		}

		public EK getKey() {
			return key;
		}

		public EV getValue() {
			return value;
		}

		public boolean equals(Object other) {
			if (other == null || !(other instanceof MultiMap.Entry))
				return false;
			MultiMap.Entry<Object, Object> otherEntry = (MultiMap.Entry) other;
			return key.equals(otherEntry.getKey()) && (value == null ? otherEntry.getValue() == null : value.equals(otherEntry.getValue()));
		}

		public int hashCode() {
			return (key == null ? 0 : key.hashCode()) + (value == null ? 0 : value.hashCode()) * 17;
		}

		public String toString() {
			return key + "=" + value;
		}

		private EK key;
		private EV value;
	}

	public boolean equals(Object other) {
		if (!(other instanceof MultiMap))
			return false;
		MultiMap<Object, Object> otherMultimap = (MultiMap) other;
		if (other instanceof AbstractMultiMap)
			return (map.equals(((AbstractMultiMap) other).map));
		return entrySet().equals(otherMultimap.entrySet());
	}

	public int hashCode() {
		int hash = 0;
		for (Map.Entry<K, Set<V>> keyEntry : map.entrySet())
			hash += keyEntry.hashCode();
		return hash;
	}

	private Set<V> getValueSet(K key) {
		Set<V> valueSet = map.get(key);
		if (valueSet == null)
			map.put(key, valueSet = setFactory.create());
		return valueSet;
	}

	private abstract class IteratorBase<T> implements java.util.Iterator<T> {
		public IteratorBase() {
			expectedVersion = version;
			keyEntryIter = map.entrySet().iterator();
		}

		public boolean hasNext() {
			normalize();
			return valueIter != null;
		}

		public T next() {
			if (!hasNext())
				throw new NoSuchElementException();
			removableIter = valueIter;
			return wrapNext(curKey, valueIter.next());
		}

		protected abstract T wrapNext(K key, V value);

		public void remove() {
			if (version != expectedVersion)
				throw new ConcurrentModificationException();
			if (removableIter == null)
				throw new IllegalStateException();
			removableIter.remove();
			removableIter = null;
			size--;
			expectedVersion = ++version;
		}

		private void normalize() {
			if (version != expectedVersion)
				throw new ConcurrentModificationException();
			if (valueIter != null && valueIter.hasNext())
				return;
			if (!keyEntryIter.hasNext()) {
				valueIter = null;
				return;
			}
			Map.Entry<K, Set<V>> entry = keyEntryIter.next();
			curKey = entry.getKey();
			valueIter = entry.getValue().iterator();

			assert valueIter.hasNext();
		}

		// private boolean isEntryIter;
		private long expectedVersion;
		private Iterator<Map.Entry<K, Set<V>>> keyEntryIter;
		private Iterator<V> valueIter, removableIter;
		private K curKey;
	}

	private class ValueIterator extends IteratorBase<V> {
		protected V wrapNext(K key, V value) {
			return value;
		}
	}

	private class EntryIterator extends IteratorBase<MultiMap.Entry<K, V>> {
		protected MultiMap.Entry<K, V> wrapNext(K key, V value) {
			return new Entry<K, V>(key, value);
		}
	}

	public String toString() {
		StringBuffer buf = new StringBuffer("{");
		boolean first = true;
		for (Map.Entry<K, Set<V>> entry : map.entrySet()) {
			if (!first)
				buf.append(", ");
			first = false;
			buf.append(entry.getKey());
			buf.append('=');
			buf.append(entry.getValue());
		}
		buf.append('}');
		return buf.toString();
	}

	private Map<K, Set<V>> map;
	private Factory<Set<V>> setFactory;
	private int size;
	private transient Collection<V> values;
	private transient Set<MultiMap.Entry<K, V>> entries;
	private transient long version;

	private static class ConstructorFactory<T> implements Factory<T>, Serializable {

		private static final long serialVersionUID = 269873996119585947L;

		public ConstructorFactory(Class<? extends T> type) {
			this.type = type;
		}

		public T create() {
			try {
				return type.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		private Class<? extends T> type;
	}
}
