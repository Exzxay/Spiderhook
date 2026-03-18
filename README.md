# Graph Database plugin for IntelliJ Platform

![Build and test](https://github.com/mdelambilly/graphdb-intellij-plugin/actions/workflows/build-plugin.yaml/badge.svg)

This plugin adds support for graph databases to the IntelliJ IDEA platform.
At the moment, the only supported database is Neo4j.

The plugin provides useful developer features for working with graph databases, such as:
* syntax highlighting and autocompletion for 
the [Cypher](https://opencypher.org/) query language
* connecting to and querying local and remote graph databases.

![Screenshot 2022-11-21 at 09 08 11](https://user-images.githubusercontent.com/5089391/202986692-78fbc25b-2d60-42bc-a746-d67db1da72b6.png)

This plugin is maintained and developed in my free time as a personal project,
for no commercial reasons.
Contributions and donations are welcome! Please see the [Getting involved](#getting-involved)
section for more information on how to contribute to this project.

This plugin is based on the
[Graph Database Support](https://github.com/neueda/jetbrains-plugin-graph-database-support)
plugin, originally developed by [Neueda Technologies](http://technologies.neueda.com/).
It was then forked and maintained by [Alberto Venturini](https://github.com/albertoventurini/graphdb-intellij-plugin),
whose repository is now archived.
This fork continues the work with support for IntelliJ 2025.3 and Neo4j 5.26 LTS.

## Installation

The plugin can be installed directly within an IntelliJ IDEA-based IDE.

1. Go to `Preferences` -> `Plugins` -> `Marketplace`
2. Search for `Graph Database`.
3. Install plugin and restart the IDE.

Alternatively, you can navigate to the [plugin homepage](https://plugins.jetbrains.com/plugin/20417-graph-database)
on the JetBrains marketplace and click on the 'Install' button.

## Usage

### Data Sources

After the plugin is installed, you should configure a data source. In order to do that,
expand the "Graph Database" tab on the right-hand side of the IDE, then click on the "+" symbol.
Fill in the fields, and test that the connection works.

### Cypher syntax highlighting

The plugin activates syntax highlighting for Cypher for files with extensions `.cyp`, `.cypher`, or `.cql`.

It is also possible to add syntax highlighting to Java Strings containing Cypher queries
via [language injection](https://www.jetbrains.com/help/idea/using-language-injections.html).
Simply add the following comment right before a Cypher String:
```java
// language=Cypher
```

## Building the plugin

### Prerequisites

- **JDK 21** (e.g. [Eclipse Temurin](https://adoptium.net/))
- **IntelliJ IDEA 2025.3** or later
- **Docker** — required for integration tests (Neo4j runs in a [Testcontainer](https://www.testcontainers.org/))

If you plan on making changes to grammar or lexer files, you may find
it useful to install the [Grammar-Kit](https://github.com/JetBrains/Grammar-Kit)
IntelliJ plugin. This is not required but nice to have.

### Build instructions

On Linux/macOS:

```shell
./gradlew :graph-database-plugin:buildPlugin
```

On Windows (PowerShell):

```powershell
.\gradlew.bat :graph-database-plugin:buildPlugin
```

Before a release, the plugin should also be tested and verified:

```shell
./gradlew test
./gradlew :graph-database-plugin:verifyPlugin
```

The plugin can also be manually tested in a sandboxed instance of IntelliJ:

```shell
./gradlew :graph-database-plugin:runIde
```

## Getting help

- For bugs, please [open an issue](https://github.com/mdelambilly/graphdb-intellij-plugin/issues/new/choose).
- For questions or general discussion, use [GitHub Discussions](https://github.com/mdelambilly/graphdb-intellij-plugin/discussions).

## Getting involved

Contributions are welcome — feel free to open a pull request!

## Contacts

For any questions or feedback, please use [GitHub Discussions](https://github.com/mdelambilly/graphdb-intellij-plugin/discussions).
