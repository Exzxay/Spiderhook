/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.jetbrains.ui.datasource.tree.model;

import com.github.mdelambilly.graphdbplugin.database.api.data.NoIdGraphEntity;
import com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.state.DataSourceApi;
import com.github.mdelambilly.graphdbplugin.jetbrains.ui.datasource.metadata.dto.ContextMenu;
import com.github.mdelambilly.graphdbplugin.jetbrains.ui.console.graph.EntityContextMenu;
import com.github.mdelambilly.graphdbplugin.jetbrains.ui.datasource.tree.TreeNodeModelApi;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class RootObjectAwareModel implements TreeNodeModelApi {

    private DataSourceApi dataSourceApi;
    private Object rootObject;

    public RootObjectAwareModel(DataSourceApi dataSourceApi, Object rootObject) {
        this.dataSourceApi = dataSourceApi;
        this.rootObject = rootObject;
    }

    @Override
    public Optional<ContextMenu> getContextMenu() {
        if (rootObject instanceof NoIdGraphEntity) {
            return Optional.of(new EntityContextMenu(dataSourceApi, (NoIdGraphEntity) rootObject));
        } else if (this instanceof NoIdGraphEntity) {
            return Optional.of(new EntityContextMenu(dataSourceApi, (NoIdGraphEntity) this));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Object> getRootObjectValue() {
        return Optional.of(rootObject);
    }

    @Nullable
    @Override
    public DataSourceApi getDataSourceApi() {
        return dataSourceApi;
    }
}
