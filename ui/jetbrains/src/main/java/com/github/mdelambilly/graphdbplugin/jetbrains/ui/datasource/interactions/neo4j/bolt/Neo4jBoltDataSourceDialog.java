/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.jetbrains.ui.datasource.interactions.neo4j.bolt;

import com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.DataSourceType;
import com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.DataSourcesComponent;
import com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.state.DataSourceApi;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.AsyncProcessIcon;
import com.github.mdelambilly.graphdbplugin.database.neo4j.bolt.Neo4jBoltConfiguration;
import com.github.mdelambilly.graphdbplugin.jetbrains.ui.datasource.DataSourcesView;
import com.github.mdelambilly.graphdbplugin.jetbrains.ui.datasource.interactions.DataSourceDialog;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.ui.components.JBTabbedPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import static com.github.mdelambilly.graphdbplugin.jetbrains.util.Validation.*;

public class Neo4jBoltDataSourceDialog extends DataSourceDialog {
    private final DataSourcesComponent dataSourcesComponent;
    private DataSourceApi dataSourceToEdit;

    private JBTextField dataSourceNameField;
    private JComboBox<String> protocolComboBox;
    private JBTextField hostField;
    private JBTextField portField;
    private JBTextField userField;
    private JBTextField databaseField;
    private JBPasswordField passwordField;
    private JButton testConnectionButton;
    private JPanel loadingPanel;
    private AsyncProcessIcon loadingIcon;
    private JComboBox<String> authTypeComboBox;
    private JScrollPane scrollPane;

    public Neo4jBoltDataSourceDialog(
            @NotNull final Project project,
            @NotNull final DataSourcesView dataSourcesView,
            @NotNull final DataSourceApi dataSourceToEdit) {
        this(project, dataSourcesView);
        this.dataSourceToEdit = dataSourceToEdit;
    }

    public Neo4jBoltDataSourceDialog(
            @NotNull final Project project,
            @NotNull final DataSourcesView dataSourcesView) {
        super(project, dataSourcesView);
        loadingPanel.setVisible(false);
        dataSourcesComponent = dataSourcesView.getComponent();
        testConnectionButton.addActionListener(e -> validationPopup());
        authTypeComboBox.addActionListener(this::handleAuthTypeChanged);
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if (StringUtils.isBlank(dataSourceNameField.getText())) {
            return validation("Data source name must not be empty", dataSourceNameField);
        }
        if (StringUtils.isBlank(hostField.getText())) {
            return validation("Host must not be empty", hostField);
        }
        if (!StringUtils.isNumeric(portField.getText())) {
            return validation("Port must be numeric", portField);
        }

        final var data = extractData();

        if (dataSourcesComponent.getDataSourceContainer().isDataSourceExists(data.dataSourceName)) {
            if (!(dataSourceToEdit != null && dataSourceToEdit.getName().equals(data.dataSourceName))) {
                return validation(String.format("Data source [%s] already exists", data.dataSourceName), dataSourceNameField);
            }
        }

        return null;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        if (scrollPane == null) {
            setupUI();
        }
        if (dataSourceToEdit != null) {
            Map<String, String> conf = dataSourceToEdit.getConfiguration();
            String protocol = conf.get(Neo4jBoltConfiguration.PROTOCOL);
            String host = conf.get(Neo4jBoltConfiguration.HOST);
            String port = conf.get(Neo4jBoltConfiguration.PORT);
            String authType = conf.get(Neo4jBoltConfiguration.AUTH_TYPE);
            String database = conf.get(Neo4jBoltConfiguration.DATABASE);
            String user = conf.get(Neo4jBoltConfiguration.USER);
            String password = conf.get(Neo4jBoltConfiguration.PASSWORD);

            dataSourceNameField.setText(dataSourceToEdit.getName());

            for (int i = 0; i < protocolComboBox.getItemCount(); i++) {
                if (protocolComboBox.getItemAt(i).equals(protocol)) {
                    protocolComboBox.setSelectedIndex(i);
                    break;
                }
            }

            for (int i = 0; i < authTypeComboBox.getItemCount(); i++) {
                if (authTypeComboBox.getItemAt(i).equals(authType)) {
                    authTypeComboBox.setSelectedIndex(i);
                    break;
                }
            }

            hostField.setText(host);
            portField.setText(port);
            databaseField.setText(database);
            userField.setText(user);
            passwordField.setText(password);
        }
        return scrollPane;
    }

