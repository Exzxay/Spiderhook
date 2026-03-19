/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.visualization.layouts;

import com.github.mdelambilly.graphdbplugin.visualization.GraphDisplay;
import com.github.mdelambilly.graphdbplugin.visualization.util.PrefuseUtil;
import prefuse.Visualization;
import prefuse.action.RepaintAction;

public class RepaintAndRepositionAction extends RepaintAction {

    private Visualization visualization;
    private GraphDisplay display;

    public RepaintAndRepositionAction(Visualization visualization, GraphDisplay display) {
        super(visualization);
        this.visualization = visualization;
        this.display = display;
    }

    @Override
    public void run(double frac) {
        PrefuseUtil.zoomAndPanToFit(visualization, display);
        super.run(frac);
    }
}
