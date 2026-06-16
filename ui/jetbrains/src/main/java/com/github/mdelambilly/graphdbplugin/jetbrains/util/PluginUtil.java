/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.jetbrains.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PluginUtil {

    private static String version;

    public static String getVersion() {
        if (version == null) {
            try (InputStream is = PluginUtil.class.getResourceAsStream("/graphdb-plugin.properties")) {
                if (is != null) {
                    Properties props = new Properties();
                    props.load(is);
                    version = props.getProperty("version", "unknown");
                } else {
                    version = "unknown";
                }
            } catch (IOException e) {
                version = "unknown";
            }
        }
        return version;
    }

    public static boolean isEnabled() {
        return true;
    }
}
