package com.github.mdelambilly.graphdbplugin.test.integration.neo4j.tests.cypher.parsing;

import com.github.mdelambilly.graphdbplugin.test.integration.neo4j.tests.cypher.util.BaseParsingTest;

public class LexerTest extends BaseParsingTest {

    public LexerTest() {
        super("lexer");
    }

    public void testScientificNotation() {
        doTest(true);
    }

    public void testHexNumbers() {
        doTest(true);
    }

    public void testOctalNumbers() {
        doTest(true);
    }

}
