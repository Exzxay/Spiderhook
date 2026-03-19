/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.language.cypher.documentation.database;

import org.junit.jupiter.api.Test;

public class CypherDocumentationTest {

    @Test
    public void testBuiltInFunctionsLoadsCorrectly() {
        CypherDocumentation.BUILT_IN_FUNCTIONS.lookup("toString");
    }
}
