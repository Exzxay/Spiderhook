/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.database.neo4j.bolt.query;

import com.github.mdelambilly.graphdbplugin.database.api.query.GraphQueryPlan;

import java.util.List;
import java.util.Map;

public class Neo4jBoltQueryPlan implements GraphQueryPlan {

    private String operatorType;
    private Map<String, Object> arguments;
    private List<String> identifiers;
    private List<Neo4jBoltQueryPlan> children;

    public Neo4jBoltQueryPlan(String operatorType, Map<String, Object> arguments, List<String> identifiers,
                              List<Neo4jBoltQueryPlan> children) {
        this.operatorType = operatorType;
        this.arguments = arguments;
        this.identifiers = identifiers;
        this.children = children;
    }

    @Override
    public String getOperatorType() {
        return operatorType;
    }

    @Override
    public Map<String, Object> getArguments() {
        return arguments;
    }

    @Override
    public List<String> getIdentifiers() {
        return identifiers;
    }

    @Override
    public List<Neo4jBoltQueryPlan> children() {
        return children;
    }
}
