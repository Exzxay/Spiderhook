/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.database.api.query;

import com.github.mdelambilly.graphdbplugin.database.api.data.GraphNode;
import com.github.mdelambilly.graphdbplugin.database.api.data.GraphRelationship;

import java.util.List;

public interface GraphQueryResultRow {

    Object getValue(final String columnName);

    Object getValue(GraphQueryResultColumn column);

    List<GraphNode> getNodes();

    List<GraphRelationship> getRelationships();
}
