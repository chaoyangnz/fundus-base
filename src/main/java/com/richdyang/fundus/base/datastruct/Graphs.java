package com.richdyang.fundus.base.datastruct;

import java.util.*;

import static java.util.Collections.singleton;
import static java.util.Collections.unmodifiableList;

/**
 * Graph utilities.
 * <p>
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
 * @author <a href="mailto:richd.yang@gmail.com">Richard Yang</a>
 * @version $Revision: 1.0 $Date:2010-2-25 下午03:24:25 $
 * @since fundus
 */
public abstract class Graphs {
    /**
     * Returns the set of all nodes reachable along directed paths from a given
     * node in a given graph. The implementation is smart about cycle detection.
     */

    public static <N> Set<N> reachableNodes(N initial, GraphVisitor<N> walker) {
        return reachableNodesFromSet(singleton(initial), walker);
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
            cycles.add(unmodifiableList(new ArrayList<N>(curPath))); // !
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
