/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.jetbrains.ui.datasource.interactions;

import com.github.mdelambilly.graphdbplugin.database.api.GraphDatabaseApi;
import com.github.mdelambilly.graphdbplugin.database.api.query.GraphQueryResult;
import com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.state.DataSourceApi;
import com.github.mdelambilly.graphdbplugin.jetbrains.database.DatabaseManagerService;
import com.github.mdelambilly.graphdbplugin.jetbrains.services.ExecutorService;
import com.github.mdelambilly.graphdbplugin.jetbrains.ui.datasource.DataSourcesView;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public abstract class DataSourceDialog extends DialogWrapper {

    protected DataSourceDialog(@NotNull final Project project, DataSourcesView dataSourcesView) {
        super(project);
        Disposer.register(project, myDisposable);
        init();
    }

    public abstract DataSourceApi constructDataSource();

    protected abstract void showLoading();

    protected abstract void hideLoading();

    public boolean go() {
        init();
        return showAndGet();
    }

    public void validationPopup() {
        ValidationInfo validationInfo = doValidate();
        if (validationInfo != null) {
            Messages.showErrorDialog(getContentPanel(),
                    validationInfo.message, "Test Connection");
        } else {
            validateConnection();
        }
    }

    private void validateConnection() {
        final var executorService = ApplicationManager.getApplication().getService(ExecutorService.class);
        showLoading();
        executorService.runInBackground(
                this::executeOkQuery,
                (status) -> {
                    hideLoading();
                    Messages.showInfoMessage(getContentPanel(),
                            "Connection successful!", "Test Connection");
                },
                (exception) -> {
                    hideLoading();
                    Messages.showErrorDialog(getContentPanel(),
                            exception.getMessage(), "Test Connection");
                },
                ModalityState.current()
        );
    }

    // TODO: this needs to be moved in the DataSourceDialog implementation.
    // Right now, it assumes that the data source will understand a query such as "RETURN 'ok'"
    // which might not be true for data sources different than neo4j.
    private String executeOkQuery() {
        DataSourceApi dataSource = constructDataSource();
        DatabaseManagerService databaseManager =
                ApplicationManager.getApplication().getService(DatabaseManagerService.class);
        GraphDatabaseApi db = databaseManager.getDatabaseFor(dataSource);
        GraphQueryResult result = db.execute("RETURN 'ok'");

        Object value = result.getRows().get(0).getValue(result.getColumns().get(0));

        if (value.equals("ok")) {
            return "ok";
        } else {
            throw new RuntimeException("Unexpected test query output: " + value);
        }
    }
}