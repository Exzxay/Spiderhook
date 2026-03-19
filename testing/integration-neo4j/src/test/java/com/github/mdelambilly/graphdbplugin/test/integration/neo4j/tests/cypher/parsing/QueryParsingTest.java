/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.test.integration.neo4j.tests.cypher.parsing;

import com.github.mdelambilly.graphdbplugin.test.integration.neo4j.tests.cypher.util.BaseParsingTest;

public class QueryParsingTest extends BaseParsingTest {

    public QueryParsingTest() {
        super("query");
    }

    public void testMultipleQueries() {
        doTest(true);
    }
}
