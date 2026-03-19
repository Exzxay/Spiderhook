/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.database.api;

import com.github.mdelambilly.graphdbplugin.database.api.data.GraphDatabaseVersion;
import com.github.mdelambilly.graphdbplugin.database.api.data.GraphMetadata;
import com.github.mdelambilly.graphdbplugin.database.api.query.GraphQueryResult;

import java.util.Map;

public interface GraphDatabaseApi {

    GraphQueryResult execute(String query);

    GraphQueryResult execute(String query, Map<String, Object> statementParameters);

    GraphMetadata metadata();

    GraphDatabaseVersion getVersion();
}
