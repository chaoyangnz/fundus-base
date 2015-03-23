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
package com.richdyang.fundus.base.datastruct;

import java.util.*;

/**
 * A multi-map implementation which uses {@link HashMap} and {@link HashSet}.
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
public class HashMultiMap<K, V> extends AbstractMultiMap<K, V> {

	private static final long serialVersionUID = -3242497269253009542L;

	public HashMultiMap() {
		super(HashMap.class, HashSet.class);
	}

	public HashMultiMap(MultiMap<K, V> multimap) {
		super(HashMap.class, HashSet.class, multimap);
	}

	public HashMultiMap(Map<K, V> map) {
		super(HashMap.class, HashSet.class, map);
	}
}
