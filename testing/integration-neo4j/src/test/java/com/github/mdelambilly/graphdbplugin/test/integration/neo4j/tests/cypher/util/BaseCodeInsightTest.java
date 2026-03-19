/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.test.integration.neo4j.tests.cypher.util;

import com.google.common.io.Resources;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URISyntaxException;

abstract class BaseCodeInsightTest extends BaseGenericTest {

    private String namespace;
    private final String dataPath;

    public BaseCodeInsightTest(String namespace, String dataPath) {
        this.namespace = namespace;
        this.dataPath = dataPath;
    }

    @Override
    protected String getTestDataPath() {
        try {
            return new File(Resources.getResource(namespace).toURI()).getAbsolutePath() + "/" + dataPath;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    protected String getTestName() {
        return getTestName(false);
    }
}
