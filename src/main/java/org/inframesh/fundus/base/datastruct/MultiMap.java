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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A map which allows multiple values per key.  This interface is similar to
 * {@link java.util.Map}, but while the entries in an ordinary map have unique
 * keys, the entries in a multi-map are unqiue by key/value pair.
 * <p>
 * The behavior of a multi-map is undefined if its keys are mutable.
 * <p>
 * As with the standard Java collections classes, instances of MultiMap are
 * unsynchronized. 
 * <p>
 * The <a href="http://jakarta.apache.org/commons/collections.html">Jakarta Commons Collections</a>
 * package also provides a <a href="http://jakarta.apache.org/commons/collections/api/org/apache/commons/collections/MultiMap.html">MultiMap</a>.
 * I like mine a bit better -- instead of extending <a href="http://java.sun.com/j2se/1.3/docs/api/java/util/Map.html">Map</a>
 * (which ends up breaking the Map contract), it provides a set of methods more particularly tailored to the
 * behavior of a MultiMap.  However, the Jakarta Collections are more widely accepted, and are a
 * generally excellent package, so I certainly recommend checking them out!

 * <p align="center">
 * <table cellpadding=4 cellspacing=2 border=0 bgcolor="#338833" width="90%"><tr><td bgcolor="#EEEEEE">
 *  * <b>Maturity:</b>
 *  * This is a 90% mature API.
 * </td></tr><tr><td bgcolor="#EEEEEE">
 *  * <b>Plans:</b>
 *  * There are no current plans to expand or revise this interface.
 * </td></tr></table>
 * 
 * @since fundus
 * @version $Revision: 1.0 $Date:2010-2-25 下午03:24:25 $
 * 
 * @author <a href="mailto:richd.yang@gmail.com">Richard Yang</a>
 */ 
public interface MultiMap<K,V>
    {
    /** Returns the number of key/value mappings in the multi-map.
     */
    public int size();

    /** Returns true if there are no mappings.
     */
    public boolean isEmpty();

    /** Returns true if there is at least one mapping for the given key.
     */
    public boolean containsKey(K key);

    /** Returns true if there is at least one key which maps to the given
     *  values.  Note that this method will likely execute in linear time
     *  in the number of key/value paris.
     */
    public boolean containsValue(V value);

    /** Returns the set of values for a given key.
     */
    public Set<V> get(K key);

    /** Adds a mapping from a key to a value.  This does not replace other
     *  existing mappings from the same key.  However, if the key/value pair
     *  is already in the multi-map, it will not be added twice.
     *  @return true if the new key/value pair was not already in the multi-map.
     */
    public boolean put(K key, V value);

    /** Adds mappings from a key to several different values.  These values
     *  are in addition to any existing mappings for the given key.
     *  @return true if any of the new key/value pairs were not already in the multi-map.
     */
    public boolean putAll(K key, Collection<? extends V> values);

    /** Removes a key/value mapping.
     *  @return true if the key/value pair was in the multi-map.
     */
    public boolean remove(K key, V value);

    /** Removes all key/value mappings for a given key.
     *  @return The set of values which were associated with removed key.
     */
    public Set<V> removeKey(K key);

    /** Adds all entries from another multi-map to this one.
     */
    public void putAll(MultiMap<? extends K, ? extends V> multimap);

    /** Adds all entries from a map to this multi-map.
     */
    public void putAll(Map<? extends K, ? extends V> map);

    /** Clears all entries.
     */
    public void clear();

    /** Returns the set of keys in this multi-map.
     *  The returned set is backed by the actual multi-map, and will reflect
     *  subsequent changes.
     */
    public Set<K> keySet();

    /** Returns the values in this multi-map.
     *  The returned collection is backed by the actual multi-map, and will reflect
     *  subsequent changes.
     */
    public Collection<V> values();

    /** Returns the set of key/value pairs in this multi-map.
     *  The returned set is backed by the actual multi-map, and will reflect
     *  subsequent changes.
     */
    public Set<Entry<K,V>> entrySet();

    /** Represents a single key/value pair in the multi-map.
     */
    public interface Entry<EK,EV>
        {
        /** The key this entry maps from.
         */
        public EK getKey();

        /** The value this entry maps to.
         */
        public EV getValue();

        /** Two entries are equal if their keys and values are equal.  They
         *  need not be entries in the same multi-map.
         */
        public boolean equals(Object o);
    
        public int hashCode();
        }
    
    /** Two multi-maps are equal iff they contain the same key/value mappings.
     */
    public boolean equals(Object o);
    
    public int hashCode();
    }
