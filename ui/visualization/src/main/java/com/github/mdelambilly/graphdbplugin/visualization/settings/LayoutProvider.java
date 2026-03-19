/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.visualization.settings;

import com.github.mdelambilly.graphdbplugin.visualization.services.LookAndFeelService;
import com.github.mdelambilly.graphdbplugin.visualization.GraphDisplay;
import com.github.mdelambilly.graphdbplugin.visualization.layouts.CenteredLayout;
import com.github.mdelambilly.graphdbplugin.visualization.layouts.DynamicForceLayout;
import com.github.mdelambilly.graphdbplugin.visualization.layouts.RepaintAndRepositionAction;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.activity.Activity;

import static com.github.mdelambilly.graphdbplugin.visualization.constants.GraphGroups.EDGE_LABEL;
import static com.github.mdelambilly.graphdbplugin.visualization.constants.GraphGroups.GRAPH;
import static com.github.mdelambilly.graphdbplugin.visualization.constants.GraphGroups.NODE_LABEL;

public class LayoutProvider {

    private static final boolean ENFORCE_BOUNDS = false;

    public static ActionList forceLayout(Visualization viz, GraphDisplay display, LookAndFeelService lookAndFeel) {
        ActionList actions = new ActionList(viz);

        actions.add(new DynamicForceLayout(GRAPH, ENFORCE_BOUNDS));
        actions.add(ColorProvider.colors(lookAndFeel));
        actions.add(new RepaintAndRepositionAction(viz, display));

        return actions;
    }

    public static ActionList repaintLayout(LookAndFeelService lookAndFeelService) {
        ActionList repaint = new ActionList(Activity.INFINITY);
        repaint.add(new CenteredLayout(NODE_LABEL));
        repaint.add(new CenteredLayout(EDGE_LABEL));
        repaint.add(ColorProvider.colors(lookAndFeelService));
        repaint.add(new RepaintAction());

        return repaint;
    }
}
