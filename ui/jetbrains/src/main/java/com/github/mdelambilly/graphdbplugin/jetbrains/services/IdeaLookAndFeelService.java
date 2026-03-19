/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.jetbrains.services;

import com.github.mdelambilly.graphdbplugin.jetbrains.component.settings.SettingsComponent;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.UIUtil;
import com.github.mdelambilly.graphdbplugin.visualization.services.LookAndFeelService;

import java.awt.*;

public class IdeaLookAndFeelService implements LookAndFeelService {

    @Override
    public Color getBackgroundColor() {
        return UIUtil.getPanelBackground();
    }

    @Override
    public Color getBorderColor() {
        return JBColor.border();
    }

    @Override
    public Color getEdgeStrokeColor() {
        return JBColor.border();
    }

    @Override
    public Color getEdgeFillColor() {
        return JBColor.border();
    }

    @Override
    public Color getNodeStrokeColor() {
        return JBColor.border();
    }

    @Override
    public Color getNodeStrokeHoverColor() {
        return UIUtil.getLabelForeground();
    }

    @Override
    public Color getNodeFillColor() {
        return UIUtil.getPanelBackground();
    }

    @Override
    public Color getNodeFillHoverColor() {
        return UIUtil.getListSelectionBackground(true);
    }

    @Override
    public Color getTextColor() {
        return UIUtil.getLabelForeground();
    }

    @Override
    public boolean isGraphViewZoomInverted() {
        return SettingsComponent.getInstance().isGraphViewZoomInverted();
    }

}
