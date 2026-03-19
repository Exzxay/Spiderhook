/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.jetbrains.actions.execute;

public class ProfileQueryAction extends ExecuteQueryAction {

    @Override
    protected String decorateQuery(String query) {
        return "PROFILE " + super.decorateQuery(query);
    }
}
