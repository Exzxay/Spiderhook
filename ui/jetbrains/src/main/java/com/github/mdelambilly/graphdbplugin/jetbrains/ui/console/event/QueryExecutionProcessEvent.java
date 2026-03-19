/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.jetbrains.ui.console.event;

import com.github.mdelambilly.graphdbplugin.jetbrains.actions.execute.ExecuteQueryPayload;
import com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.state.DataSourceApi;
import com.intellij.util.messages.Topic;
import com.github.mdelambilly.graphdbplugin.database.api.query.GraphQueryResult;

public interface QueryExecutionProcessEvent {

    Topic<QueryExecutionProcessEvent> QUERY_EXECUTION_PROCESS_TOPIC =
            Topic.create("GraphDatabaseConsole.QueryExecutionProcessTopic", QueryExecutionProcessEvent.class);

    void executionStarted(DataSourceApi dataSource, ExecuteQueryPayload payload);

    void resultReceived(ExecuteQueryPayload payload, GraphQueryResult result);

    void postResultReceived(ExecuteQueryPayload payload);

    void handleError(ExecuteQueryPayload payload, Exception exception);

    void executionCompleted(ExecuteQueryPayload payload);
}
