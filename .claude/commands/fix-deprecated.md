Cherche toutes les utilisations d'API IntelliJ dépréciées dans le projet.

1. Exécute le vérificateur de plugin :
```bash
./gradlew :graph-database-plugin:verifyPlugin 2>&1 | tail -200
```

2. Cherche aussi les usages manuels :
```bash
grep -rn "Deprecated" --include="*.java" -l
```

3. Pour chaque API dépréciée signalée :
   - Identifie le remplacement dans le SDK IntelliJ 2025.3
   - Montre le code avant/après avec le contexte (5 lignes autour)
   - Précise le package/import complet de la nouvelle API

Limite-toi à **5 corrections à la fois** pour que je puisse vérifier avant de continuer.

Après mes validations, je te dirai "continue" pour les 5 suivantes.
