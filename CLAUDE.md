# CLAUDE.md — graphdb-intellij-plugin

## Projet
Plugin IntelliJ IDEA pour les bases de données graphe (Neo4j).
Fournit : coloration syntaxique Cypher, auto-complétion, connexion et requêtage de BDD graphe.

## Objectif de migration
Adapter le plugin de IntelliJ 2023.3 vers **IntelliJ 2025.3.3** (build 253.31033.145)
et assurer la compatibilité avec **Neo4j 5.26 LTS**.

## Environnement
- OS : **Windows** avec **PowerShell 7.x**
- Gradle Wrapper : utiliser `.\gradlew.bat` (pas `./gradlew`)

## Stack technique
- Langage : Java 21
- Build : Gradle 9.4.0 avec IntelliJ Platform Gradle Plugin **2.13.0**
- Plugin ID Gradle : `org.jetbrains.intellij.platform` (PAS `org.jetbrains.intellij`)
- Sous-modules IntelliJ : utilisent `org.jetbrains.intellij.platform.module`
- Driver Neo4j : `org.neo4j.driver:neo4j-java-driver:5.26.2`
- Tests : JUnit 5, Testcontainers (image `neo4j:5.26-community`)

## Structure des modules

```
database\                → Accès Neo4j (driver), PAS de dépendance IntelliJ
language\                → Parseur Cypher, PSI, coloration (dépend de l'API IntelliJ)
platform\                → Services partagés, settings (dépend de l'API IntelliJ)
ui\                      → ToolWindow, vues graphe/table (dépend de l'API IntelliJ)
testing\                 → Utilitaires de test, Testcontainers
graph-database-plugin\   → Module principal, assemble tout, produit le plugin ZIP
```

## Conventions
- Ne propose JAMAIS de revenir au Gradle IntelliJ Plugin 1.x
- Utilise la DSL Groovy (pas Kotlin DSL) pour les build.gradle
- Les propriétés centralisées sont dans `gradle.properties`
- Chaque correction = un seul problème à la fois, bien expliqué
- Quand tu corriges une API dépréciée, cite la nouvelle API et son import complet
- Ne modifie pas la logique métier du plugin sans demande explicite
- Les commandes shell doivent être compatibles **PowerShell 7.x Windows**

## Propriétés du projet (gradle.properties)
- `platformVersion = 2025.3.3`
- `sinceBuild = 253`
- `untilBuild = 253.*`
- `javaVersion = 21`
- `neo4jDriverVersion = 5.26.2`

## Commandes utiles

```powershell
.\gradlew.bat :graph-database-plugin:buildPlugin    # Build le plugin
.\gradlew.bat test                                   # Tests
.\gradlew.bat :graph-database-plugin:verifyPlugin   # Vérification compatibilité
.\gradlew.bat :graph-database-plugin:runIde         # Lance IntelliJ sandbox
```

## Contexte IntelliJ Platform
- since-build = 253
- until-build = 253.*
- Les imports IntelliJ Platform sont dans le SDK, pas dans Maven Central
- Depuis IntelliJ 2025.3, utiliser `intellijIdea(version)` (l'artifact `ideaIC` n'est plus publié)
- `testFramework()` et `instrumentationTools()` ont été supprimés du plugin 2.x — ne pas les utiliser
- Le module `database` est un module Java pur, il n'utilise PAS le plugin IntelliJ
- Les modules `language`, `platform`, `ui` utilisent `org.jetbrains.intellij.platform.module`
- Le module `graph-database-plugin` utilise `org.jetbrains.intellij.platform` (le plugin principal)