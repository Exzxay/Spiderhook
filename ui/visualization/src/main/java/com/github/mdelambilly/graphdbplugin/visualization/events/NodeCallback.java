/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.visualization.events;

import com.github.mdelambilly.graphdbplugin.database.api.data.GraphNode;
import prefuse.visual.VisualItem;

import java.awt.event.MouseEvent;

@FunctionalInterface
public interface NodeCallback {
    void accept(GraphNode node, VisualItem item, MouseEvent e);
}
