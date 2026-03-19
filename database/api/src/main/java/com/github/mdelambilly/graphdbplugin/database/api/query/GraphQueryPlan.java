/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.database.api.query;

import java.util.List;
import java.util.Map;

public interface GraphQueryPlan {

    String getOperatorType();

    Map<String, Object> getArguments();

    List<String> getIdentifiers();

    List<? extends GraphQueryPlan> children();

}
