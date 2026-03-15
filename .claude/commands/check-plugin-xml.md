Analyse le fichier plugin.xml du projet.

Lis le fichier :
```bash
cat graph-database-plugin/src/main/resources/META-INF/plugin.xml
```

Vérifie point par point :

1. **`<depends>`** : Chaque dépendance référence-t-elle un module/plugin qui existe dans IntelliJ 2025.3 (build 253) ? Les plus courants :
   - `com.intellij.modules.platform` → OK
   - `com.intellij.modules.lang` → OK
   - `com.intellij.java` → OK si le plugin dépend du support Java
   - Signale tout module obsolète ou renommé

2. **`<extensions>`** : Chaque Extension Point (EP) déclaré est-il toujours valide dans 253 ?
   Cherche dans le code source si les classes d'implémentation existent toujours.

3. **`<applicationService>` et `<projectService>`** : Les classes référencées existent-elles ?
   ```bash
   # Pour chaque serviceImplementation="com.xxx.YYY", vérifie :
   find . -name "YYY.java" -type f
   ```

4. **`<idea-version>`** : Est-ce cohérent avec gradle.properties (sinceBuild=253, untilBuild=253.*) ?
   Note : le plugin Gradle 2.x patche automatiquement ces valeurs, donc si elles sont codées en dur dans le XML, signale-le.

5. **Notifications et toolwindows** : Vérifie les `<toolWindow>` et `<notificationGroup>` pour la compatibilité.

Liste les problèmes trouvés et propose les corrections exactes du XML.
