package com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.metadata;

import com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.state.DataSourceApi;

public interface MetadataBuilder {
    DataSourceMetadata buildMetadata(DataSourceApi dataSource);
}
