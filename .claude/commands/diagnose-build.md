Lance le build du plugin et analyse les erreurs.

Exécute :
```powershell
.\gradlew.bat :graph-database-plugin:buildPlugin 2>&1 | Select-Object -Last 100
```

Pour chaque erreur trouvée :
1. Identifie le fichier source et la ligne
2. Explique la cause (API supprimée, import manquant, signature changée...)
3. Propose la correction exacte avec l'import complet
4. N'applique PAS la correction — présente-la moi d'abord

Regroupe les erreurs par catégorie :
- Imports manquants ou cassés
- API dépréciées / supprimées
- Signatures de méthodes changées
- Problèmes de configuration Gradle

Traite les erreurs module par module dans cet ordre : database, language, platform, ui, graph-database-plugin.

Note : on est sous Windows avec PowerShell 7.x. Utilise `.\gradlew.bat` (pas `./gradlew`).
