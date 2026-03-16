/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.albertoventurini.graphdbplugin.language.cypher.util;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;

/**
 * TODO: Description
 *
 * @author dmitry@vrublevsky.me
 */
public final class PsiUtil {

    private PsiUtil() {
    }

    public static TextRange rangeFrom(PsiElement element) {
        return new TextRange(0, element.getText().length());
    }
}
