package com.richdyang.fundus.base.datastruct;

import java.util.Collection;

/**
 * A foolishly simple abstraction of a directed graph.
 * <p>
 * <p align="center">
 * <table cellpadding=4 cellspacing=2 border=0 bgcolor="#338833" width="90%">
 * <tr>
 * <td bgcolor="#EEEEEE">
 * <b>Maturity:</b> Though small, this is a mature API.</td>
 * </tr>
 * <tr>
 * <td bgcolor="#EEEEEE">
 * <b>Plans:</b> In the future, innig-util will include more complete graph
 * handling utilities. This interface might optionally support bi-directional
 * traversal. See {@link Graphs}.</td>
 * </tr>
 * </table>
 *
 * @author <a href="mailto:richd.yang@gmail.com">Richard Yang</a>
 * @version $Revision: 1.0 $Date:2010-2-25 下午03:24:25 $
 * @since fundus
 */
public interface GraphVisitor<N> {
    /**
     * Returns the edges reachable from a node of the graph.
     */

    public Collection<N> getEdgesFrom(N node);
}
