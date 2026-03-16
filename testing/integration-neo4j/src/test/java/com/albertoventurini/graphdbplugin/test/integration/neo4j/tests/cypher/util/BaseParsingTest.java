/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.albertoventurini.graphdbplugin.test.integration.neo4j.tests.cypher.util;

import com.albertoventurini.graphdbplugin.language.cypher.CypherParserDefinition;
import com.google.common.io.Resources;
import com.intellij.testFramework.ParsingTestCase;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Base for all parsing test cases.
 */
public abstract class BaseParsingTest  extends ParsingTestCase {

    public BaseParsingTest(String testDataFolder) {
        super(testDataFolder, "cyp", new CypherParserDefinition());
    }

    @Override
    protected String getTestDataPath() {
        try {
            return new File(Resources.getResource("parsing").toURI()).getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean skipSpaces() {
        return true;
    }

    @Override
    protected boolean includeRanges() {
        return true;
    }
}
