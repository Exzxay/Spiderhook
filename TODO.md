# GraphDB Plugin — TODO

## Compatibility

### Replace `PluginManager.getPlugin()` in PluginUtil (internal + deprecated in 2026.2 EAP)

Reported by Plugin Verifier against IntelliJ 2026.2 EAP (262.7581.18): `PluginManager.getPlugin(PluginId)`
is both internal and deprecated. Our fix in v1.1.3 replaced `PluginManagerCore.getPlugin()` with
`PluginManager.getPlugin()`, but both are being phased out in 2026.2.

File: `ui/jetbrains/src/main/java/.../util/PluginUtil.java`

Needs investigation: find the correct public, non-deprecated API to retrieve an `IdeaPluginDescriptor`
from within the plugin itself (since `sinceBuild = 253`). Candidate approaches:
- `PluginClassLoader` from the current classloader (may be internal)
- A Gradle-filtered `plugin.properties` resource file containing the version
- Any new public API introduced in 2025.3+ for self-referencing plugin metadata

---

### Verify compatibility with PyCharm

Neo4j is widely used in Python (graph analytics, NLP, RAG pipelines). Add PyCharm to the
`pluginVerification` IDE list in `graph-database-plugin/build.gradle` to verify the plugin
works in PyCharm, then update the `sinceBuild` / compatible products accordingly if successful.

```groovy
pluginVerification {
    ides {
        recommended()
        ide("PY", "2025.3")
    }
}
```

---

## Bugs

### AuraDB — database version not displayed correctly

The version shown in the data source panel is incorrect or missing when connected to AuraDB.
Likely caused by a change in the version reporting format between Neo4j 5.x and Neo4j 2025+
(formerly Neo4j 2026.x). The `VersionService` may be parsing a version string format that
no longer matches what the server returns.

To investigate: check what `RETURN neo4j.version()` (or the driver's `ServerInfo`) returns
on an AuraDB instance and compare with the expected format in `VersionService`.

### Parameters saved to global scope instead of per-connection

When adding parameters for a specific database/connection, they end up stored in
the global parameter list instead. The per-connection parameter list then disappears.

- Parameters should be scoped to the connection they were defined for
- The per-connection parameter list should remain visible and editable independently
  from the global list

### Undo (Ctrl+Z) — executed query is the pre-undo version

After undoing changes with Ctrl+Z, executing a query runs the version that was just
removed by the undo, not the current visible content. The plugin likely caches the
query text and does not refresh it when the document changes via undo. The document
listener (or PSI change listener) may not fire on undo events.

### Cypher syntax highlighting intermittently broken

String syntax highlighting stops working occasionally — the fix is to remove and re-type
one of the string delimiters to force re-parsing. Likely a lexer/incremental parsing issue
where the editor state gets out of sync.

### Missing run gutter icon on some valid queries

Some valid Cypher queries do not get a run gutter icon. Known examples:

- `MERGE (p:Person {name: 'Bob'}) ON CREATE SET p.created = date() ON MATCH SET p.updated = date()`
- `MATCH (n:Person&Employee) RETURN n`

The second example uses a label expression (`&`) which may not be recognized as a top-level
statement by the line marker provider. Root cause unclear — likely the PSI structure produced
for these queries is not matched by `CypherLineMarkerProvider`.

### Multiple Cypher queries: only the first one gets a run gutter icon

When adding a new Cypher query at the top of a file, a run button (play icon) appears
correctly in the gutter next to it, but all subsequent queries in the file lose their
run buttons. Adding a query at the end of the file works correctly. The issue is specific
to prepending — likely an offset/range calculation bug when re-indexing existing queries.

### Database selection popup positioning

The popup for choosing the target database appears at an inconsistent/unexpected
position on screen. It should appear anchored to the element that triggered it
(toolbar button, editor gutter, etc.).

### ALTER CURRENT USER SET PASSWORD — SET consumed as username

`ALTER CURRENT USER SET PASSWORD 'newpass'` produces `SET expected, got 'PASSWORD'`.

Root cause: `AlterUser ::= K_ALTER K_CURRENT? K_USER SymbolicNameString? (SetPassword | SetStatus)+`.
The `SymbolicNameString?` optional username is greedy — `SET` is a `ReservedWord` and therefore
a valid `SymbolicNameString`, so the parser consumes it as the username. The mandatory `SET`
required by `SetPassword` is then missing, and `PASSWORD` is unexpected.

Fix: add a negative lookahead on `SymbolicNameString?` to exclude `K_SET` and `K_STATUS`,
or change the rule to require that the username is not a clause-starting keyword:

```bnf
AlterUser ::= K_ALTER K_CURRENT? K_USER !(K_SET | K_STATUS) SymbolicNameString? (SetPassword | SetStatus)+ {pin=3}
```

### GRANT privilege — incomplete CreatePrivilege coverage causes parse error

`GRANT CREATE ON DBMS TO admin` produces a PsiError after `GRANT`.

Root cause: `CreatePrivilege` lists specific `K_CREATE <type> K_ON K_DBMS` forms
(DATABASE, ALIAS, ROLE, USER, COMPOSITE DATABASE) but does not cover a bare
`K_CREATE K_ON K_DBMS`. The grammar likely needs an additional alternative, or this
specific GRANT syntax is invalid and the test case should be updated to a valid form.

To investigate: verify whether `GRANT CREATE ON DBMS TO admin` is valid Neo4j 5 Cypher.
If valid, add the missing alternative to `CreatePrivilege`. If not valid, remove or
replace the test case in `PrivilegeCommands.cyp`.

### CALL IN TRANSACTIONS without RETURN causes a spurious parse error

`CALL { ... } IN TRANSACTIONS OF 1 ROW ON ERROR FAIL;` produces a
`<reading clause>, REPORT or RETURN expected` error even though the statement
is valid Cypher 5.

Root cause: `ReadingWithReturn ::= ReadingClause* Return {pin=1}` — the `pin=1`
triggers on `ReadingClause*` which always matches (even zero times), committing
the parser before it can determine whether a `RETURN` is actually present.
`CALL { }` is a `ReadingClause`, so the parser commits and then fails when it
finds `;` instead of `RETURN`.

Fix: change `SinglePartQuery` to add a third alternative for standalone reading
clauses (valid in Cypher 5 for side-effect queries):

```bnf
SinglePartQuery ::= ((ReadingClause* UpdatingClause+ Return?) | ReadingWithReturn | StandaloneReadingClauses)
ReadingWithReturn ::= ReadingClause* Return {pin=2 recoverWhile=statement_recover}
private StandaloneReadingClauses ::= ReadingClause+ {pin=1 recoverWhile=statement_recover}
```

`pin=2` on `ReadingWithReturn` delays the commit until `K_RETURN` is actually
seen, allowing backtrack into `StandaloneReadingClauses` when no `RETURN` follows.
This change will alter the PSI trees for all tests that contain standalone reading
clauses without `RETURN` — `.txt` files must be regenerated.

