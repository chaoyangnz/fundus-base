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

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * A multi-map implementation which uses {@link TreeMap} and {@link TreeSet}.
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
 * @author <a href="mailto:josh.yoah@gmail.com">杨超</a>
 */ 
public class TreeMultiMap<K, V> extends AbstractMultiMap<K, V> {

	private static final long serialVersionUID = 4538492351897710281L;

	public TreeMultiMap() {
		super(TreeMap.class, TreeSet.class);
	}

	public TreeMultiMap(Comparator<K> keyComparator, final Comparator<V> valueComparator) {
		super(new TreeMap<K, Set<V>>(keyComparator), new Factory<Set<V>>() {
			public Set<V> create() {
				return new TreeSet<V>(valueComparator);
			}
		});
	}

	public TreeMultiMap(MultiMap<K, V> multimap) {
		super(TreeMap.class, TreeSet.class, multimap);
	}

	public TreeMultiMap(Map<K, V> map) {
		super(TreeMap.class, TreeSet.class, map);
	}
}
