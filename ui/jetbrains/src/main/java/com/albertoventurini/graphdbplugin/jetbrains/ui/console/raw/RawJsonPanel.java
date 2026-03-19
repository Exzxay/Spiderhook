/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.albertoventurini.graphdbplugin.jetbrains.ui.console.raw;

import com.albertoventurini.graphdbplugin.database.api.data.GraphNode;
import com.albertoventurini.graphdbplugin.database.api.data.GraphRelationship;
import com.albertoventurini.graphdbplugin.database.api.query.GraphQueryResult;
import com.albertoventurini.graphdbplugin.database.api.query.GraphQueryResultColumn;
import com.albertoventurini.graphdbplugin.jetbrains.actions.execute.ExecuteQueryPayload;
import com.albertoventurini.graphdbplugin.jetbrains.component.datasource.state.DataSourceApi;
import com.albertoventurini.graphdbplugin.jetbrains.ui.console.GraphConsoleView;
import com.albertoventurini.graphdbplugin.jetbrains.ui.console.event.QueryExecutionProcessEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.intellij.json.JsonFileType;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.testFramework.LightVirtualFile;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RawJsonPanel implements Disposable {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

    private Editor editor;
    private Document document;

    public void initialize(GraphConsoleView graphConsoleView, Project project) {
        LightVirtualFile virtualFile = new LightVirtualFile("query-result.json", JsonFileType.INSTANCE, "{}");
        document = FileDocumentManager.getInstance().getDocument(virtualFile);
        editor = EditorFactory.getInstance().createEditor(document, project, JsonFileType.INSTANCE, true);

        JLabel label = new JLabel("Raw JSON output of last query execution");
        editor.setHeaderComponent(label);

        graphConsoleView.getRawTab().add(editor.getComponent(), BorderLayout.CENTER);

        project.getMessageBus().connect().subscribe(
                QueryExecutionProcessEvent.QUERY_EXECUTION_PROCESS_TOPIC,
                new QueryExecutionProcessEvent() {
                    @Override
                    public void executionStarted(DataSourceApi dataSource, ExecuteQueryPayload payload) {
                        if (document != null) {
                            ApplicationManager.getApplication().invokeLater(() ->
                                    WriteCommandAction.runWriteCommandAction(project, () -> document.setText("{}")));
                        }
                    }

                    @Override
                    public void resultReceived(ExecuteQueryPayload payload, GraphQueryResult result) {
                        if (document == null) return;
                        String json = toJson(result);
                        ApplicationManager.getApplication().invokeLater(() ->
                                WriteCommandAction.runWriteCommandAction(project, () -> document.setText(json)));
                    }

                    @Override
                    public void postResultReceived(ExecuteQueryPayload payload) {
                    }

                    @Override
                    public void handleError(ExecuteQueryPayload payload, Exception exception) {
                    }

                    @Override
                    public void executionCompleted(ExecuteQueryPayload payload) {
                    }
                });
    }

    private String toJson(GraphQueryResult result) {
        Map<String, Object> output = new LinkedHashMap<>();

        List<GraphNode> nodes = result.getNodes();
        List<GraphRelationship> relationships = result.getRelationships();
        List<GraphQueryResultColumn> columns = result.getColumns();

        if (!nodes.isEmpty()) {
            output.put("nodes", nodes.stream().map(this::nodeToMap).collect(Collectors.toList()));
        }
        if (!relationships.isEmpty()) {
            output.put("relationships", relationships.stream().map(this::relationshipToMap).collect(Collectors.toList()));
        }
        if (nodes.isEmpty() && relationships.isEmpty()) {
            // Tabular result: show rows
            output.put("columns", columns.stream().map(GraphQueryResultColumn::getName).collect(Collectors.toList()));
            output.put("rows", result.getRows().stream().map(row -> {
                Map<String, Object> rowMap = new LinkedHashMap<>();
                columns.forEach(col -> rowMap.put(col.getName(), convertValue(row.getValue(col))));
                return rowMap;
            }).collect(Collectors.toList()));
        }

        try {
            return MAPPER.writeValueAsString(output).replace("\r\n", "\n").replace("\r", "\n");
        } catch (JsonProcessingException e) {
            return "{\"error\": \"Serialization failed: " + e.getMessage().replace("\"", "'") + "\"}";
        }
    }

    private Map<String, Object> nodeToMap(GraphNode node) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", node.getId());
        map.put("labels", node.getTypes());
        map.put("properties", convertProperties(node.getPropertyContainer().getProperties()));
        return map;
    }

    private Map<String, Object> relationshipToMap(GraphRelationship rel) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", rel.getId());
        map.put("type", rel.getTypesName());
        if (rel.hasStartAndEndNode()) {
            map.put("startNodeId", rel.getStartNodeId());
            map.put("endNodeId", rel.getEndNodeId());
        }
        map.put("properties", convertProperties(rel.getPropertyContainer().getProperties()));
        return map;
    }

    private Map<String, Object> convertProperties(Map<String, Object> props) {
        Map<String, Object> result = new LinkedHashMap<>();
        props.forEach((k, v) -> result.put(k, convertValue(v)));
        return result;
    }

    private Object convertValue(Object value) {
        if (value == null) return null;
        if (value instanceof String || value instanceof Number || value instanceof Boolean) return value;
        if (value instanceof List) {
            return ((List<?>) value).stream().map(this::convertValue).collect(Collectors.toList());
        }
        if (value instanceof Map) {
            Map<String, Object> map = new LinkedHashMap<>();
            ((Map<?, ?>) value).forEach((k, v) -> map.put(String.valueOf(k), convertValue(v)));
            return map;
        }
        if (value instanceof GraphNode) return nodeToMap((GraphNode) value);
        if (value instanceof GraphRelationship) return relationshipToMap((GraphRelationship) value);
        return value.toString();
    }

    @Override
    public void dispose() {
        if (editor != null && !editor.isDisposed()) {
            EditorFactory.getInstance().releaseEditor(editor);
        }
    }
}
