/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.jetbrains.ui.console.graph;

import com.github.mdelambilly.graphdbplugin.jetbrains.actions.execute.ExecuteQueryPayload;
import com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.state.DataSourceApi;
import com.github.mdelambilly.graphdbplugin.jetbrains.database.QueryExecutionService;
import com.google.common.collect.ImmutableMap;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.github.mdelambilly.graphdbplugin.database.api.data.GraphNode;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class NodeDeleteAction extends AnAction {

    private DataSourceApi dataSource;
    private GraphNode node;

    NodeDeleteAction(String title, String description, Icon icon, DataSourceApi dataSource, GraphNode node) {
        super(title, description, icon);
        this.dataSource = dataSource;
        this.node = node;
    }

    @Override
    public void actionPerformed(@NotNull final AnActionEvent e) {
        Project project = getEventProject(e);
        if (project != null) {
            QueryExecutionService service = new QueryExecutionService(project, project.getMessageBus());

            service.executeQuery(dataSource, new ExecuteQueryPayload("MATCH (n) WHERE ID(n) = $id DETACH DELETE n",
                    ImmutableMap.of("id", Long.parseLong(node.getId())),
                    null));
        }
    }

}
