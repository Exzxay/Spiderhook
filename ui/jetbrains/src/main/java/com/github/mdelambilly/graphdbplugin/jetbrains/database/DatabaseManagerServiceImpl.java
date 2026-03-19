/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.jetbrains.database;

import com.github.mdelambilly.graphdbplugin.database.api.GraphDatabaseApi;
import com.github.mdelambilly.graphdbplugin.database.neo4j.bolt.Neo4jBoltDatabase;
import com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.DataSourceType;
import com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.state.DataSourceApi;

public class DatabaseManagerServiceImpl implements DatabaseManagerService {

    public DatabaseManagerServiceImpl() {
    }

    public GraphDatabaseApi getDatabaseFor(final DataSourceApi dataSource) {
        if (dataSource.getDataSourceType() == DataSourceType.NEO4J_BOLT) {
            return new Neo4jBoltDatabase(dataSource.getConfiguration());
        }
        throw new RuntimeException(String.format("Database for data source [%s] does not exists",
                dataSource.getDataSourceType()));
    }
}
