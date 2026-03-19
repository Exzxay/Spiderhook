/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.database.neo4j.bolt.data;

import com.github.mdelambilly.graphdbplugin.database.api.query.GraphQueryNotification;

public class Neo4jBoltQueryNotification implements GraphQueryNotification {

    private String title;
    private String description;
    private Integer positionOffset;

    public Neo4jBoltQueryNotification(String title, String description, Integer positionOffset) {
        this.title = title;
        this.description = description;
        this.positionOffset = positionOffset;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Integer getPositionOffset() {
        return positionOffset;
    }
}
