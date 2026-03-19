/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.jetbrains.ui.helpers;

import com.intellij.ui.treeStructure.PatchedDefaultMutableTreeNode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UiHelperTest {

    @Test
    public void nullValueToTreeNode() throws Exception {
        PatchedDefaultMutableTreeNode treeNode = UiHelper.keyValueToTreeNode("key", "VALUE", null, null);
        assertThat(treeNode).isNotNull();
    }
}
