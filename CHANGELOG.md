# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.1.4] - 2026-06-16

### Fixed

- Replace internal API `PluginManager.getPlugin()` with a build-time resource file
  to retrieve the plugin version, removing all `PluginManager`/`PluginManagerCore` dependencies

## [1.1.3] - 2026-06-13

### Fixed

- Fix crash on first datasource open: wrap `ScratchFileService.findFile` in `WriteAction`
- Replace internal API `PluginManagerCore.getPlugin()` with public `PluginManager.getPlugin()`

## [1.1.2] - 2026-05-12

### Fixed

- Replace deprecated and removed IntelliJ APIs (`EditorFactory`, `BalloonPopupBuilderImpl`,
  `JsonParser.Feature`, `Charsets.UTF_8`, `ActionUtil`, `PluginDescriptor`)
- Fix crash when opening a datasource whose scratch file does not exist yet

## [1.1.1]

### Fixed

- Replace internal `JBTabsImpl` API with public `JBTabs` interface to comply with JetBrains Marketplace requirements

## [1.1.0]

### Added

- Full Cypher 5 / 2025+ grammar coverage for IntelliJ 2025.3+
- Quantified path patterns, parenthesized paths, and path selectors
  (ANY SHORTEST, ALL SHORTEST, SHORTEST n, GROUP)
- Label expressions: IS / & / | / ! syntax
- Complete GRANT / DENY / REVOKE privilege model with all Neo4j 5 privilege types
- Database administration: CREATE/ALTER/DROP/START/STOP DATABASE,
  CREATE/ALTER/DROP ALIAS, topology and wait clauses
- Extended CALL IN TRANSACTIONS: batch size (OF n ROW/ROWS),
  ON ERROR (CONTINUE/BREAK/FAIL), REPORT STATUS AS
- Server management: ENABLE/ALTER/DROP SERVER, SHOW SERVERS,
  DEALLOCATE/REALLOCATE DATABASES, DRYRUN
- TERMINATE TRANSACTIONS, RENAME ROLE/USER/SERVER

### Fixed

- Composite constraint property lists now parse correctly:
  `REQUIRE (p.name, p.email) IS NODE KEY`
- EXISTS { } and COUNT { } subqueries with a bare pattern form
  (no RETURN inside) no longer produce a spurious parse error

## [1.0.2]

### Fixed

- Fix compatibility with IntelliJ 2026.1: wrap `ScratchFileService.findFile()` calls in `ReadAction.compute()`

## [1.0.1]

### Fixed

- Cypher keyword auto-uppercase no longer triggers in non-Cypher files
  (e.g. Java string literals with injected Cypher language)
- Compatible with future IntelliJ builds beyond 2025.3 (removed `until-build` restriction)

## [1.0.0]

### Added
- Support for IntelliJ 2025.3 and Neo4j 5.26 LTS (fork by Michel de Lambilly)
- Auto-uppercase Cypher keywords as you type
- Raw JSON output tab in the Graph Database Console
- Theme-aware graph visualization (dark/light mode support)

## [0.8.0]

### Added
- Support for IntelliJ 2023.3

## [0.6.2]

### Added
- Support for IntelliJ 2023.2

## [0.6.1]

### Added
- Support for IntelliJ 2023.1

## [0.6.0]

### Added
- Support WHERE in pattern elements
- Allow numbers to be written in queries in scientific (exponential) notation

## [0.5.0]

### Added
- Add support for CALL subqueries (#20)

### Removed
- Remove unneeded hardcoded functions (#55)

## [0.4.2]

### Fixed

- Remove unnecessary aggregation on relationship types, which might fail metadata retrieval (#51)

## [0.4.1]

### Fixed

- Fail gracefully when retrieving function metadata for Neo4j 3.x (#51)

## [0.4.0]

### Added

- Support new syntax for CREATE INDEX commands (#45)
- Add scrollbars to new database panel (#44)

### Changed

Various internal improvements (e.g. using Testcontainers, enabling GitHub actions)
and refactorings.

## [0.3.0]

### Added

- Don't show Cypher autocompletion in comments (#4)
- Support existential and count subqueries (#24)
- Support connections to named databases

## [0.2.2]

### Fixed

- Fail gracefully when retrieving metadata for Neo4j 3.x (#21)

## [0.2.1]

### Fixed

- Renamed notification group for update activity (#16). This fixes possible NullPointerExceptions on IDE startup.

## [0.2.0]

### Added

- Upgraded Neo4j driver to 5.2.0
- Added support for current Bolt URI schemes (https://neo4j.com/docs/bolt/current/driver-api/#uri-schemes)
- Added authentication choice (either "No auth" or "User & Password") in the data source wizard.

### Fixed

- Fixed bugs related to application services being retrieved as project services.

## [0.1.4]

### Added

- Add support for IntelliJ 2022.3

## [0.1.3]

### Added

- First release of the Graph Database plugin, based on the "Graph Database Support" plugin developed by Neueda Technologies, Ltd.

### Removed

- Support for Gremlin data sources (such as CosmosDB)
- Tracking via Google analytics

This plugin is based on the 'Graph Database Support plugin' developed by Neueda Technologies, Ltd. The original plugin has not been updated in a long time.
This plugin has been updated to run with modern versions of IntelliJ and related IDEs. Just like the original plugin, it supports Neo4j and Cypher data sources; however, support for Gremlin data sources (such as Cosmos DB) has been dropped.