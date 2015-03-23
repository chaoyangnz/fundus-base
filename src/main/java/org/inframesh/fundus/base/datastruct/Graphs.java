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

import java.util.*;

/**
 * Graph utilities.
 * 
 * <p align="center">
 * <table cellpadding=4 cellspacing=2 border=0 bgcolor="#338833" width="90%">
 * <tr>
 * <td bgcolor="#EEEEEE">
 * <b>Maturity:</b> This is obviously a very immature API. The implementation of
 * the single method in it, however, works quite well.</td>
 * </tr>
 * <tr>
 * <td bgcolor="#EEEEEE">
 * <b>Plans:</b> In the future, innig-util will include more complete graph
 * handling utilities.</td>
 * </tr>
 * </table>
 * 
 * @since fundus
 * @version $Revision: 1.0 $Date:2010-2-25 下午03:24:25 $
 * 
 * @author <a href="mailto:richd.yang@gmail.com">Richard Yang</a>
 */ 
public abstract class Graphs {
	/**
	 * Returns the set of all nodes reachable along directed paths from a given
	 * node in a given graph. The implementation is smart about cycle detection.
	 */

	public static <N> Set<N> reachableNodes(N initial, GraphVisitor<N> walker) {
		return reachableNodesFromSet(Collections.singleton(initial), walker);
	}

	/**
	 * Returns the set of all nodes reachable along directed paths from a given
	 * set of nodes in a given graph. The implementation is smart about cycle
	 * detection.
	 */

	public static <N> Set<N> reachableNodesFromSet(Set<N> initial, GraphVisitor<N> walker) {
		Set<N> nodes = new HashSet<N>(), newNodes = initial, newerNodes = new HashSet<N>();

		while (!newNodes.isEmpty()) {
			nodes.addAll(newNodes); // Put the new nodes in the current list.

			// Now put all the newly reachable nodes a list of even newer nodes.

			for (N newNode : newNodes)
				newerNodes.addAll(walker.getEdgesFrom(newNode));

			// Add unwalked newer nodes for next time

			newerNodes.removeAll(nodes);
			newNodes = newerNodes;
			newerNodes = new HashSet<N>();
		}

		return nodes;
	}

	public static <N> Set<List<N>> findCycles(N initial, GraphVisitor<N> walker) {
		Set<List<N>> cycles = new HashSet<List<N>>(); // ! change to
														// IdentityHashSet
		findCycles(initial, cycles, walker, new ArrayList<N>(), new HashSet<N>()); // !
																					// change
																					// to
																					// IdentityHashSet
		return cycles;
	}

	private static <N> void findCycles(N node, Set<List<N>> cycles, GraphVisitor<N> walker, List<N> curPath, Set<N> visited) {
		curPath.add(node);

		if (visited.contains(node))
			cycles.add(Collections.unmodifiableList(new ArrayList<N>(curPath))); // !
																					// wastes
																					// 10%
		else {
			visited.add(node);
			for (N neighbor : walker.getEdgesFrom(node))
				findCycles(neighbor, cycles, walker, curPath, visited);
			visited.remove(node);
		}

		curPath.remove(curPath.size() - 1);
	}

	private Graphs() {
	} // To prevent instantiation
}
