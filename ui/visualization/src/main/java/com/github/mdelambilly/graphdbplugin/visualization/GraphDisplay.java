/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.visualization;

import com.github.mdelambilly.graphdbplugin.visualization.constants.GraphColumns;
import com.github.mdelambilly.graphdbplugin.visualization.constants.GraphGroups;
import com.github.mdelambilly.graphdbplugin.visualization.controls.CustomNeighborHighlightControl;
import com.github.mdelambilly.graphdbplugin.visualization.events.EventType;
import com.github.mdelambilly.graphdbplugin.visualization.events.NodeCallback;
import com.github.mdelambilly.graphdbplugin.visualization.events.RelationshipCallback;
import com.github.mdelambilly.graphdbplugin.visualization.layouts.CustomItemSorter;
import com.github.mdelambilly.graphdbplugin.visualization.listeners.NodeListener;
import com.github.mdelambilly.graphdbplugin.visualization.listeners.RelationshipListener;
import com.github.mdelambilly.graphdbplugin.visualization.services.LookAndFeelService;
import com.github.mdelambilly.graphdbplugin.visualization.settings.LayoutProvider;
import com.github.mdelambilly.graphdbplugin.visualization.settings.RendererProvider;
import com.github.mdelambilly.graphdbplugin.visualization.settings.SchemaProvider;
import com.github.mdelambilly.graphdbplugin.database.api.data.GraphNode;
import com.github.mdelambilly.graphdbplugin.database.api.data.GraphRelationship;
import com.github.mdelambilly.graphdbplugin.visualization.util.DisplayUtil;
import com.github.mdelambilly.graphdbplugin.visualization.util.PrefuseUtil;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.RendererFactory;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;

import java.util.HashMap;
import java.util.Map;

import static prefuse.Constants.*;

public class GraphDisplay extends Display {

    private static final boolean DIRECTED = true;

    private static final String LAYOUT = "layout";
    private static final String REPAINT = "repaint";

    private Graph graph;

    private Map<String, Node> nodeMap = new HashMap<>();
    private Map<String, GraphNode> graphNodeMap = new HashMap<>();
    private Map<String, GraphRelationship> graphRelationshipMap = new HashMap<>();
    private CustomNeighborHighlightControl highlightControl;
    private WheelZoomControl zoomControl;
    private LookAndFeelService lookAndFeel;

    public GraphDisplay(LookAndFeelService lookAndFeel) {
        super(new Visualization());
        this.lookAndFeel = lookAndFeel;

        setBackground(lookAndFeel.getBackgroundColor());

        graph = new Graph(DIRECTED);
        graph.addColumn(GraphColumns.ID, String.class);
        graph.addColumn(GraphColumns.TYPE, String.class);
        graph.addColumn(GraphColumns.TITLE, String.class);

        m_vis.addGraph(GraphGroups.GRAPH, graph, null, SchemaProvider.provideNodeSchema(), SchemaProvider.provideEdgeSchema());
        m_vis.setInteractive(GraphGroups.EDGES, null, false);
        m_vis.setValue(GraphGroups.NODES, null, VisualItem.SHAPE, SHAPE_ELLIPSE);

        m_vis.addDecorators(GraphGroups.NODE_LABEL, GraphGroups.NODES, SchemaProvider.provideFontSchema());
//        m_vis.addDecorators(EDGE_LABEL, EDGES, SchemaProvider.provideFontSchemaWithBackground());

        m_vis.setRendererFactory(setupRenderer());

        m_vis.putAction(LAYOUT, LayoutProvider.forceLayout(m_vis, this, lookAndFeel));
        m_vis.putAction(REPAINT, LayoutProvider.repaintLayout(lookAndFeel));

        setItemSorter(new CustomItemSorter());

        setHighQuality(true);

        addControlListener(new DragControl());
        zoomControl = new WheelZoomControl(lookAndFeel.isGraphViewZoomInverted(), lookAndFeel.isGraphViewZoomInverted());
        addControlListener(zoomControl);
        addControlListener(new ZoomToFitControl());
        addControlListener(new PanControl());
        highlightControl = new CustomNeighborHighlightControl();
        addControlListener(highlightControl);
    }

    public void clearGraph() {
        highlightControl.clear();
        graph.clear();
    }

    public void addNodeListener(EventType type, NodeCallback callback) {
        addControlListener(new NodeListener(type, callback, graphNodeMap));
    }

    public void addEdgeListener(EventType type, RelationshipCallback callback) {
        addControlListener(new RelationshipListener(type, callback, graphRelationshipMap));
    }

    public void addNode(GraphNode graphNode) {
        Node node = graph.addNode();
        node.set(GraphColumns.ID, graphNode.getId());
        node.set(GraphColumns.TYPE, DisplayUtil.getType(graphNode));
        node.set(GraphColumns.TITLE, DisplayUtil.getProperty(graphNode));

        nodeMap.put(graphNode.getId(), node);
        graphNodeMap.put(graphNode.getId(), graphNode);
    }

    public void addRelationship(GraphRelationship graphRelationship) {
        if (graphRelationship.hasStartAndEndNode()) {
            String start = graphRelationship.getStartNode().getId();
            String end = graphRelationship.getEndNode().getId();

            Edge edge = graph.addEdge(nodeMap.get(start), nodeMap.get(end));
            edge.set(GraphColumns.ID, graphRelationship.getId());
            edge.set(GraphColumns.TITLE, graphRelationship.getId());
            graphRelationshipMap.put(graphRelationship.getId(), graphRelationship);
        }
    }

    private RendererFactory setupRenderer() {
        DefaultRendererFactory rendererFactory = new DefaultRendererFactory(RendererProvider.nodeRenderer(), RendererProvider.edgeRenderer());
        rendererFactory.add(new InGroupPredicate(GraphGroups.NODE_LABEL), RendererProvider.labelRenderer());
        rendererFactory.add(new InGroupPredicate(GraphGroups.EDGE_LABEL), RendererProvider.edgeLabelRenderer());

        return rendererFactory;
    }

    public void startLayout() {
        m_vis.run(LAYOUT);
        m_vis.run(REPAINT);
    }

    public void stopLayout() {
        m_vis.cancel(LAYOUT);
        m_vis.cancel(REPAINT);
    }

    public void zoomAndPanToFit() {
        PrefuseUtil.zoomAndPanToFit(m_vis, this);
    }

    public void updateSettings() {
        removeControlListener(zoomControl);
        zoomControl = new WheelZoomControl(lookAndFeel.isGraphViewZoomInverted(), lookAndFeel.isGraphViewZoomInverted());
        addControlListener(zoomControl);

    }
}
