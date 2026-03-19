/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.database.neo4j.bolt.data;

import com.github.mdelambilly.graphdbplugin.database.api.data.GraphPath;

import java.util.ArrayList;
import java.util.List;

public class Neo4jBoltPath implements GraphPath {

    private List<Object> pathComponents;

    public Neo4jBoltPath() {
        this.pathComponents = new ArrayList<>();
    }

    public Neo4jBoltPath(List<Object> pathComponents) {
        this.pathComponents = new ArrayList<>(pathComponents);
    }

    public void add(Object object) {
        pathComponents.add(object);
    }

    @Override
    public List<Object> getComponents() {
        return pathComponents;
    }
}
