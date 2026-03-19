/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.language.cypher.completion.metadata.atoms;

public enum CypherSimpleType implements CypherType {
    BOOLEAN,
    INTEGER,
    FLOAT,
    NUMBER,
    STRING,
    MAP,
    ANY,
    NULL,

    NODE,
    RELATIONSHIP,
    PATH
}
