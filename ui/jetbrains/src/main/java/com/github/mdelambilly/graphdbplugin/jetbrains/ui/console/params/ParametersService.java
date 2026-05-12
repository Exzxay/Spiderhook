/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.jetbrains.ui.console.params;

import com.github.mdelambilly.graphdbplugin.language.cypher.psi.impl.CypherParameterImpl;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.common.base.Throwables;
import com.intellij.psi.PsiElement;
import com.github.mdelambilly.graphdbplugin.language.cypher.psi.CypherTypes;
import com.github.mdelambilly.graphdbplugin.language.cypher.util.TraverseUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ParametersService {

    private static final ObjectMapper MAPPER = JsonMapper.builder()
            .enable(JsonReadFeature.ALLOW_SINGLE_QUOTES)
            .enable(JsonReadFeature.ALLOW_JAVA_COMMENTS)
            .enable(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES)
            .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
            .enable(JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS)
            .build();

    private static final JsonFactory FACTORY = MAPPER.getFactory();

    private ParametersProvider parametersProvider;

    public boolean isParametersProviderRegistered() {
        return parametersProvider != null;
    }

    public void registerParametersProvider(final ParametersProvider parametersProvider) {
        if (isParametersProviderRegistered()) {
            throw new IllegalStateException("Parameters provider already registered");
        }
        this.parametersProvider = parametersProvider;
    }

    public Map<String, Object> getParameters(PsiElement element) throws Exception {
        if (element == null) {
            return Collections.emptyMap();
        }
        Map<String, Object> allParameters = new HashMap<>();
        if (isValidParametersMap(parametersProvider.getFileSpecificParametersJson())) {
            Map<String, Object> parsedFileSpecific = MAPPER.readValue(parametersProvider.getFileSpecificParametersJson(),
                    new TypeReference<Map<String, Object>>() {
                    });
            parsedFileSpecific.forEach(allParameters::putIfAbsent);
        }
        if (isValidParametersMap(parametersProvider.getGlobalParametersJson())) {
            Map<String, Object> parsedGlobal = MAPPER.readValue(parametersProvider.getGlobalParametersJson(),
                    new TypeReference<Map<String, Object>>() {
                    });
            parsedGlobal.forEach(allParameters::putIfAbsent);
        }
        return extractQueryParameters(element, allParameters);
    }

    private Map<String, Object> extractQueryParameters(PsiElement element, Map<String, Object> allParameters) {
        Set<String> parameterNames = extractParameterNames(element);
        if (parameterNames.isEmpty()) {
            return Collections.emptyMap();
        }

        return allParameters.entrySet().stream()
                .filter(entry -> parameterNames.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Set<String> extractParameterNames(PsiElement element) {
        List<PsiElement> parameterElements = TraverseUtil.collectPsiElementsByType(element, CypherTypes.PARAMETER);
        return parameterElements.stream()
                .map(elem -> ((CypherParameterImpl) elem).getParameterName())
                .collect(Collectors.toSet());
    }

    private static boolean isEmptyParametersMap(String parametersJson) {
        try {
            return parametersJson == null || StringUtils.isBlank(parametersJson);
        } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
            throw new RuntimeException(e);
        }
    }

    private static boolean isValidParametersMap(String parametersJson) {
        try {
            if (parametersJson == null || StringUtils.isBlank(parametersJson)) {
                return false;
            }

            JsonParser parser = FACTORY.createParser(parametersJson);
            JsonNode node = MAPPER.readTree(parser);
            if (node == null) {
                return false;
            }
        } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
            throw new RuntimeException(e);
        }

        return true;
    }

}
