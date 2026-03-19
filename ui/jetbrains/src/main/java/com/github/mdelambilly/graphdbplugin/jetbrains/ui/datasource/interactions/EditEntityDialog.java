/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.jetbrains.ui.datasource.interactions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Disposer;
import com.github.mdelambilly.graphdbplugin.database.api.data.GraphEntity;
import com.github.mdelambilly.graphdbplugin.database.api.data.GraphNode;
import com.github.mdelambilly.graphdbplugin.database.api.data.GraphPropertyContainer;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class EditEntityDialog extends DialogWrapper {

    private final ObjectMapper mapper = new ObjectMapper();

    private JLabel addLabel;
    private JLabel addProperty;
    private JPanel propertyContainer;
    private JPanel labelContainer;
    private JLabel nodeLabel;
    private JPanel container;
    private JPanel labelsPanel;

    private GraphEntity node;

    public EditEntityDialog(Project project, GraphEntity node) {
        super(project);
        this.node = node;

        boolean isCreateMode = node == null;
        boolean isNodeEdit = isCreateMode || node instanceof GraphNode;

        setupUI();

        Disposer.register(project, myDisposable);
        init();

        setTitle(isCreateMode, isNodeEdit);

        addLabel.setIcon(AllIcons.General.Add);
        addLabel.setBorder(BorderFactory.createEmptyBorder());

        addProperty.setIcon(AllIcons.General.Add);
        addProperty.setBorder(BorderFactory.createEmptyBorder());

        if (isNodeEdit) {
            labelContainer.setLayout(new BoxLayout(labelContainer, BoxLayout.Y_AXIS));
            if (!isCreateMode) {
                node.getTypes()
                        .forEach(type -> labelContainer.add(createRemovableInput(type, labelContainer)));
            }
            addLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    labelContainer.add(createRemovableInput(null, labelContainer));
                    labelContainer.revalidate();
                    validate();
                }
            });
        } else {
            labelContainer.setVisible(false);
            addLabel.setVisible(false);
            labelsPanel.setVisible(false);
        }

        propertyContainer.setLayout(new BoxLayout(propertyContainer, BoxLayout.Y_AXIS));
        if (!isCreateMode) {
            node.getPropertyContainer().getProperties()
                    .forEach((key, value) ->
                            propertyContainer.add(createRemovablePairInputs(key, value.toString(), propertyContainer)));
        }
        addProperty.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                propertyContainer.add(createRemovablePairInputs(null, null, propertyContainer));
                propertyContainer.revalidate();
                validate();
            }
        });
    }

    private void setupUI() {
        // Main container — 3 rows: nodeLabel, labelsPanel, propertiesSection
        container = new JPanel(new GridBagLayout());
        container.setMinimumSize(new Dimension(400, 300));
        container.setPreferredSize(new Dimension(600, 400));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);

        // Row 0: nodeLabel
        nodeLabel = new JLabel("Node [id]");
        gbc.gridy = 0;
        gbc.weighty = 0.0;
        container.add(nodeLabel, gbc);

        // Row 1: labelsPanel (Labels header + scrollable labelContainer)
        labelsPanel = buildSectionPanel("Labels", true);
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        container.add(labelsPanel, gbc);

        // Row 2: propertiesSection (Properties header + scrollable propertyContainer)
        JPanel propertiesSection = buildSectionPanel("Properties", false);
        gbc.gridy = 2;
        container.add(propertiesSection, gbc);
    }

    /**
     * Builds a section panel with a header row (title label + add label) and a scrollable content panel.
     * @param title  section title
     * @param isLabels  true → assigns labelsPanel fields; false → assigns propertiesSection fields
     */
    private JPanel buildSectionPanel(String title, boolean isLabels) {
        JPanel section = new JPanel(new GridBagLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(!isLabels); // labelContainer is opaque=false in original form
        contentPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JLabel addIconLabel = new JLabel("");

        if (isLabels) {
            labelContainer = contentPanel;
            addLabel = addIconLabel;
        } else {
            propertyContainer = contentPanel;
            addProperty = addIconLabel;
        }

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.insets = new Insets(0, 0, 0, 0);

        // Header row: title label (col 0) + add icon label (col 1)
        c.gridy = 0;
        c.gridx = 0;
        section.add(new JLabel(title), c);
        c.gridx = 1;
        section.add(addIconLabel, c);

        // Scrollable content row
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(-1, 200));
        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        section.add(scrollPane, c);

        return section;
    }

    private void setTitle(boolean isCreateNode, boolean isEditNode) {
        if (isCreateNode) {
            setTitle("Create New Node");
            nodeLabel.setText("Node [?]");
        } else {
            setTitle(isEditNode ? "Edit Node" : "Edit Relationship");
            nodeLabel.setText(node.getRepresentation());
        }
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return container;
    }

    private Component createRemovableInput(String initialData, Container parent) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        JTextField textField = new JTextField(initialData);
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(textField);

        container.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                textField.setPreferredSize(new Dimension(e.getComponent().getWidth() - 30, 30));
                panel.revalidate();
            }
        });

        addCloseLabel(parent, panel);
        return panel;
    }

    private Component createRemovablePairInputs(String key, String value, Container parent) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        JTextField keyTextField = new JTextField(key);
        keyTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(keyTextField);

        JTextField valueTextField = new JTextField(value);
        valueTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(valueTextField);

        container.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                int width = (e.getComponent().getWidth() - 30) / 2;
                keyTextField.setPreferredSize(new Dimension(width, 30));
                valueTextField.setPreferredSize(new Dimension(width, 30));
                panel.revalidate();
            }
        });

        addCloseLabel(parent, panel);
        return panel;
    }

    private void addCloseLabel(Container parent, JPanel panel) {
        JLabel remove = new JLabel(AllIcons.Actions.Close);
        remove.setBorder(BorderFactory.createEmptyBorder());
        panel.add(remove);

        remove.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                parent.remove(panel);
                parent.revalidate();
                validate();
                repaint();
            }
        });
    }

    public GraphEntity getUpdatedEntity() {
        return new GraphEntity() {
            @Override
            public String getRepresentation() {
                return EditEntityDialog.this.node.getRepresentation();
            }

            @Override
            public String getId() {
                return EditEntityDialog.this.node.getId();
            }

            @Override
            public GraphPropertyContainer getPropertyContainer() {
                return () -> Stream.of(EditEntityDialog.this.propertyContainer.getComponents())
                        .map(Container.class::cast)
                        .filter(c -> !Strings.isNullOrEmpty(((JTextField) c.getComponent(0)).getText()))
                        .map(cont -> new AbstractMap.SimpleEntry<>(
                                ((JTextField) cont.getComponent(0)).getText(),
                                readValue((JTextField) cont.getComponent(1))))
                        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
            }

            @Override
            public List<String> getTypes() {
                return Stream.of(EditEntityDialog.this.labelContainer.getComponents())
                        .map(Container.class::cast)
                        .map(cont -> cont.getComponent(0))
                        .map(JTextField.class::cast)
                        .map(JTextField::getText)
                        .collect(toList());
            }

            @Override
            public String getTypesName() {
                return EditEntityDialog.this.node.getTypesName();
            }

            @Override
            public boolean isTypesSingle() {
                return EditEntityDialog.this.node.isTypesSingle();
            }
        };
    }

    private Object readValue(JTextField field) {
        String text = field.getText();

        try {
            JsonNode jsonNode = mapper.readTree(text);
            return readValue(jsonNode);
        } catch (IOException e) {
            e.printStackTrace();

            return text;
        }
    }

    private Object readValue(JsonNode node) {
        if (node.isFloatingPointNumber()) {
            return node.asDouble();
        } else if (node.isNumber()) {
            return node.asLong();
        } else if (node.isBoolean()) {
            return node.asBoolean();
        } else if (node.isTextual()) {
            return node.asText();
        } else if (node.isArray()) {
            return StreamSupport.stream(node.spliterator(), false)
                    .map(this::readValue)
                    .collect(toList());
        }

        return node.toString();
    }
}
