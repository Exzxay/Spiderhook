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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
    private JTextArea auraCredentialsArea;
    private JBTabbedPane tabs;

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
        setupHostProtocolStripper();
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
        protocolComboBox.setMaximumRowCount(6);
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
        generalPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
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

        // --- AuraDB tab ---
        auraCredentialsArea = new JTextArea();
        auraCredentialsArea.setRows(8);
        auraCredentialsArea.setLineWrap(true);
        auraCredentialsArea.setToolTipText("Paste the credentials file provided by Neo4j AuraDB");
        JScrollPane auraScroll = new JScrollPane(auraCredentialsArea);

        JButton fillButton = new JButton("Fill from credentials");
        fillButton.addActionListener(e -> fillFromAuraCredentials());

        JLabel auraHint = new JLabel("<html>Paste the credentials file provided by AuraDB<br>and click \"Fill from credentials\".</html>");

        JPanel auraPanel = new JPanel(new GridBagLayout());
        GridBagConstraints ac = new GridBagConstraints();
        ac.gridx = 0; ac.gridy = 0; ac.weightx = 1.0; ac.fill = GridBagConstraints.HORIZONTAL;
        ac.insets = new Insets(8, 8, 4, 8);
        auraPanel.add(auraHint, ac);
        ac.gridy = 1; ac.weighty = 1.0; ac.fill = GridBagConstraints.BOTH;
        ac.insets = new Insets(0, 8, 4, 8);
        auraPanel.add(auraScroll, ac);
        ac.gridy = 2; ac.weighty = 0.0; ac.fill = GridBagConstraints.NONE;
        ac.anchor = GridBagConstraints.EAST;
        ac.insets = new Insets(0, 8, 8, 8);
        auraPanel.add(fillButton, ac);

        tabs = new JBTabbedPane();
        tabs.addTab("General", generalPanel);
        tabs.addTab("AuraDB", auraPanel);

        // --- Content panel (inside scroll pane) ---
        JPanel content = new JPanel(new GridBagLayout());
        content.setMinimumSize(new Dimension(400, 380));
        content.setPreferredSize(new Dimension(600, 460));

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

    private void setupHostProtocolStripper() {
        hostField.getDocument().addDocumentListener(new DocumentListener() {
            private boolean updating = false;

            private void stripProtocol() {
                if (updating) return;
                String text = hostField.getText();
                for (int i = 0; i < protocolComboBox.getItemCount(); i++) {
                    String scheme = protocolComboBox.getItemAt(i) + "://";
                    if (text.startsWith(scheme)) {
                        updating = true;
                        final int idx = i;
                        SwingUtilities.invokeLater(() -> {
                            hostField.setText(text.substring(scheme.length()));
                            protocolComboBox.setSelectedIndex(idx);
                            updating = false;
                        });
                        break;
                    }
                }
            }

            @Override public void insertUpdate(DocumentEvent e) { stripProtocol(); }
            @Override public void removeUpdate(DocumentEvent e) {}
            @Override public void changedUpdate(DocumentEvent e) {}
        });
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

    private void fillFromAuraCredentials() {
        String text = auraCredentialsArea.getText();
        String uri = null, username = null, password = null, database = null, instanceName = null;

        for (String line : text.lines().toList()) {
            line = line.trim();
            if (line.startsWith("#") || line.isEmpty()) continue;
            int eq = line.indexOf('=');
            if (eq < 0) continue;
            String key = line.substring(0, eq).trim();
            String value = line.substring(eq + 1).trim();
            switch (key) {
                case "NEO4J_URI"      -> uri = value;
                case "NEO4J_USERNAME" -> username = value;
                case "NEO4J_PASSWORD" -> password = value;
                case "NEO4J_DATABASE" -> database = value;
                case "AURA_INSTANCENAME" -> instanceName = value;
            }
        }

        if (uri != null) {
            int schemeSep = uri.indexOf("://");
            if (schemeSep >= 0) {
                String scheme = uri.substring(0, schemeSep);
                String host = uri.substring(schemeSep + 3);
                for (int i = 0; i < protocolComboBox.getItemCount(); i++) {
                    if (protocolComboBox.getItemAt(i).equals(scheme)) {
                        protocolComboBox.setSelectedIndex(i);
                        break;
                    }
                }
                hostField.setText(host);
            }
        }
        if (username != null) userField.setText(username);
        if (password != null) passwordField.setText(password);
        if (database != null) databaseField.setText(database);
        if (dataSourceNameField.getText().isBlank() && instanceName != null) {
            dataSourceNameField.setText(instanceName);
        }
        // AuraDB uses port 7687 by default
        portField.setText("7687");
        // Switch back to General tab to let the user review
        tabs.setSelectedIndex(0);
    }

    private Data extractData() {
        return new Data(
                dataSourceNameField.getText(),
                protocolComboBox.getItemAt(protocolComboBox.getSelectedIndex()),
                hostField.getText().trim(),
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
