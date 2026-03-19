# CLAUDE.md — graphdb-intellij-plugin

## Project
IntelliJ IDEA plugin for graph databases (Neo4j).
Provides: Cypher syntax highlighting, auto-completion, database connection and querying.

## Migration goal
Migrate the plugin from IntelliJ 2023.3 to **IntelliJ 2025.3.3** (build 253.31033.145)
and ensure compatibility with **Neo4j 5.26 LTS**.

## Environment
- OS: **Windows** with **PowerShell 7.x**
- Gradle Wrapper: use `.\gradlew.bat` (not `./gradlew`)

## Tech stack
- Language: Java 21
- Build: Gradle 9.4.0 with IntelliJ Platform Gradle Plugin **2.13.1**
- Gradle Plugin ID: `org.jetbrains.intellij.platform` (NOT `org.jetbrains.intellij`)
- IntelliJ submodules: use `org.jetbrains.intellij.platform.module`
- Neo4j Driver: `org.neo4j.driver:neo4j-java-driver:5.26.2`
- Tests: JUnit 5, Testcontainers (image `neo4j:5.26-community`)

## Module structure

```
database\                → Neo4j access (driver), NO IntelliJ dependency
language\                → Cypher parser, PSI, syntax highlighting (depends on IntelliJ API)
platform\                → Shared services, settings (depends on IntelliJ API)
ui\                      → ToolWindow, graph/table views (depends on IntelliJ API)
testing\                 → Test utilities, Testcontainers
graph-database-plugin\   → Main plugin module, assembles everything, produces the final ZIP
```

## Conventions
- **All content in project files (source code, build files, config files, comments, commit messages) MUST be written in English. No exceptions.**
- NEVER suggest reverting to Gradle IntelliJ Plugin 1.x
- Use Groovy DSL (not Kotlin DSL) for build.gradle files
- Centralized properties live in `gradle.properties`
- One fix at a time, clearly explained
- When fixing a deprecated API, always cite the replacement API with its full import
- Do not modify the plugin's business logic unless explicitly asked
- Shell commands must be compatible with **PowerShell 7.x on Windows**
- **NEVER commit without an explicit test approval from the user.** For any feature or fix involving UI, editor behavior, or runtime plugin loading: propose launching `runIde`, wait for the user to confirm the behavior works, then commit.

## Project properties (gradle.properties)
- `platformType = IC`
- `platformVersion = 2025.3.3`
- `sinceBuild = 253`
- `untilBuild = 253.*`
- `javaVersion = 21`
- `neo4jDriverVersion = 5.26.2`

## Useful commands

```powershell
.\gradlew.bat :graph-database-plugin:buildPlugin    # Build the plugin
.\gradlew.bat test                                   # Run tests
.\gradlew.bat :graph-database-plugin:verifyPlugin   # Verify compatibility
.\gradlew.bat :graph-database-plugin:runIde         # Launch IntelliJ sandbox
```

## IntelliJ Platform context
- since-build = 253
- until-build = 253.*
- IntelliJ Platform imports come from the SDK, not Maven Central
- `testFramework()` is only available in `org.jetbrains.intellij.platform` (main plugin module), NOT in `org.jetbrains.intellij.platform.module` submodules
- JUnit 5 runtime in submodules requires adding `junit:junit:4.13.2` as `testRuntimeOnly` (used internally by the IJPGP test executor — our tests remain JUnit 5)
- `junit-platform-launcher` must be declared explicitly as `testRuntimeOnly` in Gradle 9
- The `database` module is a plain Java module — it does NOT use the IntelliJ plugin
- The `language`, `platform`, `ui` modules use `org.jetbrains.intellij.platform.module`
- The `graph-database-plugin` module uses `org.jetbrains.intellij.platform` (the main plugin)

## Expected behavior

### Long context management
Your context window will be automatically compacted as it approaches its
limit, allowing you to continue working indefinitely from where you left off.
Therefore, do not stop tasks early due to token budget concerns. As you
approach your token budget limit, save your current progress and state to a
progress.md file before the context window refreshes. Always be as persistent
and autonomous as possible and complete tasks fully, even if the end of your
budget approaches. Never artificially stop a task early regardless of
remaining context.

### Default to action
Before making any code change, briefly explain what you intend to do and why,
then proceed without waiting for confirmation — unless the change is risky or
irreversible (see Autonomy and safety below).

If the user's intent is unclear, ask a focused clarifying question rather than
guessing or inferring. Do not ask multiple questions at once — identify the
single most important unknown and ask only that.

### Autonomy and safety
Consider the reversibility and potential impact of your actions. You are
encouraged to take local and reversible actions like editing files or running
tests, but for actions that are hard to reverse, affect shared systems, or
could be destructive, ask the user before proceeding.

Examples of actions that require confirmation:
- Deleting files or branches
- git push --force, git reset --hard
- Modifying published commits

### No over-engineering
Avoid over-engineering. Only make changes that are directly requested or
clearly necessary. Keep solutions simple and focused. A bug fix does not need
cleanup of surrounding code. Do not refactor code, add features, or add
documentation beyond what is asked.

### No hallucination
Never speculate about code you haven't opened. If the user references a
specific file, you MUST read the file before responding. Never make claims
about code before investigating.

### Verbosity
After completing a task involving tool use, provide a brief summary of the
work done. No long summaries — just what changed and why.

### Parallel tool calls
If you intend to call multiple tools and there are no dependencies between
the calls, make all independent calls in parallel. For example, when reading
3 files, read them all at the same time.
