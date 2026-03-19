/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.language.cypher.editor;

import com.github.mdelambilly.graphdbplugin.language.cypher.completion.metadata.atoms.CypherKeywords;
import com.github.mdelambilly.graphdbplugin.language.cypher.psi.CypherStringLiteral;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class CypherTypedHandlerDelegate extends TypedHandlerDelegate {

    @NotNull
    @Override
    public Result checkAutoPopup(char charTyped, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        final int offset = editor.getCaretModel().getOffset() - 1;
        if (offset >= 0) {
            final PsiElement element = file.findElementAt(offset);
            if (element instanceof CypherStringLiteral) {
                return Result.STOP;
            }
        }
        return charTyped == ':'
                || charTyped == '.'
                || charTyped == '('
                || charTyped == '[' ? Result.CONTINUE : Result.STOP;
    }

    @NotNull
    @Override
    public Result charTyped(char c, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        if (c != ' ' && c != '\n' && c != '\t' && c != '(' && c != ')' && c != ',' && c != '[' && c != '{') {
            return Result.CONTINUE;
        }

        int offset = editor.getCaretModel().getOffset();
        if (offset < 2) {
            return Result.CONTINUE;
        }

        // The delimiter was inserted at offset - 1; the word ends just before it
        int wordEnd = offset - 1;
        Document document = editor.getDocument();
        CharSequence text = document.getCharsSequence();

        int wordStart = wordEnd;
        while (wordStart > 0 && Character.isLetter(text.charAt(wordStart - 1))) {
            wordStart--;
        }

        if (wordStart == wordEnd) {
            return Result.CONTINUE;
        }

        // Skip if inside a string literal
        PsiElement element = file.findElementAt(wordStart);
        PsiElement parent = element;
        while (parent != null) {
            if (parent instanceof CypherStringLiteral) {
                return Result.CONTINUE;
            }
            parent = parent.getParent();
        }

        String word = text.subSequence(wordStart, wordEnd).toString();
        String upper = word.toUpperCase();

        if (!word.equals(upper) && CypherKeywords.KEYWORDS.contains(upper)) {
            final int start = wordStart;
            final int end = wordEnd;
            WriteCommandAction.runWriteCommandAction(project, () ->
                    document.replaceString(start, end, upper)
            );
        }

        return Result.CONTINUE;
    }
}