    private void setupUI() {
        dataSourceNameField = new JBTextField();
        protocolComboBox = new JComboBox<>(new String[]{"bolt", "bolt+s", "bolt+ssc", "neo4j", "neo4j+s", "neo4j+ssc"});
        hostField = new JBTextField("localhost");
        portField = new JBTextField("7687");
        databaseField = new JBTextField();
        authTypeComboBox = new JComboBox<>(new String[]{"User & Password", "No auth"});
        userField = new JBTextField();
        passwordField = new JBPasswordField();
        testConnectionButton = new JButton("Test connection");
        loadingIcon = new AsyncProcessIcon("validateConnectionIcon");
        loadingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        loadingPanel.add(loadingIcon);

        Insets labelInsets = new Insets(4, 4, 4, 8);
        Insets fieldInsets = new Insets(4, 0, 4, 4);

        // --- Name row ---
        JPanel nameRow = new JPanel(new GridBagLayout());
        addLabelAndField(nameRow, "Name", dataSourceNameField, 0, labelInsets, fieldInsets);

        // --- General tab panel (8 rows) ---
        JPanel generalPanel = new JPanel(new GridBagLayout());
        addLabelAndField(generalPanel, "Protocol",       protocolComboBox,  0, labelInsets, fieldInsets);
        addLabelAndField(generalPanel, "Host",           hostField,         1, labelInsets, fieldInsets);
        addLabelAndField(generalPanel, "Port",           portField,         2, labelInsets, fieldInsets);
        addLabelAndField(generalPanel, "Database",       databaseField,     3, labelInsets, fieldInsets);
        addLabelAndField(generalPanel, "Authentication", authTypeComboBox,  4, labelInsets, fieldInsets);
        addLabelAndField(generalPanel, "User",           userField,         5, labelInsets, fieldInsets);
        addLabelAndField(generalPanel, "Password",       passwordField,     6, labelInsets, fieldInsets);

        GridBagConstraints bc = new GridBagConstraints();
        bc.gridy = 7; bc.gridx = 1;
        bc.fill = GridBagConstraints.NONE; bc.anchor = GridBagConstraints.WEST;
        bc.insets = new Insets(4, 0, 4, 4);
        generalPanel.add(testConnectionButton, bc);
        bc.gridx = 2;
        generalPanel.add(loadingPanel, bc);

        // vertical filler
        GridBagConstraints filler = new GridBagConstraints();
        filler.gridy = 8; filler.gridx = 0; filler.weighty = 1.0;
        filler.fill = GridBagConstraints.VERTICAL;
        generalPanel.add(Box.createVerticalGlue(), filler);

        JBTabbedPane tabs = new JBTabbedPane();
        tabs.addTab("General", generalPanel);

        // --- Content panel (inside scroll pane) ---
        JPanel content = new JPanel(new GridBagLayout());
        content.setMinimumSize(new Dimension(400, 250));
        content.setPreferredSize(new Dimension(600, 350));

        GridBagConstraints cc = new GridBagConstraints();
        cc.gridx = 0; cc.weightx = 1.0;
        cc.gridy = 0; cc.weighty = 0.0; cc.fill = GridBagConstraints.HORIZONTAL;
        content.add(nameRow, cc);
        cc.gridy = 1; cc.weighty = 1.0; cc.fill = GridBagConstraints.BOTH;
        content.add(tabs, cc);

        scrollPane = new JScrollPane(content);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    private void addLabelAndField(JPanel panel, String label, JComponent field,
                                  int row, Insets labelInsets, Insets fieldInsets) {
        GridBagConstraints lc = new GridBagConstraints();
        lc.gridy = row; lc.gridx = 0;
        lc.fill = GridBagConstraints.NONE; lc.anchor = GridBagConstraints.WEST;
        lc.insets = labelInsets;
        panel.add(new JLabel(label), lc);

        GridBagConstraints fc = new GridBagConstraints();
        fc.gridy = row; fc.gridx = 1; fc.gridwidth = 2;
        fc.fill = GridBagConstraints.HORIZONTAL; fc.weightx = 1.0;
        fc.insets = fieldInsets;
        panel.add(field, fc);
    }

    private void handleAuthTypeChanged(final ActionEvent e) {
        final boolean authFieldsEnabled =
                !authTypeComboBox.getItemAt(authTypeComboBox.getSelectedIndex()).equals("No auth");

        userField.setEnabled(authFieldsEnabled);
        passwordField.setEnabled(authFieldsEnabled);

        if (!authFieldsEnabled) {
            userField.setText("");
            passwordField.setText("");
        }
    }

    @Override
    public DataSourceApi constructDataSource() {
        final var data = extractData();

        Map<String, String> configuration = new HashMap<>();
        configuration.put(Neo4jBoltConfiguration.PROTOCOL, data.protocol);
        configuration.put(Neo4jBoltConfiguration.AUTH_TYPE, data.authType);
        configuration.put(Neo4jBoltConfiguration.HOST, data.host);
        configuration.put(Neo4jBoltConfiguration.PORT, data.port);
        configuration.put(Neo4jBoltConfiguration.USER, data.user);
        configuration.put(Neo4jBoltConfiguration.PASSWORD, data.password);
        configuration.put(Neo4jBoltConfiguration.DATABASE, data.database);

        return dataSourcesComponent.getDataSourceContainer().createDataSource(
                dataSourceToEdit,
                DataSourceType.NEO4J_BOLT,
                data.dataSourceName,
                configuration
        );
    }

    @Override
    protected void showLoading() {
        testConnectionButton.setEnabled(false);
        loadingIcon.resume();
        loadingPanel.setVisible(true);
    }

    @Override
    protected void hideLoading() {
        testConnectionButton.setEnabled(true);
        loadingIcon.suspend();
        loadingPanel.setVisible(false);
    }

    private Data extractData() {
        return new Data(
                dataSourceNameField.getText(),
                protocolComboBox.getItemAt(protocolComboBox.getSelectedIndex()),
                hostField.getText(),
                portField.getText(),
                authTypeComboBox.getItemAt(authTypeComboBox.getSelectedIndex()),
                databaseField.getText(),
                userField.getText(),
                String.valueOf(passwordField.getPassword())  // TODO: use password API?
        );
    }

    private static final class Data {

        private final String dataSourceName;
        private final String protocol;
        private final String host;
        private final String port;
        private final String authType;
        private final String database;
        private final String user;
        private final String password;

        public Data(String dataSourceName, String protocol, String host, String port, String authType, String database, String user, String password) {
            this.dataSourceName = dataSourceName;
            this.protocol = protocol;
            this.host = host;
            this.port = port;
            this.authType = authType;
            this.database = database;
            this.user = user;
            this.password = password;
        }
    }
}
