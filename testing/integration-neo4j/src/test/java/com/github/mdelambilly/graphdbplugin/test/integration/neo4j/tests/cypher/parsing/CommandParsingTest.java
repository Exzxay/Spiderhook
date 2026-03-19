/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.test.integration.neo4j.tests.cypher.parsing;

import com.github.mdelambilly.graphdbplugin.test.integration.neo4j.tests.cypher.util.BaseParsingTest;

public class CommandParsingTest extends BaseParsingTest {

    public CommandParsingTest() {
        super("command");
    }

    public void testConstraintPropertyExistsNodeCreate() {
        doTest(true);
    }

    public void testConstraintPropertyExistsNodeDrop() {
        doTest(true);
    }

    public void testConstraintPropertyExistsRelCreate() {
        doTest(true);
    }

    public void testConstraintPropertyExistsRelDrop() {
        doTest(true);
    }

    public void testConstraintPropertyUniqueCreate() {
        doTest(true);
    }

    public void testConstraintPropertyUniqueDrop() {
        doTest(true);
    }
}
