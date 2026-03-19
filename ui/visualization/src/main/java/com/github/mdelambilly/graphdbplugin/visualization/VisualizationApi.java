/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.visualization;

import com.github.mdelambilly.graphdbplugin.database.api.data.GraphNode;
import com.github.mdelambilly.graphdbplugin.database.api.data.GraphRelationship;
import com.github.mdelambilly.graphdbplugin.visualization.events.EventType;
import com.github.mdelambilly.graphdbplugin.visualization.events.NodeCallback;
import com.github.mdelambilly.graphdbplugin.visualization.events.RelationshipCallback;

import javax.swing.*;

public interface VisualizationApi {

    JComponent getCanvas();

    void addNode(GraphNode node);

    void addRelation(GraphRelationship relationship);

    void clear();

    void paint();

    void stop();

    void addNodeListener(EventType type, NodeCallback action);

    void addEdgeListener(EventType type, RelationshipCallback action);

    void resetPan();

    void updateSettings();
}
