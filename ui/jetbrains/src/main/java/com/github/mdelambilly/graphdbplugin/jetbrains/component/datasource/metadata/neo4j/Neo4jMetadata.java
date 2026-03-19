package com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.metadata.neo4j;

import com.github.mdelambilly.graphdbplugin.database.api.data.GraphDatabaseVersion;
import com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.metadata.DataSourceMetadata;

import java.util.*;

public record Neo4jMetadata(
        GraphDatabaseVersion version,
        List<Neo4jFunctionMetadata> functions,
        List<Neo4jProcedureMetadata> procedures,
        List<Neo4jConstraintMetadata> constraints,
        List<Neo4jLabelMetadata> labels,
        List<Neo4jRelationshipTypeMetadata> relationshipTypes,
        List<Neo4jIndexMetadata> indexes,
        List<String> propertyKeys)
        implements DataSourceMetadata { }
