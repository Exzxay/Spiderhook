/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.test.integration.neo4j.tests.database.neo4j;

import com.github.mdelambilly.graphdbplugin.database.neo4j.bolt.data.Neo4jGraphDatabaseVersion;
import com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.metadata.neo4j.Neo4jMetadata;
import com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.metadata.neo4j.Neo4jFunctionMetadata;
import com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.metadata.neo4j.Neo4jProcedureMetadata;
import com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.state.DataSourceApi;
import com.github.mdelambilly.graphdbplugin.test.integration.neo4j.tests.database.common.AbstractDataSourceMetadataTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DataSourceMetadataTest extends AbstractDataSourceMetadataTest {

    @Override
    public DataSourceApi getDataSource() {
        return dataSource().neo4j526();
    }

    public void testHaveTestUserFunctions() {
        final Neo4jMetadata metadata = (Neo4jMetadata) getMetadata();
        final List<Neo4jFunctionMetadata> functionsMetadata = metadata.functions();
        assertThat(functionsMetadata)
                .isNotEmpty();
    }

    public void testMetadataHaveRequiredProcedures() {
        final Neo4jMetadata metadata = (Neo4jMetadata) getMetadata();
        final List<Neo4jProcedureMetadata> procedures = metadata.procedures();

        // dbms.components() is a stable procedure used by the driver itself to detect Neo4j version.
        assertTrue(procedures.stream().anyMatch(p -> p.name().equals("dbms.components")));
    }

    public void testGetVersion() {
        var metadata = (Neo4jMetadata) getMetadata();
        var version = (Neo4jGraphDatabaseVersion) metadata.version();
        assertEquals(5, version.major());
        assertEquals(26, version.minor());
    }
}
