/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.jetbrains.util;

import com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.state.DataSourceApi;
import com.github.mdelambilly.graphdbplugin.platform.GraphConstants;

public final class NameUtil {

    public static String createDataSourceFileName(DataSourceApi dataSource) {
        return GraphConstants.BOUND_DATA_SOURCE_PREFIX + dataSource.getUUID() + "." + dataSource.getDescription().getDefaultFileExtension();
    }

    public static String extractDataSourceUUID(String fileName) {
        int beginIndex = GraphConstants.BOUND_DATA_SOURCE_PREFIX.length();
        int endIndex = beginIndex + 36;
        return fileName.substring(beginIndex, endIndex);
    }
}
