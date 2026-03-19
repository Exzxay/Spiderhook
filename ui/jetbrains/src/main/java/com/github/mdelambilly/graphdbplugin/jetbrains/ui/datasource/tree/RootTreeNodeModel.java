/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.jetbrains.ui.datasource.tree;

import com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.state.DataSourceApi;
import com.github.mdelambilly.graphdbplugin.jetbrains.ui.datasource.metadata.dto.ContextMenu;

import javax.swing.*;
import java.util.Optional;

public class RootTreeNodeModel implements TreeNodeModelApi {

    public static final String ROOT_NAME = "treeRoot";

    @Override
    public Optional<ContextMenu> getContextMenu() {
        return Optional.empty();
    }

    @Override
    public NodeType getType() {
        return Neo4jTreeNodeType.ROOT;
    }

    @Override
    public Optional<Icon> getIcon() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getText() {
        return Optional.of(ROOT_NAME);
    }

    @Override
    public Optional<Object> getValue() {
        return Optional.empty();
    }

    @Override
    public DataSourceApi getDataSourceApi() {
        return null;
    }
}
