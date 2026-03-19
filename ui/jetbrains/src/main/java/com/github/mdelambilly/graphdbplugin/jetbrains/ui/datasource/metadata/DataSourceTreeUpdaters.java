package com.github.mdelambilly.graphdbplugin.jetbrains.ui.datasource.metadata;

import com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.DataSourceType;
import com.intellij.openapi.components.Service;

import java.util.Map;
import java.util.Optional;

/**
 * Maintains a collection of known {@link DataSourceTreeUpdater} objects.
 */
@Service(Service.Level.PROJECT)
final class DataSourceTreeUpdaters {

    private final Map<DataSourceType, DataSourceTreeUpdater> handlers;

    DataSourceTreeUpdaters() {
        this.handlers = Map.of(
                DataSourceType.NEO4J_BOLT, new Neo4jBoltTreeUpdater()
        );
    }

    Optional<DataSourceTreeUpdater> get(final DataSourceType dataSourceType) {
        return Optional.ofNullable(handlers.get(dataSourceType));
    }
}
