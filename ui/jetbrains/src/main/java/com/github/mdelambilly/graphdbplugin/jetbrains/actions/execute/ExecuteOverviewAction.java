package com.github.mdelambilly.graphdbplugin.jetbrains.actions.execute;

import com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.DataSourcesComponent;
import com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.state.DataSourceApi;
import com.github.mdelambilly.graphdbplugin.jetbrains.ui.console.ConsoleToolWindow;
import com.github.mdelambilly.graphdbplugin.jetbrains.ui.console.graph.GraphPanel;
import com.github.mdelambilly.graphdbplugin.jetbrains.util.Notifier;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBus;

import java.util.List;

public class ExecuteOverviewAction extends AnAction {

    private static final String OVERVIEW_QUERY = """
            MATCH (n)-[r]-(m)
            WHERE NOT (n:Subtopic OR m:Subtopic)
            RETURN n, r, m
            """;

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = getEventProject(e);

        if (project == null) {
            return;
        }

        MessageBus messageBus = project.getMessageBus();
        DataSourcesComponent dataSourcesComponent = project.getService(DataSourcesComponent.class);
        ExecuteQueryPayload payload = new ExecuteQueryPayload(OVERVIEW_QUERY);
        List<DataSourceApi> dataSources = dataSourcesComponent.getDataSourceContainer().getDataSources();

        if (dataSources.isEmpty()) {
            Notifier.error("Query execution error", "No data source configured.");
            return;
        }

        ConsoleToolWindow.ensureOpen(project);
        messageBus.syncPublisher(ExecuteQueryEvent.EXECUTE_QUERY_TOPIC)
                .executeQuery(dataSources.getFirst(), payload);

        GraphPanel.toOverview=true;
    }





}


