# Motd Plugin pour Hytale

Ce plugin expose un menu d'aide multi-pages directement dans le jeu. Il donne accès à un sommaire, des pages détaillées avec retour au sommaire, une navigation scrollable et des raccourcis `/motd`, `/info` et `/help` (la commande reste réservée aux joueurs afin de ne pas interférer avec la commande `help` administrateur).

## Fonctionnalités
- Interface graphique simple avec un sommaire à gauche et l'aperçu d'une section à droite.
- Chargement du contenu depuis des fichiers JSON (un fichier principal + une collection de pages).
- Possibilité d'ajouter un fichier JSON par page, de placer chaque section dans `Jjeanniard_Motd/pages`.
- Bouton « Retour » pour revenir au sommaire et bouton « Fermer » pour quitter le menu.

## Commandes disponibles
- `/motd`
- `/info`
- `/help` (alias joueur, la commande admin demeure inchangée grâce à la restriction au GameMode Adventure)

Ces commandes ouvrent toutes la même interface `UiMotd` et s'appuient sur la configuration JSON chargée au démarrage.

## Navigation
1. Ouvrez `/motd` (ou `/info` ou `/help`) pour afficher l'interface d'aide.
2. Cliquez sur une section du sommaire pour charger ses détails dans la colonne de droite.
3. Utilisez le bouton « Retour » pour revenir au sommaire ou « Fermer » pour quitter.

## Configuration JSON

### Fichier principal
`src/main/resources/Jjeanniard_Motd/Motd.json` contient un tableau `Motd` de sections par défaut. Chaque section doit exposer :

```json
{
  "Id": "identifiant",
  "Title": "Titre affiché",
  "Summary": "Résumé court visible dans le sommaire",
  "Text": [
    "Ligne 1",
    "Ligne 2"
  ]
}
```

### Pages supplémentaires
Copiez ou créez un fichier JSON par page dans `src/main/resources/Jjeanniard_Motd/pages/`. Lors de la première exécution, ces fichiers sont copiés dans le dossier de données `MotdPages` et sont chargés automatiquement au démarrage.

### Ajouter ou modifier une page
1. Ajoutez un fichier `pages/votre-page.json` avec la structure ci-dessus.
2. Redémarrez le serveur (la configuration est lue au démarrage, mais les fichiers supplémentaires peuvent être modifiés directement dans `MotdPages` après le premier lancement).

## Développement
### Prérequis
- Java 25 SDK
- Hytale Server (instance `release` ou `pre-release`)

### Compilation
```bash
./gradlew build
```

Le fichier compilé se trouve dans `build/libs/`.

## Contribution
Les contributions sont bienvenues. Ouvrez une *issue* pour signaler un bug ou soumettre une *pull request* pour ajouter des contenus et formes d'aide supplémentaires.

## Licence
Ce projet est distribué sous licence MIT (voir le fichier `LICENSE`).
