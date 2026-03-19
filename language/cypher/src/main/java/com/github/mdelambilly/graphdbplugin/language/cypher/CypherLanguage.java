/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.language.cypher;

import com.intellij.lang.Language;

import static com.github.mdelambilly.graphdbplugin.platform.SupportedLanguage.CYPHER;

/**
 * @author dmitry@vrublesvky.me
 */
public final class CypherLanguage extends Language {

    public static final CypherLanguage INSTANCE = new CypherLanguage();

    private CypherLanguage() {
        super(CYPHER.getLanguageId());
    }
}
