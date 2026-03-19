/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.jetbrains.ui.datasource.metadata;

import com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.state.DataSourceApi;
import com.intellij.util.messages.Topic;

public interface MetadataRetrieveEvent {

    Topic<MetadataRetrieveEvent> METADATA_RETRIEVE_EVENT = Topic.create("GraphDatabaseDataSource.MetadataRetrieve", MetadataRetrieveEvent.class);

    void startMetadataRefresh(DataSourceApi nodeDataSource);

    void metadataRefreshSucceed(DataSourceApi nodeDataSource);

    void metadataRefreshFailed(DataSourceApi nodeDataSource, Exception exception);
}
