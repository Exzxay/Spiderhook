/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.visualization.settings;

import prefuse.data.Schema;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.PrefuseLib;
import prefuse.visual.VisualItem;

import javax.swing.*;
import java.awt.*;

import static prefuse.Constants.SHAPE_ELLIPSE;

public class SchemaProvider {

    private static final int FONT_SIZE = 10;
    private static final String UI_DEFAULT_FONT_KEY = "Label.font";

    private static int getFontColor() {
        Color c = UIManager.getColor("Label.foreground");
        return c != null ? ColorLib.color(c) : ColorLib.rgb(15, 15, 45);
    }

    private static int getEdgeLabelBackground() {
        Color c = UIManager.getColor("ToolTip.background");
        return c != null ? ColorLib.color(c) : ColorLib.rgb(240, 230, 80);
    }

    public static Schema provideFontSchema() {
        final Schema fontSchema = PrefuseLib.getVisualItemSchema();
        fontSchema.setDefault(VisualItem.INTERACTIVE, false);
        fontSchema.setDefault(VisualItem.TEXTCOLOR, getFontColor());
        Font font = FontLib.getFont(UIManager.getFont(UI_DEFAULT_FONT_KEY).getFontName(), FONT_SIZE);
        fontSchema.setDefault(VisualItem.FONT, font);

        return fontSchema;
    }

    public static Schema provideFontSchemaWithBackground() {
        Schema schema = provideFontSchema();
        schema.setDefault(VisualItem.FILLCOLOR, getEdgeLabelBackground());

        return schema;
    }

    public static Schema provideNodeSchema() {
        Schema nodeSchema = PrefuseLib.getVisualItemSchema();
        nodeSchema.setDefault(VisualItem.SHAPE, SHAPE_ELLIPSE);

        return nodeSchema;
    }

    public static Schema provideEdgeSchema() {
        return PrefuseLib.getVisualItemSchema();
    }
}
